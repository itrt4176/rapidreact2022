// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.RobotContainer.Direction;
import frc.irontigers.robot.subsystems.*;
import frc.irontigers.robot.Constants;

public class ClimberCommand extends CommandBase {
  /** Creates a new ClimberCommand. */
  private Climber climber;
  private Direction direction;
  private double extensionValue;
  public ClimberCommand(Climber climber, Direction direction, double extensionValue) {
    this.climber = climber;
    this.direction = direction;
    this.extensionValue = extensionValue;
    addRequirements(climber);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch(direction){
      case FORWARD:
      climber.set(Constants.ClimberVals.DEFAULT_SPEED);
      if(extensionValue >= Constants.ClimberVals.MAX_EXTENSION){
        climber.set(0) //this is assuming it's going to check this every scheduler
      }
      break;
      case BACKWARD:
      climber.set(-Constants.ClimberVals.DEFAULT_SPEED);
      if(extensionValue <= Constants.ClimberVals.MIN_EXTENSION){
        climber.set(0)
      }
      break;
      case STOP:
      climber.set(0);
      break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished(){
    switch(direction){
      case FORWARD:
      if(extensionValue >= Constants.ClimberVals.MAX_EXTENSION and climber.get() == 0){
        return true;
      }
      else{
        return false;
      }
      break;
      case BACKWARD:
      if(extensionValue <= Constants.ClimberVals.MIN_LENGTH and climber.get() == 0){
        return true;
      }
      else{
        return false;
      }
      break;
      case STOP:
      if(climber.get() == 0){
        return true;
      }
      else{
        return false;
      }
    }
  }
