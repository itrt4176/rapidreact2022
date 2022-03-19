// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import org.photonvision.PhotonCamera;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.irontigers.robot.Constants.MagazineVals;
import frc.irontigers.robot.RobotContainer.Direction;
import frc.irontigers.robot.commands.ballstate.IntakeBallOne;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.magazine.BallStates;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.Magazine.BallGate;

import static frc.irontigers.robot.subsystems.magazine.BallStates.PositionState.*;
import frc.irontigers.robot.utils.StateTransitionCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Shoot extends SequentialCommandGroup {
  private RunShooter runShooter;

  /** Creates a new Shoot. */
  public Shoot(Intake intake, Magazine magazine, Shooter shooter, PhotonCamera camera) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    // addCommands(
    //   new RunIntake(intake, Direction.STOP),
    //   new RampShooter(shooter, 4000, 1000),
    //   new InstantCommand(() -> magazine.setOutput(MagazineVals.DEFAULT_SPEED))
    // );

    runShooter = new RunShooter(shooter, magazine, camera);

    ConditionalCommand ballControl = new ConditionalCommand(
        new InstantCommand(() -> {
          magazine.openGate(BallGate.Both);
        },
            magazine),
        new InstantCommand(() -> {
          magazine.closeGate(BallGate.Front);
        },
            magazine),
        runShooter::isReady);

    ParallelDeadlineGroup shoot = new ParallelDeadlineGroup(runShooter, ballControl);

    addCommands(
        new RunIntake(intake, Direction.STOP),
        new InstantCommand(() -> magazine.setOutput(MagazineVals.DEFAULT_SPEED), magazine),
        shoot,
        new IntakeBallOne(shooter, magazine, intake));
  }
  
  public void increaseSpeed() {
    runShooter.increaseSpeed();
  }

  public void decreaseSpeed() {
    runShooter.decreaseSpeed();
  }
}
