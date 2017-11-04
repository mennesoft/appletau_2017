package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Jared on 23-Dec-15.  Messed up by Riley at Engineering Expo.
 */

public class AtAttachment {
    /*
    AtAttachment:

    Declares a set of motors useful for the arm, and can move the arm motors
    It acts like AtMotorEncoder, but it only describes 2 motors, and has a special method for setting the spool to a specific rotation

    */
    public final static int spoolCeiling = 2500;
    public final static float wingOut = .1f;//small
    public final static float wingIn = .7f; //big

    private AtMotorEncoder aimingMotor;
    private AtMotorEncoder spoolMotor;
    //private Servo rightWing;
    //private Servo leftWing;

    public int wingsExtended = 0; //It's not a boolean so that we can have an initial 'null' position

    public int spoolPos = 0; //Tracks spool position (see 'moveSpool') as its own, neatly writable variable

    public AtAttachment(){ //Constructor
        aimingMotor = new AtMotorEncoder("Aiming"); //Establishes 'aimingMotor' and 'spoolMotor'
        spoolMotor = new AtMotorEncoder("Spool");
    }

    public boolean initialize(HardwareMap hardwareMap) { //Maps the hardware and returns true if successful
        boolean success = aimingMotor.initialize(hardwareMap, DcMotor.Direction.FORWARD);
        success &= spoolMotor.initialize(hardwareMap, DcMotor.Direction.FORWARD);
        //rightWing = hardwareMap.servo.get("wing-r");
        //leftWing = hardwareMap.servo.get("wing-l");

        //if (rightWing == null) { success = false; }
        //if (leftWing == null) { success = false; }

        return success;
    }

    public void moveAiming(float power){  //powers the aiming mechanism
        power = Range.clip(power, -1, 1);
        aimingMotor.setPower(power);
    }

    public void moveSpool(float power){  //powers the spool to extend the motors
        spoolPos = spoolMotor.getPosition();
        if (Math.abs(spoolPos) < spoolCeiling) {
            power = Range.clip(power, -1, 1);
            spoolMotor.setPower(power);
        }

    }

    public void flapWings() { //Flip-flops wing position between extended and not. The first use of the method resets to default position
        /*
        switch (wingsExtended) {
            case 0:
                rightWing.setPosition(wingIn);
                leftWing.setPosition(wingIn);
                wingsExtended = 1; //'1' means not extended
                break;
            case 1:
                rightWing.setPosition(wingOut);
                leftWing.setPosition(wingOut);
                wingsExtended = 2; //'2' means it is extended
                break;
            case 2:
                rightWing.setPosition(wingIn);
                leftWing.setPosition(wingIn);
                wingsExtended = 1; // returns to original position
                break;
        }
        */
    }
    public void setWingsPosition(boolean wingsUp){
        /*
        if (wingsUp) {
            rightWing.setPosition(wingIn);
            leftWing.setPosition(wingIn);
        }else{
            rightWing.setPosition(wingOut);
            leftWing.setPosition(wingOut);
        }
        */
    }

    public void moveSpoolTo(int target){  //powers the spool until the encoder is within 3 degrees of the target
        int diff;
        do {
            int current = spoolMotor.getPosition(); //Method will obviously not work if there isn't an encoder there/connected
            if (current - target > 10 ) {
                spoolMotor.setPower(-50);
            }
            if (current - target < -10 ) {
                spoolMotor.setPower(50);
            }
            diff = Math.abs(spoolMotor.getPosition() - target);
            //spoolMotor.getPosition() used instead of current because current hadn't been updated again before checking if its job is done
        } while (diff > 10);
    }

    //Reports telemetry info
    public void reportPosition(Telemetry telemetry) {
        telemetry.addData("[#] Spool Position: ", spoolMotor.getPosition());
        //telemetry.addData("[#] Right Wing Position: ", rightWing.getPosition());
        //telemetry.addData("[#] Left  Wing Position: ", leftWing.getPosition());
        if (wingsExtended == 1) {
            telemetry.addData("[#] Wing Status: ", "Retracted");
        }else if (wingsExtended == 2) {
            telemetry.addData("[#] Wing Status: ", "Extended");
        }else{
            telemetry.addData("[#] Wing Status: ", "Not initialized (retracted)");
        }
    }

    public void stop() {
        aimingMotor.setPower(0);
        spoolMotor.setPower(0);
        //rightWing.setPosition(rightWing.getPosition());
        //leftWing.setPosition(leftWing.getPosition());
    }
}
