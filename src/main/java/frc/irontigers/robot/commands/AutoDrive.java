// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.irontigers.robot.subsystems.DriveSystem;

public class AutoDrive extends CommandBase {
  /** Creates a new AutoDrive. */
  private DriveSystem driveSystem;
  private boolean finished;
  public AutoDrive(DriveSystem driveSystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.driveSystem = driveSystem;
    finished = false;
    addRequirements(this.driveSystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    while (driveSystem.getOdometer().getPoseMeters().getX() <= 5) {
      driveSystem.drive(0.25, 0, 0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSystem.drive(0, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finished;
  }
}
