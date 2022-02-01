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
import static frc.irontigers.robot.Constants.*;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.Magazine;

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
  private final Shooter shooter = new Shooter();
  private final Intake intake = new Intake();
  private final Magazine magazine = new Magazine();

  private final XboxControllerIT controller = new XboxControllerIT(0);

  private final JoystickButton shooterOnButton = new JoystickButton(controller, Button.kRightBumper.value);
  private final JoystickButton shooterOffButton = new JoystickButton(controller, Button.kLeftBumper.value);

  private final JoystickButton increaseIntakeButton = new JoystickButton(controller, Button.kB.value);
  private final JoystickButton decreaseIntakeButton = new JoystickButton(controller, Button.kX.value);

  private final JoystickButton magazineOnButton = new JoystickButton(controller, Button.kStart.value);
  private final JoystickButton magazineOffButton = new JoystickButton(controller, Button.kBack.value);


  

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
    shooterOnButton.whenPressed(() -> shooter.set(ShooterVals.DEFAULT_SPEED));
    shooterOffButton.whenPressed(() -> shooter.set(0));

    // increaseIntake.whenPressed(() -> intake.set(intake.get() + 0.05));
    // decreaseIntake.whenPressed(() -> intake.set(intake.get() - 0.05));

    magazineOnButton.whenPressed(() -> magazine.set(MagazineVals.DEFAULT_SPEED));
    magazineOffButton.whenPressed(() -> magazine.set(0));
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
