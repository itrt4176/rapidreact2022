// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems.magazine;

import java.util.EnumMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.util.datalog.BooleanLogEntry;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.IntegerLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;

import static edu.wpi.first.wpilibj.PneumaticsModuleType.CTREPCM;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.irontigers.robot.subsystems.magazine.BallStates.Position;

import static frc.irontigers.robot.Constants.MagazineVals.*;
import static frc.irontigers.robot.subsystems.magazine.BallStates.PositionState.*;

public class Magazine extends SubsystemBase {

  private WPI_TalonFX conveyor;

  private BallStates states;

  private DigitalInput s0;
  private DigitalInput s1;
  private DigitalInput s2;
  private DigitalInput s3;

  private Solenoid frontGate;
  private Solenoid rearGate;

  private ColorSensorV3 colorSensor;
  private EnumMap<Sensor, DigitalInput> sensors;
  private final ColorMatch colorMatcher;
  private Color allianceColor = null;

  private IntegerLogEntry intakeLog;
  private IntegerLogEntry h1Log;
  private IntegerLogEntry h2Log;
  private IntegerLogEntry shooterLog;

  private BooleanLogEntry s0Log;
  private BooleanLogEntry s1Log;
  private BooleanLogEntry s2Log;
  private BooleanLogEntry s3Log;

  private EnumMap<Sensor, BooleanLogEntry> sensorLogs;
  

  /** Creates a new Magazine. */
  public Magazine() {
    conveyor = new WPI_TalonFX(MOTOR_ID);
    conveyor.configVoltageCompSaturation(12.0, 30);
    conveyor.enableVoltageCompensation(true);

    s0 = new DigitalInput(S0);
    s1 = new DigitalInput(S1);
    s2 = new DigitalInput(S2);
    s3 = new DigitalInput(S3);

    sensors = new EnumMap<>(Map.ofEntries(
      Map.entry(Sensor.S0, s0),
      Map.entry(Sensor.S1, s1),
      Map.entry(Sensor.S2, s2),
      Map.entry(Sensor.S3, s3)
    ));

    frontGate = new Solenoid(CTREPCM, FRONT_SOLENOID);
    rearGate = new Solenoid(CTREPCM, REAR_SOLENOID);

    colorSensor = new ColorSensorV3(I2C.Port.kMXP);

    states = new BallStates(EMPTY, EMPTY, RIGHT, EMPTY);
  
    colorMatcher = new ColorMatch();

    colorMatcher.addColorMatch(BLUE_COLOR);
    colorMatcher.addColorMatch(RED_COLOR);

    DataLog log = DataLogManager.getLog();
    intakeLog = new IntegerLogEntry(log, "magazine/state/intake");
    h1Log = new IntegerLogEntry(log, "magazine/state/h1");
    h2Log = new IntegerLogEntry(log, "magazine/state/h2");
    shooterLog = new IntegerLogEntry(log, "magazine/state/shooter");
    updateStateLog();

    s0Log = new BooleanLogEntry(log, "magazine/sensor/s0");
    s1Log = new BooleanLogEntry(log, "magazine/sensor/s1");
    s2Log = new BooleanLogEntry(log, "magazine/sensor/s2");
    s3Log = new BooleanLogEntry(log, "magazine/sensor/s3");

    sensorLogs = new EnumMap<>(Map.ofEntries(
      Map.entry(Sensor.S0, s0Log),
      Map.entry(Sensor.S1, s1Log),
      Map.entry(Sensor.S2, s2Log),
      Map.entry(Sensor.S3, s3Log)
    ));
  }

  public void setOutput(double speed) {
    conveyor.set(speed);
  }

  public double getOutput() {
    return conveyor.get();
  }

  private void readAllianceFromDS() {
    DriverStation.Alliance alliance  = DriverStation.getAlliance();
    
    if (alliance == DriverStation.Alliance.Blue) {
      allianceColor = BLUE_COLOR;
    } else if (alliance == DriverStation.Alliance.Red) {
      allianceColor = RED_COLOR;
    }  
  }

  /**
   * True when sensor is broken (blocked)
   * False when sensor isn't broken
   * 
   * @param sensor
   * @return selected sensor's value
   */
  public boolean readBallSensor(Sensor sensor) {
    boolean reading = !sensors.get(sensor).get();
    sensorLogs.get(sensor).append(reading);

    return reading;
  }

  public BallStates getState() {
    return states;
  }

