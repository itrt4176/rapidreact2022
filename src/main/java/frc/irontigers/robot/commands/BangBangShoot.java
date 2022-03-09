// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.Shooter;

public class BangBangShoot extends CommandBase {
  private final Shooter shooter;
  private double speed;
  private final BangBangController bangBang;

  /** Creates a new BangBangShooterTest. */
  public BangBangShoot(Shooter shooter, double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooter = shooter;
    addRequirements(this.shooter);
    this.speed = speed;
    bangBang = new BangBangController();
  }

  public void increaseSpeed() {
    speed += 1;
    bangBang.setSetpoint(speed);
  }

  public void decreaseSpeed() {
    speed -= 1;
    bangBang.setSetpoint(speed);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    bangBang.setSetpoint(speed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.set(bangBang.calculate(shooter.getMPS()));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.set(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
