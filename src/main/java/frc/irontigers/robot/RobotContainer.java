// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot;

import edu.wpi.first.wpilibj.GenericHID;
import frc.irontigers.robot.Constants.Flywheel;
import frc.irontigers.robot.commands.BangBangShooterTest;
import frc.tigerlib.XboxControllerIT;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import static edu.wpi.first.wpilibj.XboxController.Button.*;
import frc.irontigers.robot.subsystems.Shooter;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  // private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */

  private XboxController controller = new XboxController(0);
  private JoystickButton increaseSpeed = new JoystickButton(controller, kRightBumper.value);
  private JoystickButton decreaseSpeed = new JoystickButton(controller, kLeftBumper.value);
  private JoystickButton startBangBang = new JoystickButton(controller, kStart.value);
  private JoystickButton stopBangBang = new JoystickButton(controller, kBack.value);

  private Shooter shooter = new Shooter();

  private BangBangShooterTest bangBangTest = new BangBangShooterTest(shooter, 2500);

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxControllerIT}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    increaseSpeed.whenPressed(() -> shooter.speedUP());
    decreaseSpeed.whenPressed(() -> shooter.slowDOWN());

    startBangBang.whenPressed(bangBangTest);
    stopBangBang.cancelWhenPressed(bangBangTest);
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }
}
