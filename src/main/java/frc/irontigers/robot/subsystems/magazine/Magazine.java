// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems.magazine;

import java.util.LinkedList;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.irontigers.robot.Constants.MagazineVals;
import frc.irontigers.robot.subsystems.magazine.Ball.Position;

public class Magazine extends SubsystemBase {
  /** Creates a new InFeed. */

  private WPI_TalonFX conveyor;
  private DigitalInput sensor;

  private LinkedList<Ball> balls = new LinkedList<>();
  
  public Magazine() {
    conveyor = new WPI_TalonFX(MagazineVals.MOTOR_ID);
    sensor = new DigitalInput(0);
  }

  public void set(double speed) {
    conveyor.set(speed);
  }

  public double get() {
    return conveyor.get();
  }

  public boolean beamBroken() {
    return sensor.get();
  }

  public void addBall() {
    Ball b = new Ball();
    SendableRegistry.addChild(this, b);
    balls.add(b);
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

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.getBoolean("Beam Broken", beamBroken());
  }
}
