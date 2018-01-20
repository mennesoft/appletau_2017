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

package org.firstinspires.ftc.teamcode.appletau.AtRevComponents;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 *
 * created by Nathan Swartz over October-January 2017-2018
 */
@TeleOp(name = "Concept: 2WheelBeltNathan", group = "Concept")
public class AtTwoWheelBeltsNathan extends OpMode {
    AtREVMotor LeftDrive;
    boolean LeftDriveGood;
    float lDrive;
    AtREVMotor RightDrive;
    boolean RightDriveGood;
    float rDrive;
    AtREVMotor LeftBelt;
    boolean LeftBeltGood;
    float lBelt;
    AtREVMotor RightBelt;
    boolean RightBeltGood;
    float rBelt;
    AtREVMotor HorzBelt;
    boolean HorzBeltGood;
    float hBelt;
    AtREVMotor Panel;
    boolean PanelGood;
    float panelUp;
    float panelDown;

    boolean switchedSlowLastTime;
    boolean switchedPanelSlowLastTime;
    boolean switchedBeltSlowLastTime;
    boolean slow;
    boolean panelSlow;
    boolean beltSlow;
    double slowModeMultiplier;



    /**
     * Constructor
     */
    public AtTwoWheelBeltsNathan() {

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

        //initialize RightDrive motor. If it didn't work, still run the rest of the code.
		try{
            RightDrive = new AtREVMotor("RD");
            telemetry.addData("R Drive Initialization Good: ", RightDrive.init(hardwareMap));
            RightDriveGood = true;
        } catch (IllegalArgumentException e) {
            telemetry.addData("R Drive Initialization Good: ", "FALSE");
            RightDriveGood = false;
        }

        //initialize Left Drive
        try{
            LeftDrive = new AtREVMotor("LD");
            telemetry.addData("L Drive Initialization Good: ", LeftDrive.init(hardwareMap));
            LeftDriveGood = true;
        } catch (IllegalArgumentException e) {
            telemetry.addData("L Drive Initialization Good: ", "FALSE");
            LeftDriveGood = false;
        }

        //initialize Right Belt
        try{
            RightBelt = new AtREVMotor("RB");
            telemetry.addData("R Belt Initialization Good: ", RightBelt.init(hardwareMap));
            RightBeltGood = true;
        } catch (IllegalArgumentException e) {
            telemetry.addData("R Belt Initialization Good: ", "FALSE");
            RightBeltGood = false;
        }

        //initialize Left Belt
        try{
            LeftBelt = new AtREVMotor("LB");
            telemetry.addData("L Belt Initialization Good: ", LeftBelt.init(hardwareMap));
            LeftBeltGood = true;
        } catch (IllegalArgumentException e) {
            telemetry.addData("L Belt Initialization Good: ", "FALSE");
            LeftBeltGood = false;
        }
        //initialize Horz Belt
        try{
            HorzBelt = new AtREVMotor("HB");
            telemetry.addData("H Belt Initialization Good: ", HorzBelt.init(hardwareMap));
            HorzBeltGood = true;
        } catch (IllegalArgumentException e) {
            telemetry.addData("H Belt Initialization Good: ", "FALSE");
            HorzBeltGood = false;
        }

        //Panel
        try{
            Panel = new AtREVMotor("P");
            telemetry.addData("Panel Initialization Good: ", Panel.init(hardwareMap));
            PanelGood = true;
        } catch (IllegalArgumentException e) {
            telemetry.addData("Panel Initialization Good: ", "FALSE");
            PanelGood = false;
        }


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
        driver2();

        /////////////  T E L E M E T R Y  ///////////////
        telemetry();

    }

    //Controls and gizmos for the first driver (chassis)
    public void driver1() {

        slowModeMultiplier = .25;

        //Normal Tank Driving
        rDrive = -gamepad1.right_stick_y;
        lDrive = gamepad1.left_stick_y;




        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        rDrive = (float) scaleInput(rDrive);
        lDrive = (float) scaleInput(lDrive);

        // slow mode
        if (gamepad1.a) {
            if (!switchedSlowLastTime) {
                slow = !slow;
            }
            switchedSlowLastTime = true;
        } else {
            switchedSlowLastTime = false;
        }

        if (slow) {
            lDrive *= slowModeMultiplier;
            rDrive *= slowModeMultiplier;
        }

        rDrive *= .95;//trying to fix a tendency to veer to the left

        if (gamepad1.b) { // Red button stops stuff
            //attachments.stop();
            rDrive = 0;
            lDrive = 0;
        }

        if (LeftDriveGood) {
            try {
                LeftDrive.setPower(lDrive);
            } catch (Exception e) {
                //report on telemetry: add later
            }
        }
        if (RightDriveGood) {
            try {
                RightDrive.setPower(rDrive);
            } catch (Exception e) {
                //report on telemetry: add later
            }

        }

    }


    //Controls and gizmos for the second driver (attachments)

