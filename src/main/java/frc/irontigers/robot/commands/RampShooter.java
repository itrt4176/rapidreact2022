// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.Shooter;

public class RampShooter extends CommandBase {
  private Shooter shooter;
  private int rpmSpeed;
  private double ouputIncrease;

  /** Creates a new RampShooter. */
  public RampShooter(Shooter shooter, int rpmSpeed, int rampTime) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    addRequirements(this.shooter);
    this.rpmSpeed = rpmSpeed;

    this.ouputIncrease = 1 / (rampTime / 20.0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.set(shooter.get() + ouputIncrease);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return shooter.getRPM() >= rpmSpeed;
  }
}
