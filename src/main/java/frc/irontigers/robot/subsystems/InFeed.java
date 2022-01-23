// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class InFeed extends SubsystemBase {
  /** Creates a new InFeed. */

  private WPI_TalonFX infeed;
  
  public InFeed() {
    infeed = new WPI_TalonFX(1);
  }

  public void set(double speed) {
    infeed.set(speed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
