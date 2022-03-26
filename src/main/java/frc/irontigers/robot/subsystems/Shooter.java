// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import static frc.irontigers.robot.Constants.ShooterVals.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.irontigers.robot.Constants;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */

  private WPI_TalonFX flywheel;

  public Shooter() {
    flywheel = new WPI_TalonFX(7);

    flywheel.setNeutralMode(NeutralMode.Coast);
    flywheel.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30); // timeout taken from github.com/FRC-Sonic-Squirrels/Flywheel-Tuner/2022-CTRE/

    flywheel.config_kF(0, 0.0475, 30);
    flywheel.config_kP(0, 0.0945625, 30);
    flywheel.config_kI(0, 0.0000008510625, 30);
    flywheel.config_kD(0, 0.0, 30);
    // flywheel.config_kD(0, 0.00000009560546875, 30);
    flywheel.config_IntegralZone(0, 1701, 30);
    flywheel.configNominalOutputForward(0, 30);
    flywheel.configNominalOutputReverse(0, 30);
    flywheel.configPeakOutputForward(1, 30);
    flywheel.configPeakOutputReverse(0, 30);
    flywheel.configAllowableClosedloopError(0, (12.5 / (600.0 / 2048.0)), 30);
    flywheel.configVoltageCompSaturation(11.85, 30);
    flywheel.enableVoltageCompensation(true);

    // set(0.25);
    setVelocity(3315);
  }

  public double getRPM() {
    return flywheel.getSelectedSensorVelocity() * (600.0 / 2048.0);
  }

  public double getMPS() {
    return getRPM()/60 * Math.PI * Constants.ShooterVals.DIAMETER;
  }

  public void set(double speed) {
    flywheel.set(speed);
  }

  public double get() {
    return flywheel.get();
  }

  public void spitBall() {
    flywheel.set(0.25);
  }

  public void setVelocity(double rpm) {
    flywheel.set(ControlMode.Velocity, (rpm / (600.0 / 2048.0)));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Shooter RPM", getRPM());
    SmartDashboard.putNumber("Shooter Speed", get());

    SmartDashboard.putNumber("Shooter input voltage", flywheel.getBusVoltage());
  }
}
