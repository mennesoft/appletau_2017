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

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
@TeleOp(name = "Concept: 2WheelPrototype", group = "Concept")
public class AtTwoWheelPrototypeTeleOp2017 extends OpMode {
    AtWheelPair driveWheels;
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


    /**
     * Constructor
     */
    public AtTwoWheelPrototypeTeleOp2017() {

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

        driveWheels = new AtWheelPair("left","right","master");
        telemetry.addData("Drive Initialization Good: ", driveWheels.initialize(hardwareMap));

        //attachments = new AtAttachment();
        //telemetry.addData("Attachment Initialization Good: ", attachments.initialize(hardwareMap));
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
    public void driver1() {

        //Normal Tank Driving
        right = gamepad1.right_stick_y;
        left = gamepad1.left_stick_y;

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float) scaleInput(right);
        left = (float) scaleInput(left);

        if (gamepad1.a) {
            if (!switchedSlowLastTime) {
                slow = !slow;
            }
            switchedSlowLastTime = true;
        } else {
            switchedSlowLastTime = false;
        }

        if (gamepad1.b) { // Red button stops stuff
            //attachments.stop();
            driveWheels.stop();
        } else {
            // These are just some presets and normal driving if no super special motor buttons (above things) are pressed
            if (gamepad1.right_trigger > 0) { // Swing turn right
                left = .9f;
                right = .15f;
            } else if (gamepad1.left_trigger > 0) { // Swing turn left
                left = .15f;
                right = .9f;
            }

            if (slow) {
                left *= slowModeMultiplier;
                right *= slowModeMultiplier;
            }

            driveWheels.power1(left);
            driveWheels.power2(right);
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
        driveWheels.reportPosition(telemetry);
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
        driveWheels.stop();
    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal) {
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
