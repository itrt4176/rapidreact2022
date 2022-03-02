package frc.irontigers.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.BallsState;

import static java.util.Map.ofEntries;

import static java.util.Map.entry;
import static frc.irontigers.robot.subsystems.magazine.BallsState.PositionState.*;

public class HandleS1 extends SelectCommand {
    
    public HandleS1(Magazine magazine) {
        super(
            ofEntries(
                entry(new BallsState(), new InstantCommand(magazine::addBall)),
                
                entry(new BallsState(EMPTY, RIGHT, EMPTY, EMPTY), new InstantCommand(magazine::addBall)),
                
                entry(new BallsState(EMPTY, EMPTY, RIGHT, EMPTY), new InstantCommand(magazine::addBall)),
        
                entry(new BallsState(EMPTY, WRONG, EMPTY, EMPTY), new InstantCommand(magazine::addBall)),

                entry(new BallsState(EMPTY, EMPTY, WRONG, EMPTY), new InstantCommand(magazine::addBall)),
                                
                entry(new BallsState(WRONG, RIGHT, EMPTY, EMPTY),
                        new InstantCommand(() -> magazine.shiftToPreviousPosition(
                                magazine.getState().INTAKE))),
                                
                entry(new BallsState(WRONG, EMPTY, RIGHT, EMPTY),
                        new InstantCommand(() -> magazine.shiftToPreviousPosition(
                                magazine.getState().INTAKE)))
            ),
            magazine::getState
        );
    }
}