  /**
   * Shifts state at given position to next position.
   * 
   * Current position become <code>EMPTY</code>.
   * 
   * @param current Position to shift state of
   */
  public void shiftToNextPosition(Position current) {

    if (current.next == null) {
      current.state = EMPTY;
      return;
    }
      
    if (current.next.state == EMPTY) {

        if (current.equals(states.H1) && !rearGate.get()){
          return;
        }

        if (current.equals(states.H2) && frontGate.get()){
          return;
        }
        
        current.next.state = current.state;
        current.state = EMPTY;

        updateStateLog();
      }

  }

  /**
   * Shifts state at given position to next position.
   * 
   * Current position become <code>EMPTY</code>.
   * 
   * @param current Position to shift state of
   */
  public void shiftToPreviousPosition(Position current) {

    if (current.previous == null) {
      current.state = EMPTY;
      return;
    }
    
    if (current.previous.state == EMPTY) {

      if (current.equals(states.H2) && !rearGate.get()){
        return;
      }

      if (current.equals(states.SHOOTER)){
        return;
      }

      current.previous.state = current.state;
      current.state = EMPTY;

      updateStateLog();
    }
  }

  public void addBall() {
    states.INTAKE.state = UNKNOWN;
    updateStateLog();
  }

  public void openGate(BallGate gate) {
    switch (gate) {
      case Both:
        rearGate.set(true);
      case Front:
        frontGate.set(false);
        break;
      case Rear:
        rearGate.set(true);
        break;
    }
  }

  public void closeGate(BallGate gate) {
    switch (gate) {
      case Both:
        // rearGate.set(false);
      case Front:
        frontGate.set(true);
        break;
      case Rear:
        // rearGate.set(false);
        break;
    }
  }

  public void readBallColor() {
    if (allianceColor == null) {
      readAllianceFromDS();
    }

    Color detectedColor = colorSensor.getColor();
    Color match = colorMatcher.matchClosestColor(detectedColor).color;

    states.INTAKE.state = match == allianceColor ? RIGHT : WRONG;
    updateStateLog();
  }

  private void updateStateLog() {
    intakeLog.append(states.INTAKE.state.ordinal());
    h1Log.append(states.H1.state.ordinal());
    h2Log.append(states.H2.state.ordinal());
    shooterLog.append(states.SHOOTER.state.ordinal());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // SmartDashboard.putBoolean("Rear Sensor", readBallSensor(BallPosition.Hold1));
    // SmartDashboard.putBoolean("Front Sensor", readBallSensor(BallPosition.Shot));
    // Color color = colorSensor.getColor();
    // SmartDashboard.putNumberArray("RGB", new double[] { color.red, color.green, color.blue });

    SmartDashboard.putBoolean("Intake Unknown", states.INTAKE.state == UNKNOWN);
    SmartDashboard.putBoolean("Intake Right", states.INTAKE.state == RIGHT);
    SmartDashboard.putBoolean("Intake Wrong", states.INTAKE.state == WRONG);

    SmartDashboard.putBoolean("H1 Unknown", states.H1.state == UNKNOWN);
    SmartDashboard.putBoolean("H1 Right", states.H1.state == RIGHT);
    SmartDashboard.putBoolean("H1 Wrong", states.H1.state == WRONG);

    SmartDashboard.putBoolean("H2 Unknown", states.H2.state == UNKNOWN);
    SmartDashboard.putBoolean("H2 Right", states.H2.state == RIGHT);
    SmartDashboard.putBoolean("H2 Wrong", states.H2.state == WRONG);

    SmartDashboard.putBoolean("Shooter Unknown", states.SHOOTER.state == UNKNOWN);
    SmartDashboard.putBoolean("Shooter Right", states.SHOOTER.state == RIGHT);
    SmartDashboard.putBoolean("Shooter Wrong", states.SHOOTER.state == WRONG);

    SmartDashboard.putBoolean("INTAKE STATE", states.INTAKE.state != EMPTY);
    SmartDashboard.putBoolean("H1 STATE", states.H1.state != EMPTY);
    SmartDashboard.putBoolean("H2 STATE", states.H2.state != EMPTY);
    SmartDashboard.putBoolean("SHOOTER STATE", states.SHOOTER.state != EMPTY);

    SmartDashboard.putString("BallState", states.toString());
  }

  public enum BallGate {
    Front,
    Rear,
    Both
  }

  public enum Sensor {
    S0,
    S1,
    S2,
    S3
  }
}
