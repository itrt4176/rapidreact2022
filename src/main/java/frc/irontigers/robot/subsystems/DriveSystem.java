// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.irontigers.robot.Constants;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

public class DriveSystem extends SubsystemBase {
  /** Creates a new DriveSystem. */

  private WPI_TalonFX leftFront;
  private WPI_TalonFX leftBack;
  private WPI_TalonFX rightFront;
  private WPI_TalonFX rightBack;

  private MecanumDrive drive;

  public DriveSystem() {
    leftFront = new WPI_TalonFX(Constants.DriveSystemVals.FRONT_LEFT);
    leftBack = new WPI_TalonFX(Constants.DriveSystemVals.BACK_LEFT);
    rightFront = new WPI_TalonFX(Constants.DriveSystemVals.FRONT_RIGHT);
    rightBack = new WPI_TalonFX(Constants.DriveSystemVals.BACK_RIGHT);

    drive = new MecanumDrive(leftFront, leftBack, rightFront, rightBack);
  }

  public void drive() {
    drive.driveCartesian(Constants.DriveSystemVals.DEFAULT_SPEED, 0.0, 0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
