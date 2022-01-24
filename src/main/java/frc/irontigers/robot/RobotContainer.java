// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot;

import edu.wpi.first.wpilibj.GenericHID;
import frc.tigerlib.XboxControllerIT;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.InFeed;

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
  private JoystickButton shooterON = new JoystickButton(controller, Button.kRightBumper.value);
  private JoystickButton shooterOFF = new JoystickButton(controller, Button.kLeftBumper.value);

  private JoystickButton increaseIntake = new JoystickButton(controller, Button.kB.value);
  private JoystickButton decreaseIntake = new JoystickButton(controller, Button.kX.value);

  private JoystickButton infeedON = new JoystickButton(controller, Button.kStart.value);
  private JoystickButton infeedOFF = new JoystickButton(controller, Button.kBack.value);


  private Shooter shooter = new Shooter();
  private Intake intake = new Intake();
  private InFeed infeed = new InFeed();

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
    shooterON.whenPressed(() -> shooter.turnON());
    shooterOFF.whenPressed(() -> shooter.turnOFF());

    // increaseIntake.whenPressed(() -> intake.speedUP());
    // decreaseIntake.whenPressed(() -> intake.slowDOWN());

    infeedON.whenPressed(() -> infeed.set(0.3));
    infeedOFF.whenPressed(() -> infeed.set(0));
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
