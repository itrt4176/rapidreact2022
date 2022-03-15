package frc.irontigers.robot.commands.triggers;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.irontigers.robot.subsystems.magazine.BallStates;
import frc.irontigers.robot.subsystems.magazine.Magazine;

import static frc.irontigers.robot.subsystems.magazine.BallStates.PositionState.*;

public class ShootableState extends Trigger {
    private ArrayList<BallStates> shootableStates;
    private Magazine magazine;

    public ShootableState(Magazine magazine) {
        this.magazine = magazine;

        shootableStates = new ArrayList<>();
        shootableStates.add(new BallStates(EMPTY, RIGHT, EMPTY, EMPTY));
        shootableStates.add(new BallStates(EMPTY, EMPTY, RIGHT, EMPTY));
        shootableStates.add(new BallStates(EMPTY, RIGHT, RIGHT, EMPTY));
    }

    @Override
    public boolean getAsBoolean() {
        return shootableStates.contains(magazine.getState());
    }
}
