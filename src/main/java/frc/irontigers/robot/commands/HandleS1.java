package frc.irontigers.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import frc.irontigers.robot.subsystems.magazine.Magazine;
import frc.irontigers.robot.subsystems.magazine.BallStates;

import static java.util.Map.ofEntries;

import static java.util.Map.entry;
import static frc.irontigers.robot.subsystems.magazine.BallStates.PositionState.*;

public class HandleS1 extends SelectCommand {
    
    public HandleS1(Magazine magazine) {
        super(
            ofEntries(
                entry(new BallStates(), new InstantCommand(magazine::addBall)),
                
                entry(new BallStates(EMPTY, RIGHT, EMPTY, EMPTY), new InstantCommand(magazine::addBall)),
                
                entry(new BallStates(EMPTY, EMPTY, RIGHT, EMPTY), new InstantCommand(magazine::addBall)),
        
                entry(new BallStates(EMPTY, WRONG, EMPTY, EMPTY), new InstantCommand(magazine::addBall)),

                entry(new BallStates(EMPTY, EMPTY, WRONG, EMPTY), new InstantCommand(magazine::addBall)),
                                
                entry(new BallStates(WRONG, RIGHT, EMPTY, EMPTY),
                        new InstantCommand(() -> magazine.shiftToPreviousPosition(
                                magazine.getState().INTAKE))),
                                
                entry(new BallStates(WRONG, EMPTY, RIGHT, EMPTY),
                        new InstantCommand(() -> magazine.shiftToPreviousPosition(
                                magazine.getState().INTAKE)))
            ),
            magazine::getState
        );
    }
}
