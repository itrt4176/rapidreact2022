// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot;

import edu.wpi.first.wpilibj.GenericHID;
import frc.tigerlib.XboxControllerIT;
import frc.tigerlib.XboxControllerIT.DPadDirection;
import frc.tigerlib.command.MecanumJoystickDrive;
import frc.tigerlib.command.button.DPadButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.XboxController.Button;
import static frc.irontigers.robot.Constants.*;

import frc.irontigers.robot.commands.BangBangShooterTest;
import frc.irontigers.robot.commands.HandleS1;
import frc.irontigers.robot.commands.RampShooter;
import frc.irontigers.robot.commands.RunIntake;
//import frc.irontigers.robot.subsystems.Climber;
//import frc.irontigers.robot.commands.ClimberCommand;

import frc.irontigers.robot.subsystems.DriveSystem;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.Magazine.Sensor;
import frc.irontigers.robot.utils.OnClearedTrigger;

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
  public enum Direction {
    FORWARD,
    BACKWARD,
    STOP
  };

  private final Shooter shooter = new Shooter();
  private final Intake intake = new Intake();
  private final Magazine magazine = new Magazine();
  //private final Climber climber = new Climber();


  private final XboxControllerIT controller = new XboxControllerIT(0);

  private final DriveSystem driveSystem = new DriveSystem();
  private final MecanumJoystickDrive joystickDrive = new MecanumJoystickDrive(driveSystem, controller);  

  private final JoystickButton shooterOnButton = new JoystickButton(controller, Button.kRightBumper.value);
  private final JoystickButton shooterOffButton = new JoystickButton(controller, Button.kLeftBumper.value);

  private final JoystickButton intakeForward = new JoystickButton(controller, Button.kY.value);
  private final JoystickButton intakeBackward = new JoystickButton(controller, Button.kA.value);
  private final JoystickButton intakeStop = new JoystickButton(controller, Button.kBack.value);

  
  
  

  //private final JoystickButton climberExtend = new JoystickButton(controller, Button.kB.value);
  //private final JoystickButton climberRetract = new JoystickButton(controller, Button.kX.value);
  //private final DPadButton climberStop = new DPadButton(controller, DPadDirection.kDown);

  private final DPadButton startBangBang = new DPadButton(controller, DPadDirection.kRight);
  private final DPadButton stopBangBang = new DPadButton(controller, DPadDirection.kLeft);

  private final JoystickButton magazineOnButton = new JoystickButton(controller, Button.kStart.value);
  private final JoystickButton magazineOffButton = new JoystickButton(controller, Button.kBack.value);
  

  private final SequentialCommandGroup bangBangTest = new RampShooter(shooter, 2500, 3000)
      .andThen(new BangBangShooterTest(shooter, 2500));
                                                            
  private final OnClearedTrigger s1 = new OnClearedTrigger(() -> magazine.readBallSensor(Sensor.S1));
  private final OnClearedTrigger s2 = new OnClearedTrigger(() -> magazine.readBallSensor(Sensor.S2));
  private final OnClearedTrigger s3 = new OnClearedTrigger(() -> magazine.readBallSensor(Sensor.S3));
  private final Trigger s4 = new Trigger(() -> {return magazine.readBallSensor(Sensor.S4);});

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    controller.setDeadzone(0.15);
    driveSystem.setDefaultCommand(joystickDrive);
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

    magazineOnButton.whenPressed(() -> magazine.setOutput(-MagazineVals.DEFAULT_SPEED/2));
    magazineOffButton.whenPressed(() -> magazine.setOutput(0));

    intakeForward.whenPressed(new RunIntake(intake, Direction.FORWARD));
    intakeBackward.whenPressed(new RunIntake(intake, Direction.BACKWARD));
    intakeStop.whenPressed(new RunIntake(intake, Direction.STOP));

    s1.whenActive(new HandleS1(magazine));
    s2.whenActive(() -> magazine.shiftToNextPosition(magazine.getState().INTAKE));
    s3.whenActive(() -> magazine.shiftToNextPosition(magazine.getState().H1));
    s4.whenActive(() -> magazine.shiftToNextPosition(magazine.getState().H2));
    s4.whenInactive(() -> magazine.shiftToNextPosition(magazine.getState().SHOOTER));
    

    

    //climberExtend.whenPressed(new ClimberCommand(climber, Direction.FORWARD));
    //climberRetract.whenPressed(new ClimberCommand(climber, Direction.BACKWARD));
    //climberStop.whenPressed(new ClimberCommand(climber, Direction.STOP));

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

  public SequentialCommandGroup rejectBallTwo() {
    return new SequentialCommandGroup(
      new InstantCommand(() -> magazine.setOutput(0)),
      new RunIntake(intake, Direction.BACKWARD)
      
    );

  }
}
