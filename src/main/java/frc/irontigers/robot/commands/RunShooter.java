// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DataLogEntry;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.magazine.BallStates;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.tigerlib.interpolable.InterpolatingDouble;
import frc.tigerlib.interpolable.InterpolatingTreeMap;

import static frc.irontigers.robot.Constants.VisionVals.*;
import static frc.irontigers.robot.Constants.ShooterVals.*;

public class RunShooter extends CommandBase {
  private final Shooter shooter;
  private final Magazine magazine;
  private double targetRPM;
  private SlewRateLimiter ramper;
  private final MedianFilter smoother;
  private final PhotonCamera camera;
  private final InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> distanceMap;

  private final DoubleLogEntry distanceLog;
  private final DoubleLogEntry targetRPMLog;
  private final DoubleLogEntry errorLog;
  private final DoubleLogEntry rpmLog;

  private int shotCount;

  /** Creates a new RunShooter. */
  public RunShooter(Shooter shooter, Magazine magazine, PhotonCamera camera) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    this.camera = camera;
    this.magazine = magazine;
    addRequirements(this.shooter);
    smoother = new MedianFilter(40);

    distanceMap = new InterpolatingTreeMap<>();

    distanceMap.put(new InterpolatingDouble(3.263), new InterpolatingDouble(4950.0));
    distanceMap.put(new InterpolatingDouble(4.173), new InterpolatingDouble(5125.0));
    distanceMap.put(new InterpolatingDouble(5.117), new InterpolatingDouble(5355.0));
    distanceMap.put(new InterpolatingDouble(6.134), new InterpolatingDouble(6125.0));

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

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shotCount++;
    distanceLog.setMetadata("Shot " + shotCount);
    targetRPMLog.setMetadata("Shot " + shotCount);
    rpmLog.setMetadata("Shot " + shotCount);
    errorLog.setMetadata("Shot " + shotCount);

    ramper = new SlewRateLimiter(4640);
    // magazine.openGate(BallGate.Rear);

    PhotonTrackedTarget target = camera.getLatestResult().getBestTarget();

    if (target != null) {
      double distance = PhotonUtils.calculateDistanceToTargetMeters(CAM_HEIGHT, TARGET_HEIGHT, CAM_ANGLE, Units.degreesToRadians(target.getPitch()));

      SmartDashboard.putNumber("Distance to target (m)", distance);
      distanceLog.append(distance);

      targetRPM = distanceMap.getInterpolated(new InterpolatingDouble(distance)).value;

      if (targetRPM == 0) {
        targetRPMLog.append(-1);
        cancel();
      }

      targetRPMLog.append(targetRPM);

      SmartDashboard.putNumber("Shooter Setpoint (RPM)", targetRPM);
    } else {
      distanceLog.append(-1);
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
    // magazine.closeGate(BallGate.Front);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return magazine.getState().equals(new BallStates());
  }
}