    public void driver2() {
        slowModeMultiplier = .25;//not actually used, for some reason didn't work when I tried it

        //move left and right belts with up/down on joysticks
        //move left belt horizontally with left/right on left joystick
        lBelt = gamepad2.left_stick_y;
        rBelt = gamepad2.right_stick_y;
        hBelt = gamepad2.left_stick_x;
        panelUp = gamepad2.right_trigger;
        panelDown = gamepad2.left_trigger;

        //scale power from joystick/trigger inputs
        lBelt = (float) scaleInput(lBelt);
        rBelt = (float) scaleInput(rBelt);
        hBelt = (float) scaleInput(hBelt);
        panelUp = (float) scaleInput(panelUp);
        panelUp *= -1;
        panelDown = (float) scaleInput(panelDown);

        // slow mode for panel

        if (gamepad2.a) {
            if (!switchedPanelSlowLastTime) {
                panelSlow = !panelSlow;
            }
            switchedPanelSlowLastTime = true;
        } else {
            switchedPanelSlowLastTime = false;
        }

        if (panelSlow) {
            panelUp *= .25;
            panelDown *= .25;
        }


        // slow mode for both belts
        if (gamepad2.x) {
            if (!switchedBeltSlowLastTime) {
                beltSlow = !beltSlow;
            }
            switchedBeltSlowLastTime = true;
        } else {
            switchedBeltSlowLastTime = false;
        }

        if (beltSlow) {
            rBelt *= .25;
            lBelt *= .25;
        }

        //h belt always in slow mode
        hBelt *= .25;

        if (gamepad2.b) {//B button stops all motors
            rBelt = 0;
            lBelt = 0;
            hBelt = 0;
            panelDown = 0;
            panelUp = 0;
        }


        if(LeftBeltGood) {
            try {
                LeftBelt.setPower(lBelt);
            } catch (Exception e) {
                //maybe report on telemetry: add later
            }
        }
        if(RightBeltGood) {
            try {
                RightBelt.setPower(-rBelt);
            } catch (Exception e) {
                //maybe report on telemetry: add later
            }
        }
        if(HorzBeltGood) {
            try {
                HorzBelt.setPower(hBelt);
            } catch (Exception e) {
                //maybe report on telemetry: add later
            }
        }


        if(PanelGood) {
            try {
                if (panelDown == 0 && panelUp != 0) {
                    Panel.setPower(panelUp);
                } else if (panelDown != 0 && panelUp == 0) {
                    Panel.setPower(panelDown);
                } else {
                    Panel.setPower(0);
                }
            } catch (Exception e) {
                //maybe report on telemetry: add later
            }
        }


    }


    //Updates telemetry
    public void telemetry() {
        //   attachments.reportPosition(telemetry);
        //driveWheels.reportPosition(telemetry);
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Right  side pwr:", rDrive);
        //telemetry.addData("Left  side raw:", driveWheels.getPower1());
        telemetry.addData("Left side pwr:", lDrive);

        telemetry.addData("Right  belt pwr:", rBelt);
        telemetry.addData("Left  belt  pwr:", lBelt);
        telemetry.addData("Horz  belt  pwr:", hBelt);
        telemetry.addData("Panel down  pwr:",panelDown);
        telemetry.addData("Panel up    pwr:",panelUp);

        telemetry.addData("Everything(1): ", gamepad1.toString());
        telemetry.addData("Everything(2): ", gamepad2.toString());
        /*  | Code above compresses data into neat lines |
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
        double numAttempts = 0;
        boolean tryRBagain = true;
        boolean tryLBagain = true;
        boolean tryRDagain = true;
        boolean tryLDagain = true;
        boolean tryHBagain = true;

        while (numAttempts < 20 && (tryRBagain || tryLBagain || tryRDagain || tryLDagain || tryHBagain)) {

            numAttempts++;
            if (RightBeltGood && tryRBagain) {
                try {
                    RightBelt.setPower(0);
                    tryRBagain = false;
                } catch (Exception e) {
                    tryRBagain = true;
                }
            }


            if (LeftBeltGood && tryLBagain) {
                try {
                    LeftBelt.setPower(0);
                    tryLBagain = false;
                } catch (Exception e) {
                    tryLBagain = true;
                }
            }


            if (RightDriveGood && tryRDagain) {
                try {
                    RightDrive.setPower(0);
                    tryRDagain = false;
                } catch (Exception e) {
                    tryRDagain = true;
                }
            }


            if (LeftDriveGood && tryLDagain) {
                try {
                    LeftDrive.setPower(0);
                    tryLDagain = false;
                } catch (Exception e) {
                    tryLDagain = true;
                }
            }


            if (HorzBeltGood && tryHBagain) {
                try {
                    HorzBelt.setPower(0);
                    tryHBagain = false;
                } catch (Exception e) {
                    tryHBagain = true;
                }
            }
        }
        if (numAttempts>20) {
            //say in telemetry that it failed
        }
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
