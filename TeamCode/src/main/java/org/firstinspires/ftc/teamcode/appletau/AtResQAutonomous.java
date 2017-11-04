package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by Riley/Jared on 03-Jan-16.
 */
public class AtResQAutonomous extends LinearOpMode{

    ElapsedTime timer;
    AtWheelDrive driveWheels;
    AtAttachment armDrive;
    AtODS ods;


    final float leftTurnDistance = -4.0f; //Ideally these distances are integers, so they don't
    final float rightTurnDistance = 4.0f; // unalign the sides.
    final float driveUpMountainTime = 10.0f;
    final float abortGettingToMountainTime = 6f;

    public void ourInit (){
        /////// I N I T ///////
        timer = new ElapsedTime();
        driveWheels = new AtWheelDrive();
        driveWheels.initialize(hardwareMap);
        armDrive = new AtAttachment();
        armDrive.initialize(hardwareMap);
        ods = new AtODS();
        ods.initialize(hardwareMap);
        /////// I N I T ///////
    }

    @Override
    public void runOpMode() throws InterruptedException {
        ourInit();
        // wait for the start button to be pressed
        telemetry.addData("Hi there!  Press start when you're ready.", "");
        waitForStart();

        //redMountain();
        redParking();

        driveWheels.stop();
        //driveWheelsBack.directRight(0);
        //driveWheelsBack.directLeft(0);
        armDrive.stop();
        telemetry.addData("Done!", "");

        //  P  E  R  F  E  C  T !
    }

    private void debugData (){
        driveWheels.reportPosition(telemetry);
        telemetry.addData("  Timer reading:", timer.time());
        telemetry.addData("  ODS reading:", ods.getLight());
    }

    private void redParking(){
        int stage = 0;
        boolean done = false;
        timer.reset();

        while (!done){
            switch (stage){
                case 0:
                    //Drive til line
                    telemetry.addData("Linked drive until line", "");
                    driveWheels.linkedDrive(.8f); //Drive linkedly until hit line
                    if (ods.atLine() || timer.time() > 5) {
                        driveWheels.alignl();
                        driveWheels.alignr();
                        driveWheels.stop();
                        stage ++;
                    }
                    break;

                case 1: //Non-looping
                    telemetry.addData("Forward a bit more", "");
                    driveWheels.customDist(3, 3);
                    driveWheels.alignl();
                    driveWheels.alignr();
                    //driveWheelsBack.stop();
                    stage ++;
                    break;


                case 2:
                    timer.reset();
                    stage ++;
                    break;

                case 3:
                    //Now drive until we see a new color (parking zone), or until it's been too long and hope is lost
                    telemetry.addData("Linked drive until line (again)", "");
                    driveWheels.linkedDrive(.8f); //Drive linkedly until hit line
                    if (timer.time() > 6){
                        telemetry.addData("We probably missed.   :_(","   Aborting");
                        //return; //Abort.
                        stage=7;
                    }
                    if (ods.atLine()) {
                        driveWheels.alignl();
                        driveWheels.alignr();
                        stage ++;
                    }
                    break;


                case 4:
                    //And a while longer, to actually get forward
                    telemetry.addData("Driving forward a little, linkedly", "");
                    telemetry.addData("Time remaining til give up:   ", 3 - timer.time());
                    driveWheels.linkedDrive(.8f);
                    if (timer.time() > 3) {
                        stage = 7;
                    }
                    break;

                case 7:
                    done = true;
                    telemetry.addData("  Done!  ", "");
                    driveWheels.stop();
                    try {
                        waitOneFullHardwareCycle();
                        telemetry.addData("   Waited a full hardware cycle.  ", "");
                    } catch (InterruptedException e) {
                    }
            }
            debugData();
            telemetry.addData("Stage:  ", stage);
        }
    }

