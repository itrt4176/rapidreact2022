// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;



import javax.swing.text.Position;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.irontigers.robot.Constants.MagazineVals;
import frc.irontigers.robot.RobotContainer.Direction;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.magazine.BallStates;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.BallStates.PositionState;
import frc.irontigers.robot.utils.StateTransitionCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Advance extends StateTransitionCommand<BallStates> {
  /** Creates a new Advance. */
  public Advance(Shooter shooter, Intake intake, Magazine magazine) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new RunIntake(intake, Direction.STOP),
      new InstantCommand(() -> magazine.setOutput(MagazineVals.DEFAULT_SPEED))
    );

    setNextSelector(magazine::getState);

    addNextState(
      new BallStates(PositionState.EMPTY, PositionState.RIGHT, PositionState.EMPTY, PositionState.EMPTY), 
      () -> new AdvanceBallOne(shooter, magazine, intake)
    );

    addNextState(
      new BallStates(PositionState.EMPTY, PositionState.RIGHT, PositionState.RIGHT, PositionState.EMPTY), 
      () -> new StoreBallTwo(shooter, magazine, intake)
    );

    addNextState(
      new BallStates(PositionState.EMPTY, PositionState.WRONG, PositionState.EMPTY, PositionState.EMPTY),
      () -> new rejectBallOne(shooter, magazine, intake));

    addNextState(
      new BallStates(PositionState.UNKNOWN, PositionState.WRONG, PositionState.EMPTY, PositionState.EMPTY), 
      () -> new ReadColorCommand(magazine, shooter, intake));
  }
}
