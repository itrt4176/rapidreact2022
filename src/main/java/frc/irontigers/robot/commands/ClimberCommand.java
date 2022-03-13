// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.RobotContainer.Direction;
import frc.irontigers.robot.subsystems.*;
import frc.irontigers.robot.Constants;
import frc.irontigers.robot.subsystems.Climber;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

public class ClimberCommand extends CommandBase {
  /** Creates a new ClimberCommand. */
  private Climber climber;
  private Direction direction;
  private double extensionValue;
  public boolean finished;
  public ClimberCommand(Climber climber, Direction direction) {
    this.climber = climber;
    this.direction = direction;
    addRequirements(climber);
    
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    finished = false;
  }

  

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    extensionValue = climber.getMotorPosition();
    switch(direction){
      case FORWARD:
      climber.set(-.3);
      // extensionValue = climber.getMotorPosition();
      if(extensionValue <= Constants.ClimberVals.MAX_EXTENSION){
        climber.set(0); //this is assuming it's going to check this every scheduler
        finished = true;
      }
      break;
      case BACKWARD:
      climber.set(.2);
      // extensionValue = climber.getMotorPosition();
      if(extensionValue >= -Constants.ClimberVals.MAX_EXTENSION){
        climber.set(0);
        finished = true;
      }
      break;
      case STOP:
      // extensionValue = climber.getMotorPosition();
      climber.set(0);
      finished = true;
      break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished(){
    return finished;
  }
}