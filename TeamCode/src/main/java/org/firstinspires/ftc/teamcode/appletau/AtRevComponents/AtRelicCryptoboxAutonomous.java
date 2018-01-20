package org.firstinspires.ftc.teamcode.appletau.AtRevComponents;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Nathan Swartz January 2018. Started with basic autonomous code made by Jared Menningen and Riley Hunt.
 */

@Autonomous(name = "Concept: relicCryptobox", group = "Concept")
public class AtRelicCryptoboxAutonomous extends LinearOpMode{

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

    ElapsedTime timer;


    public void ourInit (){
        /////// I N I T ///////
        //initialize RightDrive motor. If it didn't work, still run the rest of the code.
        try{
            telemetry.addLine("initing right drive");
            RightDrive = new AtREVMotor("RD");
            telemetry.addData("R Drive Initialization Good: ", RightDrive.init(hardwareMap));
            RightDriveGood = true;
        } catch (Exception e) {
            telemetry.addData("R Drive Initialization Good: ", "FALSE");
            RightDriveGood = false;
        }

        //initialize Left Drive
        try{
            telemetry.addLine("initing left drive");
            LeftDrive = new AtREVMotor("LD");
            telemetry.addData("L Drive Initialization Good: ", LeftDrive.init(hardwareMap));
            LeftDriveGood = true;
        } catch (IllegalArgumentException e) {
            telemetry.addData("L Drive Initialization Good: ", "FALSE");
            LeftDriveGood = false;
        }
        try {
            telemetry.addLine("done initing");
            telemetry.update();
        } catch (Exception e) {
            //do nothing
        }

        /////// I N I T ///////
    }

    @Override
    public void runOpMode() throws InterruptedException {
        try {
            telemetry.addLine("starting initializing");
        } catch (Exception e) {
            //nothing
        }
        try {
            ourInit();
        } catch (Exception e) {
            //nothing
        }

        // wait for the start button to be pressed
        try {
            telemetry.addData("Hi there!  Press start when you're ready.", "");
        } catch (Exception e) {
            //nothing
        }
        waitForStart();//this is needed, waits for the user to press the play button on the DS

        //get off balance stone
        try {
            driveToSafeZone();
        } catch (Exception e) {
            //nothing
        }

        try {
            telemetry.addData("Done!", "");
        } catch (Exception e) {
            //nothing
        }

        //  P  E  R  F  E  C  T ! actually not
    }

    private void debugData (){

        telemetry.addData("  Timer reading:", timer.time());

    }

    private void driveToSafeZone(){
        int stage = 0;
        boolean done = false;
        try {
            timer.reset();
        } catch (Exception e) {
            //nothing
        }

        try {
            RightDrive.setPower(.3);
            LeftDrive.setPower(-.3);
        } catch (Exception e) {
            //nothing
        }

        try {
            Thread.sleep(3500);
        } catch (Exception e) {
            //do something?
        }

        try {
            RightDrive.setPower(0);
            LeftDrive.setPower(0);
        } catch (Exception e) {
            //nothing
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