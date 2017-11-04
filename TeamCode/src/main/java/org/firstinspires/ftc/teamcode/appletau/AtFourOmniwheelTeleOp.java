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

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
@TeleOp(name = "4 Omniwheel Drive", group = "Sensor")
public class AtFourOmniwheelTeleOp extends AtFourOmniwheelMethods {

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {
        long startLoopTime = System.nanoTime();
        telemetry.addData("loop begin:", System.nanoTime() - startTimeMs);

        ///////////////  D R I V E R   1  ///////////////
        driver1();
        telemetry.addData("driver 1 end:", System.nanoTime() - startTimeMs);

        ///////////////  D R I V E R   2  ///////////////
        driver2();
        telemetry.addData("driver 2 end:", System.nanoTime() - startTimeMs);

        /////////////  T E L E M E T R Y  ///////////////
        telemetry();
        telemetry.addData("telemetry / big loop end:", System.nanoTime() - startTimeMs);
        loopTimes[nextLoopTimerListIndex] = System.nanoTime() - startLoopTime;
        nextLoopTimerListIndex ++;
        if (nextLoopTimerListIndex >= 4) {
            nextLoopTimerListIndex = 0;
        }
    }

    //Controls and gizmos for the first driver (chassis)
    private void driver1() {
        if (gamepad1.b) { // Red button stops stuff
            stopAll();
            return;
        }

        // The joystick driving
        joy1stick1OmniDrive();

        // The Toggles
        if (gamepad1.a) { // A toggles slow mode
            if (!switchedSlowLastTime) {
                slow = !slow;
            }
            switchedSlowLastTime = true;
        } else {
            switchedSlowLastTime = false;
        }
        if (gamepad1.x) { // X toggles absolute direction, rather than the robot's forward
            if (!switchedAbsDirLastTime) {
                absDir = !absDir;
            }
            switchedAbsDirLastTime = true;
        } else {
            switchedAbsDirLastTime = false;
        }
        if (gamepad1.y) { // Y toggles whether the robot is in beacon push mode
            if (!toggledBeaconModePrevious) {
                beaconPushMode = !beaconPushMode;
                //frontColorSensor.enableLed(beaconPushMode);
                middleColorSensor.enableLed(beaconPushMode);
            }
            toggledBeaconModePrevious = true;
        } else {
            toggledBeaconModePrevious = false;
        }

        if (beaconPushMode){
            if (middleSeeingLine()){
                middleWhiteFound = true;
            }/*
            if (frontSeeingLine()){
                frontWhiteFound = true;
            }*/
        } else {
            middleWhiteFound = false;
            frontWhiteFound = false;
        }

        if (slow || (beaconPushMode && middleWhiteFound)) {
            flPower *= slowModeMultiplier;
            frPower *= slowModeMultiplier;
        }

        blPower = frPower; // it works this way for driving
        brPower = flPower;

        if (Math.abs(speed) < .1) { // If left joystick is not big, maybe rotate with right
            float power = (Math.abs(gamepad1.right_stick_x) < .2) ? 0 : gamepad1.right_stick_x;
            flPower = power;
            blPower = power;
            frPower = -power;
            brPower = -power;
        }
        float generalPower = .9f * ((slow || (beaconPushMode && frontWhiteFound)) ? slowModeMultiplier : 1);
        if (gamepad1.right_trigger > 0.2f) { // Pinwheel turn right (with noise correction)
            flPower = generalPower;
            blPower = generalPower;
            frPower = generalPower * -1;
            brPower = generalPower * -1;
        } else if (gamepad1.left_trigger > 0.2f) { // Pinwheel turn left
            flPower = generalPower * -1;
            blPower = generalPower * -1;
            frPower = generalPower;
            brPower = generalPower;
        }

        driveWheelsLeft.power1(blPower);
        driveWheelsLeft.power2(flPower);
        driveWheelsRight.power1(brPower);
        driveWheelsRight.power2(frPower);

    }

    boolean buttonPushOnRightSide = false;

    private void driver2() {
        if (gamepad2.b){
            stopAll();
            return;
        }
        if (gamepad2.x){
            unstickBall();
        }

        if (!unstickingBall && !throwing) {
            float throwPower = gamepad2.left_stick_y;
            if (Math.abs(throwPower) < .20) {
                throwPower = 0;
            }
            thrower.setPower(throwPower);
        }

        if (gamepad2.right_stick_y >= 0)
            collector.setPower(Math.pow(gamepad2.right_stick_y, 2)); //Scaled input for collector
        else
            collector.setPower(-1 * Math.pow(gamepad2.right_stick_y, 2)); //Scaled input for collector

        if (gamepad2.y){
            acceleratorGate.setPosition(GATE_UP_POS);
        } else if (gamepad2.a){
            acceleratorGate.setPosition(GATE_DOWN_POS);
        }

        if (gamepad2.right_trigger > 0.2f){
            buttonPusher.setPosition(RIGHT_BUTTON_POS);
        } else if (gamepad2.left_trigger > 0.2f){
            buttonPusher.setPosition(LEFT_BUTTON_POS);
        } else if (gamepad2.start){
            buttonPusher.setPosition(NO_BUTTON_POS);
        }
        dpadBeaconer(false); // alternate way to press beacon buttons: D pad

        //testServos();

        if (gamepad2.left_stick_button){
            startShooting();
        }

        unstickBallBackgroundRoutine();
    }
}
