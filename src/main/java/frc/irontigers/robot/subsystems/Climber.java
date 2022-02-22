// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.irontigers.robot.Constants;

public class Climber extends SubsystemBase {
  /** Creates a new Climber. */

  private WPI_TalonFX climber;
  public double extentionValue;

  public Climber() {
    climber = new WPI_TalonFX(Constants.ClimberVals.MOTOR_ID);
  }

  public void set(double speed) {
    climber.set(speed);
  }
  public double getMotorPosition(){
    return climber.getSelectedSensorPosition();
  }

  public double get() {
    return climber.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  SmartDashboard.putNumber("Climber Position", getMotorPosition());
  }
}
