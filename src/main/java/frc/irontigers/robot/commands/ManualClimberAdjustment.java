// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.Climber;
import frc.irontigers.robot.subsystems.magazine.Magazine;

public class ManualClimberAdjustment extends CommandBase {
  /** Creates a new MaualClimberAdjustment. */
  private Climber climber;
  private XboxController controller;

  public ManualClimberAdjustment(Climber climber, XboxController controller) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.climber = climber;
    this.controller = controller;

    addRequirements(climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    climber.set(controller.getRightTriggerAxis() - controller.getLeftTriggerAxis());
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
