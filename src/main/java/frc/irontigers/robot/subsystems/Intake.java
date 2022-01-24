// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import static frc.irontigers.robot.Constants.Intake.*;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private Talon intake;
  private double speed;

  public Intake() {
    intake = new Talon(MOTOR_ID);
    speed = DEFAULT_SPEED;
  }

  public void set(double speed) {
    intake.set(speed);
  }

  public double get() {
    return speed;
  }

  public void speedUP() {
    speed = MathUtil.clamp(speed + 0.05, -1.0, 1.0);
    set(speed);
  }

  public void slowDOWN() {
    speed = MathUtil.clamp(speed - 0.05, -1.0, 1.0);
    set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Intake Speed", get());
  }
}
