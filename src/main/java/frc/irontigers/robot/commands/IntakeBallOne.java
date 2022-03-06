// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.irontigers.robot.RobotContainer.Direction;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.magazine.BallStates;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.BallStates.PositionState;
import frc.irontigers.robot.subsystems.magazine.Magazine.BallGate;
import frc.irontigers.robot.utils.StateTransitionCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class IntakeBallOne extends StateTransitionCommand<BallStates> {

  /** Creates a new IntakeBallOne. */
  public IntakeBallOne(Shooter shooter, Magazine magazine, Intake intake) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new InstantCommand(() -> shooter.set(0)),
      new InstantCommand(() -> magazine.openGate(BallGate.Front)),
      new InstantCommand(() -> magazine.openGate(BallGate.Rear)),
      new InstantCommand(() -> magazine.setOutput(0)),
      new RunIntake(intake, Direction.STOP)
    );

    setNextSelector(magazine::getState);

    addNextState(new BallStates(PositionState.UNKNOWN, PositionState.EMPTY, PositionState.EMPTY, PositionState.EMPTY), new ReadColorCommand(magazine, shooter, intake));
  }
}
