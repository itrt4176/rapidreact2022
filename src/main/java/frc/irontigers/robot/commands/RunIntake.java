// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.Constants;
import frc.irontigers.robot.RobotContainer.Direction;

public class RunIntake extends CommandBase {
  /** Creates a new RunIntake. */
  private Intake intake;
  private Direction direction;
  public RunIntake(Intake intake, Direction direction) {
    this.intake = intake;
    this.direction = direction;
    addRequirements(intake);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch(direction){
      case FORWARD:
      while (intake.get()<Constants.IntakeVals.DEFAULT_SPEED){
        intake.set(intake.get()+0.05);
      }
        break;
      case BACKWARD:
      while (intake.get()>-Constants.IntakeVals.DEFAULT_SPEED){
        intake.set(intake.get()-0.05);
      }
        break;
      case STOP:
        intake.set(0);
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished(){
    return false;
  }
}
