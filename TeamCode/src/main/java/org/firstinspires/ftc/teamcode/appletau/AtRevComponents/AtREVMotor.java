package org.firstinspires.ftc.teamcode.appletau.AtRevComponents;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
public class AtREVMotor extends AtREVComponent {
    private DcMotor motor;
    ///private int position;

    public AtREVMotor(String motorName) {
        name = motorName;
    }

    @Override
    public boolean init(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, name);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        return (motor != null);
    }

    public void setPower(double power) {
        motor.setPower(power);
    }

    /**
     * @param isForward whether the direction is forward (false is reverse)
     */
    public void setDirection(boolean isForward) {
        DcMotorSimple.Direction dir = (isForward) ? DcMotor.Direction.FORWARD : DcMotor.Direction.REVERSE;
        motor.setDirection(dir);
    }

    /**
     * Sets motor power according to some given position, stops if within the given sensitivity value
     * @param power power to set the motor
     * @param degrees goal position in degrees
     * @param sensitivity sensitivity in degrees
     */
    public void powerToPosition (double power, int degrees, int sensitivity) {
        power = Math.abs(power);
        int goalPos = (int) (degrees * (3.1111111111)); // 1120 / 360 = 3.1111111111111
        int margin = (int) (sensitivity * (3.1111111111));
        double  motorPower = power;
        if (Math.abs(motor.getCurrentPosition() - goalPos) < 2 * margin) {
            motorPower /= 2;
        }
        if (Math.abs(motor.getCurrentPosition() - goalPos) > margin) { //If far away enough from goal position
            if (motor.getCurrentPosition() < goalPos) {
                motor.setPower(motorPower); //If position is behind the assigned position, move "forwards"
            } else {
                motor.setPower(-1 * motorPower); //Otherwise, must be ahead of assigned position, must "go back"
            }
        } else {
            motor.setPower(0); //Stop if within sensitivity value of goal position
        }
    }

    public int getPosition() {return motor.getCurrentPosition(); }

}
