// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems;

import frc.irontigers.robot.Constants;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;
import frc.tigerlib.subsystem.drive.MecanumDriveSubsystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveOdometry;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.datalog.IntegerLogEntry;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

public class DriveSystem extends MecanumDriveSubsystem {
  /** Creates a new DriveSystem. */

  private WPI_TalonFX leftFront;
  private WPI_TalonFX leftBack;
  private WPI_TalonFX rightFront;
  private WPI_TalonFX rightBack;
  private int direction = 1; //-1 or 1
  private int gear = 3; 
  private double gearScalar;


  private AHRS gyro;

  private MecanumDriveKinematics kinematics;

  private IntegerLogEntry gearLog;
  private DoubleLogEntry gearScalarLog;
  private IntegerLogEntry directionLog;

  private DoubleLogEntry odoXLog;
  private DoubleLogEntry odoYLog;
  private DoubleLogEntry odoRotLog;

  public DriveSystem() {
    leftFront = new WPI_TalonFX(Constants.DriveSystemVals.FRONT_LEFT);
    leftBack = new WPI_TalonFX(Constants.DriveSystemVals.BACK_LEFT);
    rightFront = new WPI_TalonFX(Constants.DriveSystemVals.FRONT_RIGHT);
    rightBack = new WPI_TalonFX(Constants.DriveSystemVals.BACK_RIGHT);

    gyro = new AHRS();
    kinematics = new MecanumDriveKinematics(
      new Translation2d(Units.inchesToMeters(-23.5 / 2.0), Units.inchesToMeters(20.25 / 2.0)),
      new Translation2d(Units.inchesToMeters(23.5 / 2.0), Units.inchesToMeters(20.25 / 2.0)),
      new Translation2d(Units.inchesToMeters(-23.5 / 2.0), Units.inchesToMeters(-20.25 / 2.0)),
      new Translation2d(Units.inchesToMeters(23.5 / 2.0), Units.inchesToMeters(-20.25 / 2.0))
    );

    setGyro(gyro);
    setMotors(leftFront, leftBack, rightFront, rightBack, kinematics);

    DataLog log = DataLogManager.getLog();
    gearLog = new IntegerLogEntry(log, "drive/gear");
    gearScalarLog = new DoubleLogEntry(log, "drive/gearScalar");
    directionLog = new IntegerLogEntry(log, "drive/direction");

    odoXLog = new DoubleLogEntry(log, "drive/odometer/x");
    odoYLog = new DoubleLogEntry(log, "drive/odometer/y");
    odoRotLog = new DoubleLogEntry(log, "drive/odometer/rotation");
  }

  public MecanumDriveOdometry getOdometer() {
    return odometer;
  }
  

  public double motorToWheelSpeed(TalonFX motor) {
    return (motor.getSelectedSensorVelocity()*600/2048)/10.71;
  }

  public double getLeftFrontDistance(){
    return leftFront.getSelectedSensorPosition();
  }

  public double getLeftBackDistance(){
    return leftBack.getSelectedSensorPosition();
  }

  public double getRightFrontPosition(){
    return rightFront.getSelectedSensorPosition();
  }

  public double getRightBackPosition(){
    return rightBack.getSelectedSensorPosition();
  }

  

  @Override
  public void drive(double xSpeed, double ySpeed, double rotation) {
      switch(gear){
        case 0:
         gearScalar = .25;
         break;
        case 1:
         gearScalar = .5;
         break;
        case 2:
          gearScalar = .75;
          break;
        case 3:
          gearScalar = 1;
          break;
      }

      super.drive(direction * gearScalar * ySpeed, direction * gearScalar * xSpeed, gearScalar * rotation);

      gearScalarLog.append(gearScalar);
  }
  public void setFrontToIntake(){
    direction = -1;
    directionLog.append(direction);
  }
  public void setFrontToShooter(){
    direction = 1;
    directionLog.append(direction);
  }
  public void shiftUp(){
    if (gear < 3) {
      gear++;
    }
    
    gearLog.append(gear);
  }
  public void shiftDown(){
    if (gear > 0) {
      gear--;
    }
    
    gearLog.append(gear);
  }

  public boolean isDriveDirectionTowardsShooter(){
    if(direction == 1){
      return true;
    }else{
      return false;
    }
  }
  public void toggleDriveFront(){
    if(isDriveDirectionTowardsShooter()  == true){
      setFrontToIntake();
    } else if(isDriveDirectionTowardsShooter() == false){
      setFrontToShooter();

    }
  }
  
  

  @Override
  public void periodic() {
    super.periodic();
    // This method will be called once per scheduler run
    super.periodic();
    Pose2d pos = getRobotPosition();
    odoXLog.append(pos.getX());
    odoYLog.append(pos.getY());
    odoRotLog.append(pos.getRotation().getDegrees());
  }

  @Override
  protected MecanumDriveWheelSpeeds getWheelSpeeds() {
    // TODO Auto-generated method stub
    return new MecanumDriveWheelSpeeds(
      (leftFront.getSelectedSensorVelocity() * 10.0 * Units.inchesToMeters(6 * Math.PI)) / (2048.0 * 10.71),
      (rightFront.getSelectedSensorVelocity() * 10.0 * Units.inchesToMeters(6 * Math.PI)) / (2048.0 * 10.71),
      (leftBack.getSelectedSensorVelocity() * 10.0 * Units.inchesToMeters(6 * Math.PI)) / (2048.0 * 10.71),
      (rightBack.getSelectedSensorVelocity() * 10.0 * Units.inchesToMeters(6 * Math.PI)) / (2048.0 * 10.71)
    );
  
  }

 
  public void resetEncoders() {
    leftFront.setSelectedSensorPosition(0);
    leftFront.setSelectedSensorPosition(0);
    rightFront.setSelectedSensorPosition(0);
    rightBack.setSelectedSensorPosition(0);
  }
  
  
}
