package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Manages the motors for an arm. 'At' stands for Apple Tau. One motor extends and retracts, the
 * other motor raises and lowers.
 */
public class AtArm {
    private static final String WinchMotorName = "ArmW";
    private static final String ElevationMotorName = "ArmE";

    private DcMotor winch;
    private DcMotor elevation;

    /**
     * Initialize the arm motors. This succeeds only if both motors named 'ArmW' and 'ArmE'.
     * @param hardwareMap The map of attached hardware devices.
     * @return True if all motors are found, False otherwise.
     */
    public boolean initialize(HardwareMap hardwareMap) {
        winch = hardwareMap.dcMotor.get(WinchMotorName);
        elevation = hardwareMap.dcMotor.get(ElevationMotorName);
        return (winch != null) && (elevation != null);
    }

    /**
     * Release the winch so that the arm extends with spring tension.
     * @param speedTo100 An integer from 0 (stopped) to 100 (full speed).
     */
    public void extend(int speedTo100) {
        double power = Math.max(-1.0, -Math.abs(speedTo100) / 100.0);
        winch.setPower(power);
    }

    /**
     * Activate the winch so that the arm retracts against spring tension.
     * @param speedTo100 An integer from 0 (stopped) to 100 (full speed).
     */
    public void retract(int speedTo100) {
        double power = Math.min(1.0, Math.abs(speedTo100) / 100.0);
        winch.setPower(power);
    }

    /**
     * Move the elevation motor so that the arm goes up.
     * @param speedTo100 An integer from 0 (stopped) to 100 (full speed reverse).
     */
    public void raise(int speedTo100) {
        double power = Math.min(1.0, Math.abs(speedTo100) / 100.0);
        elevation.setPower(power);
    }

    /**
     * Move the elevation motor so that the arm goes down.
     * @param speedTo100 An integer from 0 (stopped) to 100 (full speed reverse).
     */
    public void lower(int speedTo100) {
        double power = Math.max(-1.0, -Math.abs(speedTo100) / 100.0);
        elevation.setPower(power);
    }

    /**
     * Stop all motors.
     */
    public void stop() {
        winch.setPower(0);
        elevation.setPower(0);
    }
}
