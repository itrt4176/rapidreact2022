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
    ramper = new SlewRateLimiter(4640);
    // magazine.openGate(BallGate.Rear);

    PhotonTrackedTarget target = camera.getLatestResult().getBestTarget();

    if (target != null) {
      double distance = PhotonUtils.calculateDistanceToTargetMeters(CAM_HEIGHT, TARGET_HEIGHT, CAM_ANGLE, Units.degreesToRadians(target.getPitch()));

      SmartDashboard.putNumber("Distance to target (m)", distance);

      targetRPM = distanceMap.getInterpolated(new InterpolatingDouble(distance)).value;

      if (targetRPM == 0) {
        cancel();
      }

      SmartDashboard.putNumber("Shooter Setpoint (RPM)", targetRPM);
    } else {
      cancel();
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

    // if (smoothedError >= 0 && smoothedError <= 18.5) {
    //   // magazine.openGate(BallGate.Front);
    //   // magazine.setOutput(MagazineVals.DEFAULT_SPEED);
    //   magazine.openGate(BallGate.Both);
    //   SmartDashboard.putBoolean("Shootable", true);
    // } else {
    //   magazine.closeGate(BallGate.Front);
    //   // magazine.setOutput(0);
    //   SmartDashboard.putBoolean("Shootable", false);
    // }

    SmartDashboard.putNumber("Shooter Error (RPM)", error);
    SmartDashboard.putNumber("Median Error (RPM)", smoothedError);
    SmartDashboard.putNumber("Shooter Velocity (RPM)", currentRPM);
    
    return smoothedError >= 0.0 && smoothedError <= 15.0;
  }

 /*  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // shooter.set(bangBang.calculate(shooter.getMPS()));
    double currentSpeed = shooter.getMPS();
    // double error = speed - currentSpeed;
    // double p;

    // if (Math.abs(error) <= 3) {
    //   p = 0.005;
    // } else if (Math.abs(error) <= 5) {
    //   p = 0.0125;
    // } else {
    //   p = 0.0025;
    // }

    // double output = shooter.get() + (p * error);

    // if (Math.abs(error) > 0.25) {
    //   shooter.set(output);
    // }

    shooter.set(shooter.get() + finePid.calculate(currentSpeed));
    SmartDashboard.putNumber("Shooter mps", currentSpeed);

    currentSpeed = shooter.getMPS();

    double smoothSpeed = vAverage.calculate(currentSpeed);

    if (Math.abs(speed - smoothSpeed) <= 0.5) {
      magazine.openGate(BallGate.Front);
    } else {
      magazine.closeGate(BallGate.Front);
    }

    SmartDashboard.putNumber("Shooter mps", currentSpeed);
    SmartDashboard.putNumber("Average Speed", smoothSpeed);
  } */

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