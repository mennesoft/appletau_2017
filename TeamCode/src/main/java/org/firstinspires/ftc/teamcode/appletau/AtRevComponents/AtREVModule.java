package org.firstinspires.ftc.teamcode.appletau.AtRevComponents;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;


/**
 * Manages the main drive motors. 'At' stands for Apple Tau.
 *
 * Riley uncrossed wires here on 3 Dec 16.  The power1 and power2 methods had been crossing wires.
 * This probably breaks backward compatibility.  I'm not too sorry.
 */
public class AtREVModule {
    private ArrayList<AtREVComponent> components = new ArrayList<>();

    /*
    public AtREVModule(String[] motorNames) {
        for (int index = 0; index < motorNames.length; index++) {
            motors[index] = new AtREVMotor(motorNames[index]);
        }
    }
    */

    /**
     * Initialize the drive motors. This succeeds only if all 4 motors named 'wleft', 'wright',
     * 'drive-bl' and 'drive-br' are found in the hardware map.
     *
     * @param hardwareMap The map of attached hardware devices.
     * @return True if all motors are found, False otherwise.
     *
     */
    public boolean initialize(HardwareMap hardwareMap) {
        boolean success = true;
        for (int ii = 0; ii < components.size(); ii++) {
            success &= components.get(ii).init(hardwareMap);
        }
        return success;
    }

    public AtREVComponent get(String name){
        for (int ii = 0; ii < components.size(); ii++){
            if (components.get(ii).name.equals(name)){
                return components.get(ii);
            }
        }
        return null;
    }

    public void add(AtREVComponent component){
        components.add(component);
    }
}
