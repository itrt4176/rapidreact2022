// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.irontigers.robot.Constants.MagazineVals;
import frc.irontigers.robot.RobotContainer.Direction;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.Magazine.BallGate;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AdvanceBallTwo extends SequentialCommandGroup {
  /** Creates a new AdvanceBallTwo. */
  public AdvanceBallTwo(Magazine magazine, Intake intake) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new RunIntake(intake, Direction.STOP),
      new InstantCommand(() -> magazine.closeGate(BallGate.Rear)),
      new InstantCommand(() -> magazine.setOutput(MagazineVals.DEFAULT_SPEED))
    );
  }
}
