package frc.irontigers.robot.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import frc.irontigers.robot.utils.OnClearedTrigger;

public class OnClearedTriggerTest {
    private boolean testVal;
    private OnClearedTrigger testTrigger;

    @Test
    public void testOnlyFalse() {
        testTrigger = new OnClearedTrigger(this::testBoolSupplier);
        testVal = false;
        assertFalse(testTrigger.getAsBoolean());
        assertFalse(testTrigger.getAsBoolean());
    }

    @Test
    public void testFalseThenTrue() {
        testTrigger = new OnClearedTrigger(this::testBoolSupplier);
        testVal = false;
        assertFalse(testTrigger.getAsBoolean());
        testVal = true;
        assertFalse(testTrigger.getAsBoolean());
    }

    @Test
    public void testFalseThenContinuousTrue() {
        testTrigger = new OnClearedTrigger(this::testBoolSupplier);
        testVal = false;
        assertFalse(testTrigger.getAsBoolean());
        testVal = true;
        for (int i = 0; i < 10; i++) {
            assertFalse(testTrigger.getAsBoolean());
        }
    }

    @Test
    public void testGetAsBooleanFull() {
        testTrigger = new OnClearedTrigger(this::testBoolSupplier);
        testVal = false;
        assertFalse(testTrigger.getAsBoolean());

        testVal = true;
        assertFalse(testTrigger.getAsBoolean());

        testVal = false;
        assertTrue(testTrigger.getAsBoolean());
        assertFalse(testTrigger.getAsBoolean());
    }
    
    private boolean testBoolSupplier() {
        return testVal;
    }
}
