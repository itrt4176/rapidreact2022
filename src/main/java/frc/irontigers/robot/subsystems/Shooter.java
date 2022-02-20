// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import static frc.irontigers.robot.Constants.ShooterVals.*;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.irontigers.robot.Constants;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */

  private WPI_TalonFX flywheel;

  public Shooter() {
    flywheel = new WPI_TalonFX(0);
  }

  public double getRPM() {
    return flywheel.getSelectedSensorVelocity()*600/2048;
  }

  public double getMPS() {
    return getRPM()/60 * Math.PI * Constants.ShooterVals.Diameter;
  }

  public void set(double speed) {
    flywheel.set(speed);
  }

  public double get() {
    return flywheel.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Shooter RPM", getRPM());
    SmartDashboard.putNumber("Shooter Speed", get());
  }
}