    private void redMountain(){
        int stage = 0;
        boolean done = false;

        while (!done){
            switch (stage){
                case 0:
                    //Drive til line
                    telemetry.addData("Linked drive until line","");
                    driveWheels.linkedDrive(.8f); //Drive linkedly until hit line
                    if (ods.atLine()) {
                        driveWheels.alignl();
                        driveWheels.alignr();
                        driveWheels.stop();
                        stage ++;
                    }
                    break;

                case 1: //Non-looping
                    //turn 90ish degrees left
                    telemetry.addData("Turning left", "");
                    driveWheels.customDist(leftTurnDistance, rightTurnDistance); // customDist() Can turn too! ain't that amazing?
                    driveWheels.alignl();
                    driveWheels.alignr();
                    //driveWheelsBack.stop();
                    stage ++;
                    break;

                case 2: //Non-looping
                    //Drive forward a while, to lose sight of this line
                    driveWheels.customDist(2f, 2f);
                    driveWheels.alignl();
                    driveWheels.alignr();
                    stage ++;
                    break;

                case 3:
                    timer.reset();
                    stage ++;
                    break;

                case 4:
                    //Now drive until we see a new color (ramp), or until it's been too long and hope is lost
                    telemetry.addData("Linked drive until line (again)", "");
                    driveWheels.linkedDrive(.8f); //Drive linkedly until hit line
                    if (timer.time() > abortGettingToMountainTime){
                        telemetry.addData("We probably missed.   :_(","   Aborting");
                        //return; //Abort.
                        stage=7;
                    }
                    if (ods.atLine()) {
                        driveWheels.alignl();
                        driveWheels.alignr();
                        stage ++;
                    }
                    break;

                case 5:
                    timer.reset();
                    stage ++;
                    break;

                case 6:
                    //And a while longer, to actually get up the mountain
                    telemetry.addData("Driving up mountain, linkedly", "");
                    telemetry.addData("Time remaining til give up:   ", driveUpMountainTime - timer.time());
                    driveWheels.linkedDrive(.8f);
                    if (timer.time() > driveUpMountainTime) {
                        stage ++;
                    }
                    break;

                case 7:
                    done = true;
                    telemetry.addData("  Done!  ", "");
                    driveWheels.stop();
                    try {
                        waitOneFullHardwareCycle();
                        telemetry.addData("   Waited a full hardware cycle.  ", "");
                    } catch (InterruptedException e) {
                    }
            }
            debugData();
            telemetry.addData("Stage:  ", stage);
        }
    }
/*
    private void oldStuff(){
        //Drive til line
        while(!ods.atLine()) {
            telemetry.addData("Linked drive until line","");
            debugData();
            driveWheelsBack.linkedDrive(.8f); //Drive linkedly until hit line
        }
        driveWheelsBack.stop();

        //turn 90ish degrees left
        telemetry.addData("Turning left", "");
        driveWheelsBack.customDist(leftTurnDistance, rightTurnDistance); // customDist() Can turn too! ain't that amazing?
        driveWheelsBack.stop();

        //Drive forward a while, to lose sight of this line
        driveWheelsBack.customDist(2f, 2f);

        //Now drive until we see a new color (ramp), or until it's been too long and hope is lost
        timer.reset();
        while(!ods.atLine()) {
            telemetry.addData("Linked drive until line (again)", "");
            driveWheelsBack.linkedDrive(.8f); //Drive linkedly until hit line
            debugData();
            if (timer.time() > abortGettingToMountainTime){
                telemetry.addData("We probably missed.   :_(","   Aborting");
                return; //Abort.
            }
        }

        //And a while longer, to actually get up the mountain
        timer.reset();
        while (timer.time() < driveUpMountainTime) {
            telemetry.addData("Driving up mountain, linkedly", "");
            telemetry.addData("Time remaining til give up:   ", driveUpMountainTime - timer.time());
            debugData();
            driveWheelsBack.linkedDrive(.8f);
        }

    }
*/

}