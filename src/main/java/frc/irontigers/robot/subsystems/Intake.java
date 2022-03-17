// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.irontigers.robot.Constants;
import frc.irontigers.robot.Constants.IntakeVals;


public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private WPI_TalonSRX intake;
  private Solenoid deployer;

  public Intake() {
    intake = new WPI_TalonSRX(IntakeVals.MOTOR_ID);
    deployer = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.IntakeVals.DEPLOY_ID);
  }

  public void set(double speed) {
    intake.set(speed);
  }

  public double get() {
    return intake.get();
  }

  public void deploy() {
    deployer.set(true);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Intake Speed", get());
  }
}
