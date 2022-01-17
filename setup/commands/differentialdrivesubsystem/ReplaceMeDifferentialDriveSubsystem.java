// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the docs directory of this project.

package edu.wpi.first.wpilibj.commands.differentialdrivesubsystem;

import frc.tigerlib.subsystem.drive.DifferentialDriveSubsystem;

public class ReplaceMeDifferentialDriveSubsystem extends DifferentialDriveSubsystem {
    /** Creates a new ReplaceMeDifferentialDriveSubsystem */
    public ReplaceMeDifferentialDriveSubsystem() {
        // Setup the drive subsystem using the following methods:
        // setGyro()
        // setMotors()
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        super.periodic();
    }

    // Return the distance from the left encoder
    @Override
    protected double getLeftDistance() {
        return 0;
    }

    // Return the distance from the right encoder
    @Override
    protected double getRightDistance() {
        return 0;
    }

    // Reset the encoders
    @Override
    protected void resetEncoders() {

    }
}
