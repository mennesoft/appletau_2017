package org.firstinspires.ftc.teamcode.appletau.AtRevComponents;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Tracks both a motorOne and an encoder. This class assumes use of an AndyMark NeverRest motorOne
 * encoder. We use the 40 model for the drive train.
 * <p/>
 * The listed properties are 280 cycles per revolution and 1120 ticks per revolution. Here are the
 * full specs:
 *
 * Gearbox Reduction: 40:1
 * Voltage: 12 volt DC
 * No Load Free Speed, at gearbox output shaft: 160 rpm
 * No Load Free Speed, motorOne only: 6,600 rpm
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
public class AtREVMotorPair extends AtREVComponent {
    private DcMotor motorOne;
    private DcMotor motorTwo;

    private String nameOne;
    private String nameTwo;

    public AtREVMotorPair(String motorNames) {
        //'Name' Example "drive-fl:drive-fr"
        int dividePos = motorNames.indexOf(':');
        nameOne = motorNames.substring(0,dividePos);
        nameTwo = motorNames.substring(dividePos + 1);
    }

    @Override
    public boolean init(HardwareMap hardwareMap) {
        motorOne = hardwareMap.get(DcMotor.class, nameOne);
        motorOne.setDirection(DcMotorSimple.Direction.FORWARD);
        motorTwo = hardwareMap.get(DcMotor.class, nameTwo);
        return (motorOne != null) && (motorTwo != null);
    }

    public void setPower(double power) {
        motorOne.setPower(power);
        motorTwo.setPower(power);
    }

    /**
     * @param isForward whether the direction is forward (false is reverse)
     */
    public void setOneDirection(boolean isForward) {
        DcMotorSimple.Direction dir = (isForward) ? DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE;
        motorOne.setDirection(dir);
    }

    public void setTwoDirection(boolean isForward) {
        DcMotorSimple.Direction dir = (isForward) ? DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE;
        motorTwo.setDirection(dir);
    }

    /*

    public double getPower(){
        return motorOne.getPower();
    }

    public int getPosition(){
        return motorOne.getCurrentPosition() - initPosition;
    }
    */

}
