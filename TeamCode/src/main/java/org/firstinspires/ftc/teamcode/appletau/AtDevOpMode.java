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

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class AtDevOpMode extends OpMode {

    AtWheelDrive driveWheels;
    private boolean lastTimeX = false;
    private double time = 0;

	/**
	 * Constructor
	 */
	public AtDevOpMode() {

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

		driveWheels = new AtWheelDrive();
        driveWheels.initialize(hardwareMap);
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		/*
		 * Gamepad 1.
		 * 
		 * Gamepad 1 controls the motors via the sticks with tank drive
		 */

        //Normal Tank Driving
        float right = gamepad1.right_stick_y;
        float left = gamepad1.left_stick_y;


		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);

		if (gamepad1.b){ // Red button stops stuff
			driveWheels.stop();
        }else if (gamepad1.y){  // Spin
            left = 1f;
            right = -1f;
		}else if (gamepad1.a){ //Pressing 'a' runs full power!
            left = 1f;
            right = 1f;
		}else if (gamepad1.right_bumper){ // Swing turn right
            left = .9f;
            right = .4f;
        }else if (gamepad1.left_bumper){ // Swing turn left
            left = .4f;
            right = .9f;
        }else {
			driveWheels.customSpeed(left, right);
		}

        if (gamepad1.x) {
            driveWheels.alignr();
        }

        //Telemetry:
		driveWheels.reportPosition(telemetry);

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("'Time' : ", time);
        telemetry.addData("left tgt",  " pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt", "pwr: " + String.format("%.2f", right));
        telemetry.addData("Everything(1) : ", gamepad1.toString());
        telemetry.addData("Everything(2) : ", gamepad2.toString());

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
	double scaleInput(double dVal)  {
		int sign = (int) (dVal/Math.abs(dVal));  // n / |n| = sign of n (reapply later)
		dVal = Math.abs(dVal);
		double raw = Math.pow(10.0,(dVal-1.0)) ; //Natural log
        dVal = raw -.1 + .1*dVal;
		return dVal*sign;
	}

}
