// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot;

import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PS4Controller.Axis;
import frc.tigerlib.XboxControllerIT;
import frc.tigerlib.XboxControllerIT.DPadDirection;
import frc.tigerlib.command.MecanumJoystickDrive;
import frc.tigerlib.command.button.DPadButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.XboxController.Button;
import static frc.irontigers.robot.Constants.*;

import org.photonvision.PhotonCamera;

import frc.irontigers.robot.commands.RunShooter;
import frc.irontigers.robot.commands.Shoot;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

import frc.irontigers.robot.commands.AutoDrive;
import frc.irontigers.robot.commands.RampShooter;
import frc.irontigers.robot.commands.RunIntake;
import frc.irontigers.robot.commands.Shoot;
import frc.irontigers.robot.commands.ballstate.AdvanceBallOne;
import frc.irontigers.robot.commands.ballstate.IntakeBallOne;
import frc.irontigers.robot.commands.triggers.ShootableState;
import frc.irontigers.robot.commands.ClimberCommand;
import frc.irontigers.robot.commands.ManualClimberAdjustment;
import frc.irontigers.robot.subsystems.Climber;
import frc.irontigers.robot.subsystems.DriveSystem;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.magazine.BallStates;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.BallStates.PositionState;
import frc.irontigers.robot.subsystems.magazine.Magazine.BallGate;
import frc.irontigers.robot.subsystems.magazine.Magazine.Sensor;
import frc.irontigers.robot.utils.PeriodicCommandWrapper;
import frc.irontigers.robot.utils.StateTransitionCommand;

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
  //Intske Direction
  public enum Direction {
    FORWARD,
    BACKWARD,
    STOP
  };

  private final Shooter shooter = new Shooter();
  private final Intake intake = new Intake();
  private final Magazine magazine = new Magazine();
  //private final Climber climber = new Climber();
  private final PhotonCamera camera = new PhotonCamera("limelight");

  private final Climber climber = new Climber();

  private final XboxControllerIT controller = new XboxControllerIT(0);

  private final DriveSystem driveSystem = new DriveSystem();
  private final MecanumJoystickDrive joystickDrive = new MecanumJoystickDrive(driveSystem, controller);  

  private final Trigger shooterButton = new ShootableState(magazine).and(new JoystickButton(controller, Button.kA.value));
  
  private final DPadButton climberExtendToHeight = new DPadButton(controller, DPadDirection.kUp);
  private final DPadButton climberRetractFull = new DPadButton(controller, DPadDirection.kDown);
  
  private final Shoot runShooter = new Shoot(intake, magazine, shooter, camera);
  
  private final JoystickButton gearShiftUp = new JoystickButton(controller, Button.kRightBumper.value);
  private final JoystickButton gearShiftDown = new JoystickButton(controller, Button.kLeftBumper.value);

  private final JoystickButton toggleDriveDirection = new JoystickButton(controller, Button.kB.value);

  private final ManualClimberAdjustment manualclimber = new ManualClimberAdjustment(climber, controller);

  // private final SequentialCommandGroup rampShooter = runShooter
  //     .beforeStarting(() -> magazine.openGate(BallGate.Both));
  
  // private final PeriodicCommandWrapper betterShoot = new PeriodicCommandWrapper(rampShooter, 0.001);
                                                            
  // private final Trigger s0 = new Trigger(() -> magazine.readBallSensor(Sensor.S0)).debounce(0.04, DebounceType.kBoth);
  private final Trigger s1 = new Trigger(() -> magazine.readBallSensor(Sensor.S1)).debounce(0.04, DebounceType.kBoth);
  private final Trigger s2 = new Trigger(() -> magazine.readBallSensor(Sensor.S2)).debounce(0.08, DebounceType.kBoth);
  private final Trigger s3 = new Trigger(() -> magazine.readBallSensor(Sensor.S3)).debounce(0.04, DebounceType.kBoth);

  private final Trigger s0 = new Trigger(() -> magazine.readBallSensor(Sensor.S0)).debounce(0.04, DebounceType.kBoth).negate().and(s1);

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    controller.setDeadzone(0.05);
    driveSystem.setDefaultCommand(joystickDrive);
    climber.setDefaultCommand(manualclimber);

    // magazine.closeGate(BallGate.Rear);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxControllerIT}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    
    shooterButton.whenActive(new Shoot(intake, magazine, shooter, camera));

    // s0.whenInactive(new HandleS1(magazine));
    s0.whenActive(new ConditionalCommand(
        new InstantCommand((magazine::addBall)), 
        new InstantCommand(() -> magazine.shiftToPreviousPosition(magazine.getState().INTAKE)),
        () -> magazine.getState().INTAKE.getState() == PositionState.EMPTY
    ));
    s1.whenInactive(() -> {if (intake.get() == 0) { magazine.shiftToNextPosition(magazine.getState().INTAKE); }});
    s2.whenInactive(() -> magazine.shiftToNextPosition(magazine.getState().H1));
    s3.whenActive(() -> magazine.shiftToNextPosition(magazine.getState().H2));
    s3.whenInactive(() -> magazine.shiftToNextPosition(magazine.getState().SHOOTER));
    
    climberExtendToHeight.whenPressed(new ClimberCommand(climber, Direction.BACKWARD)); //probably will not work?
    climberRetractFull.whenPressed(new ClimberCommand(climber, Direction.FORWARD));

    gearShiftUp.whenPressed(() -> driveSystem.shiftUp());
    gearShiftDown.whenPressed(() -> driveSystem.shiftDown());

    toggleDriveDirection.whenPressed(() -> driveSystem.toggleDriveFront());

    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new ParallelCommandGroup(
      new AutoDrive(driveSystem),
      new SequentialCommandGroup(
        new InstantCommand(() -> intake.deploy()), 
        new WaitCommand(2), // To make sure that the intake is actually deployed before the next scheduler call 
        new Shoot(intake, magazine, shooter, camera)
      )
    );
  }
}

