package frc.irontigers.robot.utils;

import java.util.HashMap;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.irontigers.robot.subsystems.magazine.BallsState;

public abstract class StateTransitionCommand<E> extends SequentialCommandGroup {
    private HashMap<E, Command> nextMap = new HashMap<>();
    private Command nextCommand;
    private Supplier<E> selector;

    public final void addNextState(E input, Command command) {
        nextMap.put(input, command);
    }

    public final void setNextSelector(Supplier<E> Selector) {
        selector = Selector;
    }
    

    @Override
    public void initialize() {
        addCommands(new WaitUntilCommand(() -> {
            nextCommand = nextMap.get(selector.get());
            return nextCommand != null;
        }));

        super.initialize();
    }

    @Override
    public boolean isFinished() {
        if (super.isFinished()) {
            nextCommand.schedule();
            return true;
        }
        return false;
    }
}
