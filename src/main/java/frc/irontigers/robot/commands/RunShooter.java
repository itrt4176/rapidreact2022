// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DataLogEntry;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.magazine.BallStates;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.utils.AdjustableInterpolatingTreeMap;
import frc.tigerlib.interpolable.InterpolatingDouble;
import frc.tigerlib.interpolable.InterpolatingTreeMap;

import static frc.irontigers.robot.Constants.VisionVals.*;

import java.util.Collection;
import java.util.Set;

import static frc.irontigers.robot.Constants.ShooterVals.*;

public class RunShooter extends CommandBase {
  private final Shooter shooter;
  private final Magazine magazine;
  private double targetRPM;
  private double distance;
  private SlewRateLimiter ramper;
  private final MedianFilter smoother;
  private final PhotonCamera camera;
  private final AdjustableInterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> distanceMap;

  private final DoubleLogEntry distanceLog;
  private final DoubleLogEntry targetRPMLog;
  private final DoubleLogEntry errorLog;
  private final DoubleLogEntry rpmLog;

  private int shotCount;

  private final NetworkTable ntShooterMap;
  private final NetworkTableEntry distanceKeys;
  private final NetworkTableEntry rpmVals;

  /** Creates a new RunShooter. */
  public RunShooter(Shooter shooter, Magazine magazine, PhotonCamera camera) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.camera = camera;
    this.magazine = magazine;
    addRequirements(this.shooter);
    smoother = new MedianFilter(40);

    ntShooterMap = NetworkTableInstance.getDefault().getTable("ShooterMap");
    ntShooterMap.getEntry(".type").setString("RobotPreferences");
    // Listener to set all Preferences values to persistent
    // (for backwards compatibility with old dashboards).
    ntShooterMap.addEntryListener(
        (table, key, entry, value, flags) -> entry.setPersistent(),
        EntryListenerFlags.kImmediate | EntryListenerFlags.kNew);
    HAL.report(tResourceType.kResourceType_Preferences, 1);

    distanceMap = new AdjustableInterpolatingTreeMap<>();

    double[] defaultDist = { 3.263, 4.173, 5.117, 6.134 };
    double[] defaultRpms = { 4950.0, 5125.0, 5355.0, 6125.0 };

    distanceKeys = ntShooterMap.getEntry("distances");
    distanceKeys.setDefaultDoubleArray(defaultDist);
    distanceKeys.setPersistent();

    rpmVals = ntShooterMap.getEntry("rpms");
    rpmVals.setDefaultDoubleArray(defaultRpms);
    rpmVals.setPersistent();

    double[] distances = distanceKeys.getDoubleArray(defaultDist);
    double[] rpms = rpmVals.getDoubleArray(defaultRpms);

    for (int i = 0; i < distances.length; i++) {
      distanceMap.put(new InterpolatingDouble(distances[i]), new InterpolatingDouble(rpms[i]));
    }

    // distanceMap.put(new InterpolatingDouble(3.263), new InterpolatingDouble(4950.0));
    // distanceMap.put(new InterpolatingDouble(4.173), new InterpolatingDouble(5125.0));
    // distanceMap.put(new InterpolatingDouble(5.117), new InterpolatingDouble(5355.0));
    // distanceMap.put(new InterpolatingDouble(6.134), new InterpolatingDouble(6125.0));

    shotCount = 0;

    DataLog log = DataLogManager.getLog();
    distanceLog = new DoubleLogEntry(log, "shooter/vision/distance");
    targetRPMLog = new DoubleLogEntry(log, "shooter/targetRPM");
    rpmLog = new DoubleLogEntry(log, "shooter/rpm");
    errorLog = new DoubleLogEntry(log, "shooter/error");
  }

  public void increaseSpeed() {
    targetRPM += 25;
    SmartDashboard.putNumber("Shooter Setpoint (RPM)", targetRPM);
  }

  public void decreaseSpeed() {
    targetRPM -= 25;
    SmartDashboard.putNumber("Shooter Setpoint (RPM)", targetRPM);
  }

  public void adjustDistanceMap(ShotResult result) {
    if (distance == 0) {
      return;
    }

    switch (result) {
      case UNDERSHOT:
        distanceMap.replaceNearest(new InterpolatingDouble(distance), new InterpolatingDouble(targetRPM + 10));
        break;
      case OVERSHOT:
        distanceMap.replaceNearest(new InterpolatingDouble(distance), new InterpolatingDouble(targetRPM - 10));
        break;
      case SCORE:
        distanceMap.put(new InterpolatingDouble(distance), new InterpolatingDouble(targetRPM));
        break;
    }

    InterpolatingDouble[] distances = new InterpolatingDouble[distanceMap.size()];
    distanceMap.keySet().toArray(distances);
    double[] distancesArr = new double[distances.length];

    InterpolatingDouble[] rpms = new InterpolatingDouble[distanceMap.size()];
    distanceMap.values().toArray(rpms);
    double[] rpmsArr = new double[rpms.length];


    for (int i = 0; i < distances.length; i++) {
      distancesArr[i] = distances[i].value;
      rpmsArr[i] = rpms[i].value;
    }

    distanceKeys.setDoubleArray(distancesArr);
    rpmVals.setDoubleArray(rpmsArr);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    camera.setDriverMode(false);
    shotCount++;
    distanceLog.setMetadata("Shot " + shotCount);
    targetRPMLog.setMetadata("Shot " + shotCount);
    rpmLog.setMetadata("Shot " + shotCount);
    errorLog.setMetadata("Shot " + shotCount);

    ramper = new SlewRateLimiter(4640);
    // magazine.openGate(BallGate.Rear);

    PhotonTrackedTarget target = camera.getLatestResult().getBestTarget();

    if (target != null) {
      distance = PhotonUtils.calculateDistanceToTargetMeters(CAM_HEIGHT, TARGET_HEIGHT, CAM_ANGLE, Units.degreesToRadians(target.getPitch()));

      SmartDashboard.putNumber("Distance to target (m)", distance);
      distanceLog.append(distance);

      targetRPM = distanceMap.getInterpolated(new InterpolatingDouble(distance)).value;

      if (targetRPM == 0) {
        targetRPM = 5200;
      }

      targetRPMLog.append(targetRPM);

      SmartDashboard.putNumber("Shooter Setpoint (RPM)", targetRPM);
    } else {
      distance = 0;
      distanceLog.append(0);
      targetRPM = 5200;
    }
  }

  @Override
  public void execute() {
    shooter.setVelocity(ramper.calculate(targetRPM));
  }

  public boolean isReady() {
    double currentRPM = shooter.getRPM();
    double error = currentRPM - targetRPM;
    double smoothedError = smoother.calculate(error);

    SmartDashboard.putNumber("Shooter Error (RPM)", error);
    SmartDashboard.putNumber("Median Error (RPM)", smoothedError);
    SmartDashboard.putNumber("Shooter Velocity (RPM)", currentRPM);

    errorLog.append(smoothedError);
    rpmLog.append(currentRPM);

    return smoothedError >= 0.0 && smoothedError <= 15.0;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.set(DEFAULT_SPEED);
    smoother.reset();
    // camera.setDriverMode(true);
    // magazine.closeGate(BallGate.Front);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return magazine.getState().equals(new BallStates());
    return false;
  }

  public enum ShotResult {
    UNDERSHOT,
    SCORE,
    OVERSHOT
  }
}
