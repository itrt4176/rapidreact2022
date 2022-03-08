// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands.ballstate;





import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.irontigers.robot.RobotContainer.Direction;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.magazine.BallStates;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.BallStates.PositionState;
import frc.irontigers.robot.utils.StateTransitionCommand;
import pabeles.concurrency.IntOperatorTask;
import frc.irontigers.robot.Constants;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RejectBallTwo extends StateTransitionCommand<BallStates> {
  /** Creates a new RejectBallTwo. 
   * @param shooter
   * */
  public RejectBallTwo(Magazine magazine, Intake intake, Shooter shooter) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new InstantCommand(() -> magazine.setOutput(-Constants.MagazineVals.DEFAULT_SPEED)),
      new RunIntake(intake, Direction.BACKWARD));

      setNextSelector(magazine:: getState);

      addNextState(
        new BallStates(PositionState.EMPTY, PositionState.RIGHT, PositionState.EMPTY, PositionState.EMPTY), 
        () -> new AdvanceBallOne(shooter, magazine, intake)
      );

      addNextState(
        new BallStates(PositionState.EMPTY, PositionState.EMPTY, PositionState.RIGHT, PositionState.EMPTY), 
        () -> new StoreBallOne(magazine, shooter, intake)
      );

  }
}
