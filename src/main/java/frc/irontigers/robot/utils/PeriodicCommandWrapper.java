package frc.irontigers.robot.utils;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class PeriodicCommandWrapper extends CommandBase {
    protected Notifier notifier;
    protected double period;
    protected Command wrappedCommand;

    public PeriodicCommandWrapper(Command command, double period) {
        wrappedCommand = command;
        notifier = new Notifier(wrappedCommand::execute);
        notifier.setName(wrappedCommand.getName() + " (" + period + " s notifier)");
        // notifier.setHALThreadPriority(true, 85);
        this.period = period;

        for (Subsystem subsystem : wrappedCommand.getRequirements()) {
            addRequirements(subsystem);
        }

        setName(wrappedCommand.getName());
    }

    @Override
    public void initialize() {
        wrappedCommand.initialize();
        notifier.startPeriodic(period);
    }

    @Override
    public void end(boolean interrupted) {
        notifier.stop();
        wrappedCommand.end(interrupted);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public boolean isFinished() {
        return wrappedCommand.isFinished();
    }
}
