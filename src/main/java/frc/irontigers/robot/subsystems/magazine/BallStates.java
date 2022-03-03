package frc.irontigers.robot.subsystems.magazine;

public class BallStates {
    public final Position SHOOTER, H2, H1, INTAKE;

    public BallStates(PositionState intake, PositionState h1, PositionState h2, PositionState shooter) {
        SHOOTER = new Position(shooter);
        H2 = new Position(h2);
        H1 = new Position(h1);
        INTAKE = new Position(intake);

        SHOOTER.previous = H2;
        H2.next = SHOOTER;
        H2.previous = H1;
        H1.next = H2;
        H1.previous = INTAKE;
        INTAKE.next = H1;
    }

    public BallStates() {
        SHOOTER = new Position(PositionState.EMPTY);
        H2 = new Position(PositionState.EMPTY);
        H1 = new Position(PositionState.EMPTY);
        INTAKE = new Position(PositionState.EMPTY);

        SHOOTER.previous = H2;
        H2.next = SHOOTER;
        H2.previous = H1;
        H1.next = H2;
        H1.previous = INTAKE;
        INTAKE.next = H1;
    }

    @Override
    public int hashCode() {
        // int hash = 0;
        // int i = 0;
        // Position[] positions = {INTAKE, H1, H2, SHOOTER};

        // for (Position pos : positions) {
        //     if (pos.state != null) {
        //         hash |= pos.state.ordinal() << i;
        //     }
        //     i += 2;
        // }

        int hash = INTAKE.state.ordinal();
        hash |= H1.state.ordinal() << 2;
        hash |= H2.state.ordinal() << 4;
        hash |= SHOOTER.state.ordinal() << 6;

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BallStates) {
            return obj.hashCode() == hashCode();
        } else {
            return false;
        }
    }
    
    public enum PositionState {
        EMPTY,
        RIGHT,
        WRONG,
        UNKNOWN
    }

    public class Position {
        Position previous;
        Position next;
        PositionState state;

        private Position(PositionState state) {
            this.state = state;
        }
        
        /**
         * @return the state
         */
        public PositionState getState() {
            return state;
        }

        /**
         * @param state the state to set
         */
        void setState(PositionState state) {
            this.state = state;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Position) {
                return state == ((Position) obj).getState()
                        && previous == ((Position) obj).previous
                        && next == ((Position) obj).next;
            }

            return false;
        }
    }
}
