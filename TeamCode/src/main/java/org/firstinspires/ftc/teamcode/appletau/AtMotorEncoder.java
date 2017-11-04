package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Tracks both a motor and an encoder. This class assumes use of an AndyMark NeverRest motor
 * encoder. We use the 40 model for the drive train.
 * <p/>
 * The listed properties are 280 cycles per revolution and 1120 ticks per revolution. Here are the
 * full specs:
 *
 * Gearbox Reduction: 40:1
 * Voltage: 12 volt DC
 * No Load Free Speed, at gearbox output shaft: 160 rpm
 * No Load Free Speed, motor only: 6,600 rpm
 * Gearbox Output Power: 14W
 * Stall Torque: 350 oz-in
 * Stall Current: 11.5 amps
 * Force Needed to Break Gearbox: 1478 oz-in
 * Minimum torque needed to back drive: 12.8 oz-in
 * Output pulse per revolution of Output Shaft (ppr): 1120 (280 rises of Channel A)
 * Output pulse per revolution of encoder shaft (ppr): 28 (7 rises of Channel A)
 *
 * That means that 1120 encoder counts = 1 rotation = 360 degrees
 */
public class AtMotorEncoder {
    final String motorName;
    private DcMotor motor;
    //public int ourPosition = 0; (Replaced by a nicer getPosition() method)
    public int initPosition;

    ///private int position;

    public AtMotorEncoder(String name) {
        motorName = name;
    }

    public boolean initialize(HardwareMap hardwareMap, DcMotor.Direction direction) {
        motor = hardwareMap.dcMotor.get(motorName);
        
        if (motor != null) {
            motor.setDirection(direction);
            initPosition = motor.getCurrentPosition();
        }
        return (motor != null);
    }

    public void setPower(double power) {
        motor.setPower(power);
    }

    public double getPower(){
        return motor.getPower();
    }

    public int getPosition(){
        return motor.getCurrentPosition() - initPosition;
    }

}
