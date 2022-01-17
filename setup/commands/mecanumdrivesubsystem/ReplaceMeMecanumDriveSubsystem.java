// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.wpilibj.commands.mecanumdrivesubsystem;

import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import frc.tigerlib.subsystem.drive.MecanumDriveSubsystem;

public class ReplaceMeMecanumDriveSubsystem extends MecanumDriveSubsystem {
  /** Creates a new ReplaceMeMecanumDriveSubsystem. */
  public ReplaceMeMecanumDriveSubsystem() {
    // Setup the drive subsystem using the following methods:
    // setGyro()
    // setMotors()
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    super.periodic();
  }

  // Return wheel speeds in m/s
  @Override
  protected MecanumDriveWheelSpeeds getWheelSpeeds() {
    return new MecanumDriveWheelSpeeds(
        0,
        0,
        0,
        0
      );
  }

  // Reset encoders
  @Override
  protected void resetEncoders() {

    
  }
}
