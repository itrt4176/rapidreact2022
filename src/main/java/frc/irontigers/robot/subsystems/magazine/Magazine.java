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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.irontigers.robot.Constants.MagazineVals.*;
import frc.irontigers.robot.subsystems.magazine.Ball.Position;

public class Magazine extends SubsystemBase {

  private WPI_TalonFX conveyor;

  private LinkedList<Ball> balls;

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

    balls = new LinkedList<>();
    addBall();

    intakeSensor = new DigitalInput(S1);
    hold1Sensor = new DigitalInput(S2);
    hold2Sensor = new DigitalInput(S3);
    shotSensor = new DigitalInput(S4);

    frontGate = new Solenoid(CTREPCM, FRONT_SOLENOID);
    rearGate = new Solenoid(CTREPCM, REAR_SOLENOID);

    colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
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
  public boolean readBallSensor(BallSensor sensor) {
    switch (sensor) {
      case Hold1:
        return !hold1Sensor.get();
      case Hold2:
        return !hold2Sensor.get();
      case Intake:
        return !intakeSensor.get();
      case Shot:
        return !shotSensor.get();
      default:
        return true;
    }
  }

  public Ball addBall() {
    var ball = new Ball();
    balls.add(ball);
    SmartDashboard.putData("Ball[" + (balls.size() - 1) + "]", ball);
    return ball;
  }

  public void removeFirstBall() {
    if (!balls.isEmpty()) {
      balls.removeFirst();
    }
  }

  public void removeLastBall() {
    if (!balls.isEmpty()) {
      balls.removeLast();
    }
  }

  public Ball getBallAt(Position position) {
    for (Ball b : balls) {
      if (b.getPosition() == position) {
        return b;
      }
    }

    return null;
  }
  
  public Ball getBall(int index) {
    return balls.get(index);
  }

  public int getBallCount() {
    return balls.size();
  }

  public void updateBallPositions() {
    updateBallPositions(true);
  }

  public void updateBallPositions(boolean forward) {
    for (Ball b : balls) {
      if (forward) {
        b.forward();
      } else {
        b.back();
      }

      if (b.getPosition() == null) {
        balls.remove(b);
      }
    }
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

  public Color readColor() {
    return colorSensor.getColor();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Rear Sensor", readBallSensor(BallSensor.Hold1));
    SmartDashboard.putBoolean("Front Sensor", readBallSensor(BallSensor.Shot));
  }

  public enum BallGate {
    Front,
    Rear,
    Both
  }

  public enum BallSensor {
    Intake,
    Hold1,
    Hold2,
    Shot
  }
}
