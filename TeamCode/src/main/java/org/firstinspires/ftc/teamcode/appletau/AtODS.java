package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Riley on 1/28/16.
 *
 * Incorporate an Optical Distance Sensor into your robot!  It distinguishes between red/white
 * and grey/blue colors, in distances less than 8cm.  It's got a third the functionality of a
 * color sensor, and the potential usefulness of an ultrasonic sensor over short range.
 *
 * One random internet user's setup reads 130 on white tape, 100 on red, 30 on
 * blue, and 20 on gray mat (since it shines only a red light)
 *
 * From Modern Robotics:
 *   The Optical Distance Sensor uses a pulsed light signal to return a value representing the
 *   distance from a target surface.  As the distance from the target changes the returned value
 *   changes indicating whether the movement is towards the target or away from the target.
 *
 *   The ODS operates by pulsing an LED to illuminate the target surface and measuring the strength
 *   of the reflected signal. The returned value will vary for the same distance as the reflectivity
 *   of the target surface varies.
 *
 *   Specifications:
 *   Power:  5 volts DC. 20 mA max.
 *   Standard 3 pin connector:  For connection to 3 pin analog port
 *   Signaling voltage levels:  Analog 0  5 volts
 *   Wiring standard:  Pin 1  black  Gnd | Pin 2  red  +5v | Pin 3  yellow  Analog output
 *   Illumination LED color:  Red
 *   Typical maximum range:  8 centimeters
 *   Typical minimum range:  0.5 centimeters
 *   Internal sampling rate:  1,000 samples per second
 *   Unit dimensions LxWxH:  32 x 32 x 12 millimeters
 *   Mounting holes:  24 x 24 millimeters square pattern
 *   Unit weight: 10 grams
 *   Wire length: 25 centimeters
 *
 * It should work.
 */
public class AtODS {
    private final int colorThreshold = 30;
    OpticalDistanceSensor distanceSensor;
    private String sensorName;
    private double ambient;

    public AtODS() {
        this("ods");
    }
    public AtODS(String name) {
        sensorName = name;
    }

    public boolean initialize(HardwareMap hardwareMap) {
        distanceSensor = hardwareMap.opticalDistanceSensor.get(sensorName);
        boolean success = distanceSensor != null;
        if (success) {
            ambient = distanceSensor.getRawLightDetected();
        }
        return success;
    }

    public double getLight() {
        return distanceSensor.getRawLightDetected() - ambient;
    }

    public boolean atLine(){
        return (getLight() > colorThreshold);
    }
}
