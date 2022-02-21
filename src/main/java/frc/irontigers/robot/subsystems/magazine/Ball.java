// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems.magazine;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

/** Add your docs here. */
public class Ball implements Sendable, AutoCloseable {
    private Position position;
    private Color color;

    public Ball() {
        position = Position.Intake;
        color = Color.Unknown;
    }

    public void forward() {
        position = position.getNext();
    }

    public void back() {
        position = position.getNext();
    }

    public Position getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Ball");
        builder.addStringProperty("position", position::name, null);
        builder.addStringProperty("color", color::name, null);
    }

    @Override
    public void close() {
        SendableRegistry.remove(this);
    }

    public enum Position {
        Intake {
            @Override
            public Position getPrevious() {
                return null;
            }

            @Override
            public Position getNext() {
                return BeforeH1;
            }
        },
        BeforeH1 {
            @Override
            public Position getPrevious() {
                return Intake;
            }

            @Override
            public Position getNext() {
                return Hold1;
            }
        },
        Hold1 {
            @Override
            public Position getPrevious() {
                return BeforeH1;
            }

            @Override
            public Position getNext() {
                return BeforeH2;
            }
        },
        BeforeH2 {
            @Override
            public Position getPrevious() {
                return Hold1;
            }

            @Override
            public Position getNext() {
                return Hold2;
            }
        },
        Hold2 {
            @Override
            public Position getPrevious() {
                return BeforeH2;
            }

            @Override
            public Position getNext() {
                return Shooting;
            }
        },
        Shooting {
            @Override
            public Position getPrevious() {
                return Hold2;
            }

            @Override
            public Position getNext() {
                return null;
            }
        };

        public abstract Position getPrevious();

        public abstract Position getNext();
    }

    public enum Color {
        Red,
        Blue,
        Unknown;

        public boolean equals(Alliance a) {
            if (a == Alliance.Invalid) {
                return true;
            }

            return name() == a.name();
        }
    }
}
