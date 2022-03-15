// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot;

import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.GenericHID;
import frc.tigerlib.XboxControllerIT;
import frc.tigerlib.XboxControllerIT.DPadDirection;
import frc.tigerlib.command.MecanumJoystickDrive;
import frc.tigerlib.command.button.DPadButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.XboxController.Button;
import static frc.irontigers.robot.Constants.*;

import org.photonvision.PhotonCamera;

import frc.irontigers.robot.commands.RunShooter;
import frc.irontigers.robot.commands.Shoot;
import frc.irontigers.robot.commands.RampShooter;
import frc.irontigers.robot.commands.RunIntake;
import frc.irontigers.robot.commands.ballstate.IntakeBallOne;
import frc.irontigers.robot.subsystems.DriveSystem;
import frc.irontigers.robot.subsystems.Intake;
import frc.irontigers.robot.subsystems.Shooter;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.BallStates.PositionState;
import frc.irontigers.robot.subsystems.magazine.Magazine.BallGate;
import frc.irontigers.robot.subsystems.magazine.Magazine.Sensor;
import frc.irontigers.robot.utils.PeriodicCommandWrapper;

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
  private final PhotonCamera camera = new PhotonCamera("limelight");


  private final XboxControllerIT controller = new XboxControllerIT(0);

  private final DriveSystem driveSystem = new DriveSystem();
  private final MecanumJoystickDrive joystickDrive = new MecanumJoystickDrive(driveSystem, controller);  

  private final JoystickButton shooterOnButton = new JoystickButton(controller, Button.kRightBumper.value);
  private final JoystickButton shooterOffButton = new JoystickButton(controller, Button.kLeftBumper.value);

  private final JoystickButton intakeForward = new JoystickButton(controller, Button.kY.value);
  // private final JoystickButton intakeBackward = new JoystickButton(controller, Button.kA.value);
  private final JoystickButton intakeStop = new JoystickButton(controller, Button.kBack.value);

  private final JoystickButton openFrontGateButton = new JoystickButton(controller, Button.kX.value);
  private final JoystickButton closeFrontGateButton = new JoystickButton(controller, Button.kA.value);

  private final DPadButton openRearGateButton = new DPadButton(controller, DPadDirection.kUp);
  private final DPadButton closeRearGateButton = new DPadButton(controller, DPadDirection.kDown);

  //private final JoystickButton climberExtend = new JoystickButton(controller, Button.kB.value);
  //private final JoystickButton climberRetract = new JoystickButton(controller, Button.kX.value);
  //private final DPadButton climberStop = new DPadButton(controller, DPadDirection.kDown);

  private final DPadButton increaseBangBang = new DPadButton(controller, DPadDirection.kRight);
  private final DPadButton decreaseBangBang = new DPadButton(controller, DPadDirection.kLeft);

  private final JoystickButton magazineOnButton = new JoystickButton(controller, Button.kStart.value);
  private final JoystickButton magazineOffButton = new JoystickButton(controller, Button.kBack.value);
  
  private final Shoot runShooter = new Shoot(intake, magazine, shooter, camera);

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

    // magazine.closeGate(BallGate.Rear);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxControllerIT}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    shooterOnButton.whenPressed(runShooter);
    shooterOffButton.cancelWhenPressed(runShooter);

    magazineOnButton.whenPressed(() -> magazine.setOutput(MagazineVals.DEFAULT_SPEED));
    magazineOffButton.whenPressed(() -> magazine.setOutput(0));

    intakeForward.whenPressed(new RunIntake(intake, Direction.FORWARD));
    // intakeBackward.whenPressed(new RunIntake(intake, Direction.BACKWARD));
    intakeStop.whenPressed(new RunIntake(intake, Direction.STOP));

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
    
    
    openFrontGateButton.whenPressed(() -> magazine.openGate(BallGate.Front));
    closeFrontGateButton.whenPressed(() -> magazine.closeGate(BallGate.Front));

    openRearGateButton.whenPressed(() -> magazine.openGate(BallGate.Rear));
    closeRearGateButton.whenPressed(() -> magazine.closeGate(BallGate.Rear));

    //climberExtend.whenPressed(new ClimberCommand(climber, Direction.FORWARD));
    //climberRetract.whenPressed(new ClimberCommand(climber, Direction.BACKWARD));
    //climberStop.whenPressed(new ClimberCommand(climber, Direction.STOP));

    increaseBangBang.whenPressed(runShooter::increaseSpeed);
    decreaseBangBang.whenPressed(runShooter::decreaseSpeed);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new IntakeBallOne(shooter, magazine, intake);
  }
}

