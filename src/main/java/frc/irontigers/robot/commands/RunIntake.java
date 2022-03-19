// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.Constants;
import frc.irontigers.robot.RobotContainer.Direction;

public class RunIntake extends CommandBase {
  /** Creates a new RunIntake. */
  private Intake intake;
  private Direction direction;

  private StringLogEntry directionLog;

  public RunIntake(Intake intake, Direction direction) {
    this.intake = intake;
    this.direction = direction;
    addRequirements(intake);
    // Use addRequirements() here to declare subsystem dependencies.

    directionLog = new StringLogEntry(DataLogManager.getLog(), "intake/command/set");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    switch (direction) {
      case FORWARD:
     intake.set(Constants.IntakeVals.DEFAULT_SPEED);
        break;
      case BACKWARD:
      intake.set(-Constants.IntakeVals.DEFAULT_SPEED);
        break;
      case STOP:
        intake.set(0);
        break;
    }

    directionLog.append(direction.toString());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
