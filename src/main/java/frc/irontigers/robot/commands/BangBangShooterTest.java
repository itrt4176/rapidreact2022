// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.Shooter;

public class BangBangShooterTest extends CommandBase {
  private final Shooter shooter;
  private final int speed;
  private final BangBangController bangBang;

  /** Creates a new BangBangShooterTest. */
  public BangBangShooterTest(Shooter shooter, int rpmSpeed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    addRequirements(this.shooter);
    speed = rpmSpeed;
    bangBang = new BangBangController();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    bangBang.setSetpoint(speed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.set(bangBang.calculate(shooter.getRPM()));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
