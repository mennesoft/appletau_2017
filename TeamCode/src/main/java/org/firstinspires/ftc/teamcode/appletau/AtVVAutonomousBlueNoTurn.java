package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 *
 * Created by Jared and Riley on 12-Nov-16.
 */

//@Autonomous(name = "VV 2 ball Autonomous (Blue Side, no turn)", group = "Autonomous")
public class AtVVAutonomousBlueNoTurn extends AtVVAutonomousMethods {

    @Override
    public void runOpMode() throws InterruptedException {
        // Init
        ourInit();
        telemetry.update();
        // wait for the start button to be pressed
        telemetry.addData("Hi there!  Press start when you're ready.", "");
        waitForStart();

        //calibrateGyro();

        frontColorSensor.enableLed(true);    // Set the LED in the beginning
        middleColorSensor.enableLed(true);    // Set the LED in the beginning
        beaconColorSensor.enableLed(true);

        frontInitialLightReading = getCurrentFrontLightReading();
        middleInitialLightReading = getCurrentMiddleLightReading();

        /*
        //TEST OF COLOR SENSOR
        int dbgt = 0;
        while (dbgt < 20 * 300){
            telemetry.addData("MIDDLE LIGHT", getCurrentMiddleLightReading());
            telemetry.addData("MIDDLE TARGET", middleInitialLightReading);
            telemetry.addData("FRONT LIGHT", getCurrentFrontLightReading());
            telemetry.addData("FRONT TARGET", frontInitialLightReading);
            telemetry.addData("TIMER", System.currentTimeMillis() / 100);
            dbgt++;
            Thread.sleep(50);
            telemetry.addData("Front Sensor Info : ", frontColorSensor.getConnectionInfo());
            telemetry.addData("Middle Sensor Info: ", middleColorSensor.getConnectionInfo());
            telemetry.addData("Beacon Sensor Info: ", beaconColorSensor.getConnectionInfo());
            telemetry.update();
        }
        */

        //get out from the wall

        driveAtPower(.3f);
        telemetry.addData("Start-Drive","");
        telemetry.update();
        Thread.sleep(400);

        driveAtPower(.6f);
        telemetry.addData("Mid-Drive","");
        telemetry.update();
        Thread.sleep(700);

        driveAtPower(.3f);
        telemetry.addData("End-Drive","");
        telemetry.update();
        Thread.sleep(300);

        stopDrive();

        //Throw a ball
        telemetry.addData("1st Throw","");
        telemetry.update();
        throwBall();
        thrower.setPower(0);

        telemetry.addData("Collecting","");
        telemetry.update();
        collector.setPower(1f);
        Thread.sleep(2000);
        telemetry.addData("2nd Throw","");
        telemetry.update();
        throwBall();
        thrower.setPower(0);
        collector.setPower(0);

        setLeftPowers(-.3f, -.3f);
        setRightPowers(.3f, .3f);
        Thread.sleep(400);

        stopDrive();

        driveAtPower(.5f);
        Thread.sleep(1000);

        stopDrive();

        // Done.
        telemetry.addData("Done!", "");

        //  P  E  R  F  E  C  T !
    }

}
