/* No Copyright (c) 2015 FTC TEAM 7979 APPLE TAU

No rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to none of the limitations in the disclaimer below) 
provided that none, any, or all of the following conditions are met:

Redistributions of source code must retain the above lack of copyright notice, 
this list of conditions and the following disclaimer.

Redistributions in binary form mustn't reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of FTC TEAM 7979 APPLE TAU nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE NOT GRANTED BY
THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYLEFT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYLEFT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; DESTRUCTION CAUSED BY ROUGE ROBOTS; MASSIVE EXPLOSIONS AND COLLAPSE
OF SOCIETY; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WEATHERING CONTRACT, STRICT LIABILITY,
OR TORTOISE (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.appletau.AtRevComponents.*;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
@TeleOp(name = "REV Test", group = "Concept")
public class AtTwoWheelTeleOp extends OpMode {
    //AtWheelPair driveWheels;
    //AtAttachment attachments;
    private int wingTime = 0;
    private boolean slow = true;
    private boolean linked = false;
    private boolean justLinked = false;
    private boolean justWinged = false;
    private boolean wingsUp = false;
    private boolean switchedSlowLastTime = false;
    private final int wingWaitTime = 300;
    float left;
    float right;

    String[] bads = {" ", " ", " ", " "};

    private final float slowModeMultiplier = .4f;
    private final boolean wingInactive = false;

    private AtREVMotor spinner1;
    private AtREVMotor spinner2;
    AtREVMotor beltMover;
    private AtREVModule revModule;

    /**
     * Constructor
     */
    public AtTwoWheelTeleOp() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
		/*
         * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		revModule = new AtREVModule();

        spinner1 = new AtREVMotor("belt_L");
        revModule.add(spinner1);

        spinner2 = new AtREVMotor("belt_R");
        revModule.add(spinner2);

//        beltMover = new AtREVMotor("belt_mover");
//        revModule.add(beltMover);

        revModule.initialize(hardwareMap);

        //telemetry.addData("Drive Initialization Good: ", driveWheels.initialize(hardwareMap));

        //attachments = new AtAttachment();
        //telemetry.addData("Attachment Initialization Good: ", attachments.init(hardwareMap));
        //attachments.flapWings();
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        ///////////////  D R I V E R   1  ///////////////
        driver1();

        ///////////////  D R I V E R   2  ///////////////
        //driver2();

        /////////////  T E L E M E T R Y  ///////////////
        telemetry();

    }

    //Controls and gizmos for the first driver (chassis)
    private void driver1() {

        //Spinner Value
        float spin1 = (float)scaleInput(gamepad1.right_stick_y);
        float spin2 = (float)scaleInput(gamepad1.left_stick_y);
        float mover = (float)scaleInput(gamepad1.right_stick_x);

        if (spin1 < 0.95f) spin1 /= 3;
        if (spin2 < 0.95f) spin2 /= 3;
        if (mover < 0.95f) mover /= 3;

        if (gamepad1.a){ //Unison Spinning +
            ((AtREVMotor)revModule.get("belt_L")).setPower(.5f);
            ((AtREVMotor)revModule.get("belt_R")).setPower(-.5f);
        } else if (gamepad1.y){ //Unison Spinning -
            ((AtREVMotor)revModule.get("belt_L")).setPower(.5f);
            ((AtREVMotor)revModule.get("belt_R")).setPower(-.5f);
//        } else if (gamepad1.x){ //belt moving +
//            ((AtREVMotor)revModule.get("belt_mover")).setPower(.5f);
//        } else if (gamepad1.b){ //belt moving -
//            ((AtREVMotor)revModule.get("belt_mover")).setPower(-.5f);
        } else {
            ((AtREVMotor)revModule.get("belt_L")).setPower(spin1);
            ((AtREVMotor)revModule.get("belt_R")).setPower(spin2);
//            ((AtREVMotor)revModule.get("belt_mover")).setPower(mover);
        }
    }


    //Controls and gizmos for the second driver (attachments)
    public void driver2() {

        float leftA = gamepad2.left_stick_y;
        float rightA = gamepad2.right_stick_y;

        leftA = (float) scaleInput(leftA) * .6f;
        rightA = (float) scaleInput(rightA);

        //attachments.moveAiming(leftA);
        //attachments.moveSpool(rightA);

        if (gamepad2.x) {
            if (!justWinged) {
                wingsUp = !wingsUp;
            }
            justWinged = true;
        } else {
            justWinged = false;
        }
       // attachments.setWingsPosition(wingsUp);

        /*
        if (gamepad2.x && wingTime > wingWaitTime && !wingInactive) { //Simple timer; wingInactive disables entire thing
            attachments.flapWings();
            wingTime = 0;
        }
        wingTime += 1;
        */
    }


    //Updates telemetry
    public void telemetry() {
        //   attachments.reportPosition(telemetry);
        //driveWheels.reportPosition(telemetry);
        /*
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Left  side pwr:", left);
        telemetry.addData("Left  side raw:", driveWheels.getPower1());
        telemetry.addData("Right side pwr:", right);
        telemetry.addData("Right side raw:", driveWheels.getPower2());
        telemetry.addData("Everything(1): ", gamepad1.toString());
        telemetry.addData("Everything(2): ", gamepad2.toString());
        /*  | Code above compresses data into a neat 4 lines |
        if (driveWheelsBack.frBad) {
            telemetry.addData("\nFR encoder is bad!\n", "");
        }
        if (driveWheelsBack.flBad) {
            telemetry.addData("\nFL encoder is bad!\n", "");
        }
        if (driveWheelsBack.brBad) {
            telemetry.addData("\nBR encoder is bad!\n", "");
        }
        if (driveWheelsBack.blBad) {
            telemetry.addData("\nBL encoder is bad!\n", "");
        }
        */
    }


    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {
        //driveWheels.stop();
    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    private double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.06, 0.08, 0.09, 0.11, 0.13, 0.15,
                0.18, 0.22, 0.25, 0.33, 0.40, 0.52, 0.66, 0.85, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
    /*   An alternative scaler, using logs instead of predefined values
	double scaleInput(double dVal)  {
		int sign = (int) (dVal/Math.abs(dVal));  // n / |n| = sign of n (reapply later)
		dVal = Math.abs(dVal);
		double raw = Math.pow(10.0,(dVal-1.0)) ; //Natural log
        dVal = raw -.1 + .1*dVal;
		return dVal*sign;
	}
     */

}
