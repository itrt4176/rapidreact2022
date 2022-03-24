package frc.irontigers.robot.utils;

import frc.tigerlib.interpolable.InterpolatingDouble;
import frc.tigerlib.interpolable.InterpolatingTreeMap;

public class AdjustableInterpolatingTreeMap<K extends InterpolatingDouble, V extends InterpolatingDouble> extends InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> {
    
    public AdjustableInterpolatingTreeMap(int maximumSize) {
        super(maximumSize);
    }

    public AdjustableInterpolatingTreeMap() {
        super();
    }
    
    public void replaceNearest(K key, V value) {
        InterpolatingDouble topBound = ceilingKey(key);
        InterpolatingDouble bottomBound = floorKey(key);

        double topDistance = topBound.value - key.value;
        double bottomDistance = key.value - bottomBound.value;

        if (topDistance < bottomDistance) {
            remove(topBound);
            put(key, value);
        } else if (bottomDistance < topDistance) {
            remove(bottomBound);
            put(key, value);
        } else {
            put(key, value);
        }
    }
}
