// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.irontigers.robot.utils;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.button.Trigger;

/** Add your docs here. */
public class OnClearedTrigger extends Trigger {
    private BooleanSupplier isActive;
    private boolean last;

    public OnClearedTrigger(BooleanSupplier isActive) {
        this.isActive = isActive;
        last = false;
    }

    @Override
    public boolean getAsBoolean() {
        boolean get = !isActive.getAsBoolean() && last;
        last = isActive.getAsBoolean();
        
        return get;
    }
}
