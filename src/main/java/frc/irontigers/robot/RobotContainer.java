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
import frc.irontigers.robot.commands.RunShooter.ShotResult;

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
  , kLeft};

  private final Shooter shooter = new Shooter();
  private final Intake intake = new Intake();
  private final Magazine magazine = new Magazine();
  //private final Climber climber = new Climber();
  private final PhotonCamera camera = new PhotonCamera("limelight");
  private boolean cancel = false;

  private final Climber climber = new Climber();

  private final XboxControllerIT smartController = new XboxControllerIT(2);
  private final XboxControllerIT overrideController = new XboxControllerIT(3);

  private final XboxControllerIT manualController = new XboxControllerIT(0);
  private final XboxControllerIT shotAdjustController = new XboxControllerIT(1);

  private final DriveSystem driveSystem = new DriveSystem();

  private final MecanumJoystickDrive joystickDrive = new MecanumJoystickDrive(driveSystem, manualController);

  private final Trigger shooterButton = /* new ShootableState(magazine).and( */new JoystickButton(smartController, Button.kA.value);//);
  
  private final DPadButton climberExtendToHeight = new DPadButton(smartController, DPadDirection.kUp);
  private final DPadButton climberRetractFull = new DPadButton(smartController, DPadDirection.kDown);
  
  private final Shoot runShooter = new Shoot(intake, magazine, shooter, camera);
  
  private final JoystickButton gearShiftUp = new JoystickButton(manualController, Button.kRightBumper.value);
  private final JoystickButton gearShiftDown = new JoystickButton(manualController, Button.kLeftBumper.value);

  private final JoystickButton toggleDriveDirection = new JoystickButton(smartController, Button.kB.value);

  private final ManualClimberAdjustment manualclimber = new ManualClimberAdjustment(climber, manualController);

  // private final RunShooter shoot = new RunShooter(shooter, magazine, camera);
  private final Shoot shoot = new Shoot(intake, magazine, shooter, camera);

  

  // private final SequentialCommandGroup rampShooter = runShooter
  //     .beforeStarting(() -> magazine.openGate(BallGate.Both));
  
  // private final PeriodicCommandWrapper betterShoot = new PeriodicCommandWrapper(rampShooter, 0.001);

  private final Trigger s0Override = new JoystickButton(overrideController, Button.kA.value);
  private final Trigger s1Override = new JoystickButton(overrideController, Button.kB.value).negate();
  private final Trigger s2Override = new JoystickButton(overrideController, Button.kY.value);
  private final Trigger s3Override = new JoystickButton(overrideController, Button.kX.value);

  private final JoystickButton intakeForward = new JoystickButton(manualController, Button.kStart.value);
  private final JoystickButton intakeBackward = new JoystickButton(overrideController, Button.kX.value);
  private final JoystickButton intakeOff = new JoystickButton(manualController, Button.kBack.value);

  private final JoystickButton magazineOn = new JoystickButton(manualController, Button.kX.value);
  private final JoystickButton magazineOff = new JoystickButton(manualController, Button.kY.value);

  private final DPadButton m_climberExtend = new DPadButton(manualController, DPadDirection.kUp);
  private final DPadButton m_climberRetract = new DPadButton(manualController, DPadDirection.kDown);
  
  private final JoystickButton m_runShooter = new JoystickButton(manualController, Button.kA.value);
  private final JoystickButton stopShooter = new JoystickButton(manualController, Button.kB.value);

  private final DPadButton overshot = new DPadButton(shotAdjustController, DPadDirection.kUp);
  private final DPadButton undershot = new DPadButton(shotAdjustController, DPadDirection.kDown);
  private final JoystickButton madeIt = new JoystickButton(shotAdjustController, Button.kA.value);
  private final DPadButton increaseSpeed = new DPadButton(shotAdjustController, DPadDirection.kRight);
  private final DPadButton decreaseSpeed = new DPadButton(shotAdjustController, DPadDirection.kLeft);
                                                            
  // private final Trigger s0 = new Trigger(() -> magazine.readBallSensor(Sensor.S0)).debounce(0.04, DebounceType.kBoth);
  private final Trigger s1 = new Trigger(() -> magazine.readBallSensor(Sensor.S1)).debounce(0.04, DebounceType.kBoth).or(s1Override);
  private final Trigger s2 = new Trigger(() -> magazine.readBallSensor(Sensor.S2)).debounce(0.08, DebounceType.kBoth).or(s2Override);
  private final Trigger s3 = new Trigger(() -> magazine.readBallSensor(Sensor.S3)).debounce(0.04, DebounceType.kBoth).or(s3Override);

  private final Trigger s0 = new Trigger(() -> magazine.readBallSensor(Sensor.S0)).debounce(0.04, DebounceType.kBoth)
      .negate().and(s1).or(s0Override);

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    smartController.setDeadzone(0.05);
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
        new InstantCommand((magazine::addBall), magazine), 
        new InstantCommand(() -> magazine.shiftToPreviousPosition(magazine.getState().INTAKE), magazine),
        () -> magazine.getState().INTAKE.getState() == PositionState.EMPTY
    ));
    s1.whenInactive(() -> {if (intake.get() == 0) { magazine.shiftToNextPosition(magazine.getState().INTAKE); }}, intake);
    s2.whenInactive(() -> magazine.shiftToNextPosition(magazine.getState().H1), magazine);
    s3.whenActive(() -> magazine.shiftToNextPosition(magazine.getState().H2), magazine);
    s3.whenInactive(() -> magazine.shiftToNextPosition(magazine.getState().SHOOTER), magazine);

    // s0Override.whenActive(new ConditionalCommand(
    //     new InstantCommand((magazine::addBall)),
    //     new InstantCommand(() -> magazine.shiftToPreviousPosition(magazine.getState().INTAKE)),
    //     () -> magazine.getState().INTAKE.getState() == PositionState.EMPTY));
    // s1Override.whenInactive(() -> {
    //   if (intake.get() == 0) {
    //     magazine.shiftToNextPosition(magazine.getState().INTAKE);
    //   }
    // });
    // s2Override.whenInactive(() -> magazine.shiftToNextPosition(magazine.getState().H1));
    // s3Override.whenActive(() -> magazine.shiftToNextPosition(magazine.getState().H2));
    // s3Override.whenInactive(() -> magazine.shiftToNextPosition(magazine.getState().SHOOTER));
    
    climberExtendToHeight.whenPressed(new ClimberCommand(climber, Direction.BACKWARD)); 
    climberRetractFull.whenPressed(new ClimberCommand(climber, Direction.FORWARD));

    gearShiftUp.whenPressed(() -> driveSystem.shiftUp());
    gearShiftDown.whenPressed(() -> driveSystem.shiftDown());

    toggleDriveDirection.whenPressed(() -> driveSystem.toggleDriveFront());

    intakeForward.whenPressed(new RunIntake(intake, Direction.FORWARD));
    intakeBackward.whenPressed(new RunIntake(intake, Direction.BACKWARD));
    intakeOff.whenPressed(new RunIntake(intake, Direction.STOP));

    magazineOn.whenPressed(new InstantCommand(() -> magazine.setOutput(MagazineVals.DEFAULT_SPEED), magazine));
    magazineOff.whenPressed(new InstantCommand(() -> magazine.setOutput(0), magazine));

    m_climberExtend.whenPressed(new ClimberCommand(climber, Direction.BACKWARD)); 
    m_climberRetract.whenPressed(new ClimberCommand(climber, Direction.FORWARD));

    m_runShooter.whenPressed(shoot);
    stopShooter.cancelWhenPressed(shoot);

    overshot.whenPressed(() -> shoot.adjustDistanceMap(ShotResult.OVERSHOT));
    undershot.whenPressed(() -> shoot.adjustDistanceMap(ShotResult.UNDERSHOT));
    madeIt.whenPressed(() -> shoot.adjustDistanceMap(ShotResult.SCORE));
    // increaseSpeed.whenPressed(shoot::increaseSpeed);
    // decreaseSpeed.whenPressed(shoot::decreaseSpeed);
    
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
        new AutoDrive(driveSystem),//.withTimeout(4),
        // new Shoot(intake, magazine, shooter, camera)  Cannot see the target, calibration not 100%
        new InstantCommand(() -> shooter.set(.85), shooter),  // Short term solution for auto
        new WaitCommand(3),
        new InstantCommand(() -> magazine.setOutput(.5), magazine)
      );
  }


  
  PhotonCamera getCamera() {
    return camera;
  }
}

