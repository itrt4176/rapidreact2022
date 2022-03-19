package frc.irontigers.robot.utils;

import java.util.HashMap;
import java.util.function.Supplier;

import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public abstract class StateTransitionCommand<E> extends SequentialCommandGroup {
    private HashMap<E, Supplier<Command>> nextMap = new HashMap<>();
    private Supplier<Command> nextCommand;
    private Supplier<E> selector;

    private StringLogEntry transitionLog = new StringLogEntry(DataLogManager.getLog(), "magazine/state/transition");

    public final void addNextState(E input, Supplier<Command> command) {
        nextMap.put(input, command);
    }

    public final void setNextSelector(Supplier<E> Selector) {
        selector = Selector;
    }
    

    @Override
    public void initialize() {
        transitionLog.append(this.getClass().getSimpleName());
        addCommands(new WaitUntilCommand(() -> {
            nextCommand = nextMap.get(selector.get());
            return nextCommand != null;
        }));

        super.initialize();
    }

    @Override
    public boolean isFinished() {
        if (super.isFinished()) {
            nextCommand.get().schedule();
            return true;
        }
        return false;
    }
}
