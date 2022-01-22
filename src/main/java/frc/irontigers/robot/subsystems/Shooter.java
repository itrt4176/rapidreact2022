// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.irontigers.robot.Constants.Flywheel;
import edu.wpi.first.math.MathUtil;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */

  private WPI_TalonFX flywheel;
  private double speed;

  public Shooter() {
    flywheel = new WPI_TalonFX(0);
    speed = Flywheel.DEFAULT_SPEED;
  }

  public double getRPM() {
    return flywheel.getSelectedSensorVelocity()*600/2048;
  }

  public void set(double speed) {
    flywheel.set(speed);
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
    SmartDashboard.putNumber("Shooter RPM", getRPM());
    SmartDashboard.putNumber("Shooter Speed", get());
  }
}