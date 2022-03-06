// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.irontigers.robot.Constants;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;
import frc.tigerlib.subsystem.drive.MecanumDriveSubsystem;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.util.Units;

public class DriveSystem extends MecanumDriveSubsystem {
  /** Creates a new DriveSystem. */

  private WPI_TalonFX leftFront;
  private WPI_TalonFX leftBack;
  private WPI_TalonFX rightFront;
  private WPI_TalonFX rightBack;

  private AHRS gyro;

  private MecanumDriveKinematics kinematics;

  public DriveSystem() {
    leftFront = new WPI_TalonFX(Constants.DriveSystemVals.FRONT_LEFT);
    leftBack = new WPI_TalonFX(Constants.DriveSystemVals.BACK_LEFT);
    rightFront = new WPI_TalonFX(Constants.DriveSystemVals.FRONT_RIGHT);
    rightBack = new WPI_TalonFX(Constants.DriveSystemVals.BACK_RIGHT);

    gyro = new AHRS();
    kinematics = new MecanumDriveKinematics(
      new Translation2d(Units.inchesToMeters(-23.5 / 2.0), Units.inchesToMeters(20.25 / 2.0)),
      new Translation2d(Units.inchesToMeters(23.5 / 2.0), Units.inchesToMeters(20.25 / 2.0)),
      new Translation2d(Units.inchesToMeters(-23.5 / 2.0), Units.inchesToMeters(-20.25 / 2.0)),
      new Translation2d(Units.inchesToMeters(23.5 / 2.0), Units.inchesToMeters(-20.25 / 2.0))
    );

    setGyro(gyro);
    setMotors(leftFront, leftBack, rightFront, rightBack, kinematics);
    
  }

  public double motorToWheelSpeed(TalonFX motor) {
    return (motor.getSelectedSensorVelocity()*600/2048)/10.71;
  }


  @Override
  public void drive(double xSpeed, double ySpeed, double rotation) {
      // TODO Remove after library fixed
      super.drive(ySpeed, xSpeed, rotation);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  protected MecanumDriveWheelSpeeds getWheelSpeeds() {
    // TODO Auto-generated method stub
    return new MecanumDriveWheelSpeeds(0, 0, 0, 0);
  }

  @Override
  protected void resetEncoders() {
    // TODO Auto-generated method stub
    leftFront.setSelectedSensorPosition(0);
    leftFront.setSelectedSensorPosition(0);
    rightFront.setSelectedSensorPosition(0);
    rightBack.setSelectedSensorPosition(0);
  }
}
