// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.DriveSystem;

public class AutoDrive extends CommandBase {
  /** Creates a new AutoDrive. */
  private DriveSystem driveSystem;
  private Pose2d currentPos;
  private Pose2d destination;
  public AutoDrive(DriveSystem driveSystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.driveSystem = driveSystem;
    addRequirements(this.driveSystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    currentPos = driveSystem.getRobotPosition();
    destination = currentPos.plus(new Transform2d(currentPos, new Pose2d(-2.0, 1.0, new Rotation2d())));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentPos = driveSystem.getRobotPosition();
    driveSystem.drive(0, -0.25, 0);
    // SmartDashboard.putNumber("Coordinate change", driveSystem.getRobotPosition().getY()
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSystem.drive(0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return currentPos.getX() <= Math.abs(destination.getX());
  }
}
