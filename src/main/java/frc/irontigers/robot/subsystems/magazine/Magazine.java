// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems.magazine;

import java.util.LinkedList;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;

import static edu.wpi.first.wpilibj.PneumaticsModuleType.CTREPCM;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SerialPort.WriteBufferMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.irontigers.robot.subsystems.magazine.BallStates.Position;

import static frc.irontigers.robot.Constants.MagazineVals.*;
import static frc.irontigers.robot.subsystems.magazine.BallStates.PositionState.*;

public class Magazine extends SubsystemBase {

  private WPI_TalonFX conveyor;

  private BallStates states;

  private DigitalInput intakeSensor;
  private DigitalInput hold1Sensor;
  private DigitalInput hold2Sensor;
  private DigitalInput shotSensor;

  private Solenoid frontGate;
  private Solenoid rearGate;

  private ColorSensorV3 colorSensor;

  /** Creates a new Magazine. */
  public Magazine() {
    conveyor = new WPI_TalonFX(MOTOR_ID);

    intakeSensor = new DigitalInput(S1);
    hold1Sensor = new DigitalInput(S2);
    hold2Sensor = new DigitalInput(S3);
    shotSensor = new DigitalInput(S4);

    frontGate = new Solenoid(CTREPCM, FRONT_SOLENOID);
    rearGate = new Solenoid(CTREPCM, REAR_SOLENOID);

    colorSensor = new ColorSensorV3(I2C.Port.kMXP);

    states = new BallStates();
  }

  public void setOutput(double speed) {
    conveyor.set(speed);
  }

  public double getOutput() {
    return conveyor.get();
  }

  /**
   * True when sensor is broken (blocked)
   * False when sensor isn't broken
   * 
   * @param sensor
   * @return selected sensor's value
   */
  public boolean readBallSensor(Sensor sensor) {
    switch (sensor) {
      case S1:
        return !hold1Sensor.get();
      case S2:
        return !hold2Sensor.get();
      case S3:
        return !intakeSensor.get();
      case S4:
        return !shotSensor.get();
      default:
        return false;
    }
  }

  public BallStates getState() {
    return states;
  }
  
  /**
   * Shift current states forward one position.
   * 
   * First position becomes <code>EMPTY</code>, last state is dropped.
   */
  public void shiftPositionStates() {
    states.SHOOTER.state = states.H2.state;
    states.H2.state = states.H1.state;
    states.H1.state = states.INTAKE.state;
    states.INTAKE.state = EMPTY;
  }

  /**
   * Shifts state at given position to next position.
   * 
   * Current position become <code>EMPTY</code>.
   * 
   * @param current Position to shift state of
   */
  public void shiftToNextPosition(Position current) {
    if (current.next != null) {
      current.next.state = current.state;
    }
    current.state = EMPTY;
  }

  /**
   * Shifts state at given position to next position.
   * 
   * Current position become <code>EMPTY</code>.
   * 
   * @param current Position to shift state of
   */
  public void shiftToPreviousPosition(Position current) {
    if (current.previous != null) {
      current.previous.state = current.state;
    }
    current.state = EMPTY;
  }

  public void addBall() {
    states.INTAKE.state = UNKNOWN;
  }

  public void openGate(BallGate gate) {
    switch (gate) {
      case Both:
        rearGate.set(false);
      case Front:
        frontGate.set(false);
        break;
      case Rear:
        rearGate.set(false);
        break;
    }
  }

  public void closeGate(BallGate gate) {
    switch (gate) {
      case Both:
        rearGate.set(true);
      case Front:
        frontGate.set(true);
        break;
      case Rear:
        rearGate.set(true);
        break;
    }
  }

  public Color readBallColor() {
    return colorSensor.getColor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // SmartDashboard.putBoolean("Rear Sensor", readBallSensor(BallPosition.Hold1));
    // SmartDashboard.putBoolean("Front Sensor", readBallSensor(BallPosition.Shot));
    Color color = readBallColor();
    SmartDashboard.putNumberArray("RGB", new double[] { color.red, color.green, color.blue });

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
  }

  public enum BallGate {
    Front,
    Rear,
    Both
  }

  public enum Sensor {
    S1,
    S2,
    S3,
    S4
  }
}
