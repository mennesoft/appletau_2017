package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 *
 * Created by Jared and Riley on 12-Nov-16.
 */

//@Autonomous(name = "VV 2 ball Autonomous (Red Side, no turn)", group = "Autonomous")
public class AtVVAutonomousRedNoTurn extends AtVVAutonomousMethods {

    @Override
    public void runOpMode() throws InterruptedException {
        // Init
        ourInit();
        telemetry.update();
        // wait for the start button to be pressed
        telemetry.addData("Hi there!  Press start when you're ready.", "");
        waitForStart();

        setLeftPowers(.3f, .3f);
        setRightPowers(-.3f, -.3f);
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
