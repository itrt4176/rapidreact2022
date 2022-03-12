// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot;

import edu.wpi.first.math.util.Units;
/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class ShooterVals {
        public final static double DIAMETER = Units.inchesToMeters(6);
        public final static int MOTOR_ID = 0;
        public final static double DEFAULT_SPEED = 1;
    }

    public static final class IntakeVals {
        public final static int MOTOR_ID = 1;
        public final static double DEFAULT_SPEED = 0.6;
    }

    public static final class MagazineVals {
        public final static int MOTOR_ID = 6;
        public final static double DEFAULT_SPEED = 0.6;
    }

    public static final class DriveSystemVals {
        public final static int BACK_LEFT = 2;
        public final static int FRONT_LEFT = 3;
        public final static int FRONT_RIGHT = 4;
        public final static int BACK_RIGHT = 5;

        public final static double DEFAULT_SPEED = 0.2;
    }
    public static final class ClimberVals {
        public final static int MOTOR_ID = 7; //fix this
        public final static double MAX_EXTENSION = 15714.5355; //also fix this
        public final static double MIN_EXTENSION = 0; //probbaly also this
    }
}
