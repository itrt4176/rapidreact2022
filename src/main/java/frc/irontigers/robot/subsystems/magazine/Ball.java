// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.subsystems.magazine;

/** Add your docs here. */
public class Ball {
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
        Unknown
    }
}
