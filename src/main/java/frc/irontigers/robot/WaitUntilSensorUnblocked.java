// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.Magazine.BallSensor;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class WaitUntilSensorUnblocked extends SequentialCommandGroup {
  /** Creates a new WaitUntilSensorUnblocked. */
  public WaitUntilSensorUnblocked(Magazine magazine, BallSensor sensor) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
        new WaitUntilCommand(() -> magazine.readBallSensor(sensor)),
        new WaitUntilCommand(() -> !magazine.readBallSensor(sensor))
    );
  }
}
