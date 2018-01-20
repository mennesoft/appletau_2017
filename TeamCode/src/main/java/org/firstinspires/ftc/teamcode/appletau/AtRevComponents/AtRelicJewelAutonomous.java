package org.firstinspires.ftc.teamcode.appletau.AtRevComponents;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Nathan Swartz January 2018. Started with basic autonomous code made by Jared Menningen and Riley Hunt.
 */



@Autonomous(name = "Concept: relicJewel", group = "Concept")
public class AtRelicJewelAutonomous extends LinearOpMode {

    //we only need the drive motors
    AtREVMotor LeftDrive;
    boolean LeftDriveGood;
    float lDrive;
    AtREVMotor RightDrive;
    boolean RightDriveGood;
    float rDrive;

    ColorSensor color_sensor;//color sensor
    boolean colorGood = true;
    float Red;
    float Green;
    float Blue;

    ElapsedTime timer;


    public void ourInit() {
        /////// I N I T ///////
        //initialize RightDrive motor. If it didn't work, still run the rest of the code.

        telemetry.addLine("Starting init...");
        telemetry.update();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            telemetry.addLine("Problem with sleeping!");
            telemetry.update();
        }

        try {
            telemetry.addLine("initing right drive");
            RightDrive = new AtREVMotor("RD");
            telemetry.addData("R Drive Initialization Good: ", RightDrive.init(hardwareMap));
            RightDriveGood = true;
        } catch (Exception e) {
            telemetry.addData("R Drive Initialization Good: ", "FALSE");
            RightDriveGood = false;
        }

        //initialize Left Drive
        try {
            telemetry.addLine("initing left drive");
            LeftDrive = new AtREVMotor("LD");
            telemetry.addData("L Drive Initialization Good: ", LeftDrive.init(hardwareMap));
            LeftDriveGood = true;
        } catch (IllegalArgumentException e) {
            telemetry.addData("L Drive Initialization Good: ", "FALSE");
            LeftDriveGood = false;
        }

        try {
            telemetry.addLine("initing color sensor");
            color_sensor = hardwareMap.colorSensor.get("C");
            telemetry.addData("Color Initialization Good: ", color_sensor);
            colorGood = true;
        } catch (Exception e) {
            telemetry.addData("Color Initialization Not Good: ", e.getMessage());
            colorGood = false;
        }

        telemetry.addLine("done initing");
        telemetry.update();


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

        telemetry.addData("Hi there!  Press start when you're ready.", "");

        waitForStart();//this is needed, waits for the user to press the play button on the DS

        //LATER: move out servo arm

        try {
            color_sensor.enableLed(true);
        } catch (Exception e) {
            telemetry.addData("enable LED no work:",e);
        }

        //get off balance stone: drive forward slowly for 1.5 seconds
        /*/disabled for now
        try {
            LeftDrive.setPower(-.25);
            RightDrive.setPower(.25);
            Thread.sleep(1500);
            LeftDrive.setPower(0);
            RightDrive.setPower(0);
        } catch (Exception e) {
            //nothing
        }
        /*/

        //read the value from the color sensor

        try {
            telemetry.addData("timer seconds:",timer.seconds());
            telemetry.update();
            Thread.sleep(1000);
        } catch (Exception e) {
            telemetry.addData("error w/ timer:",e);
            telemetry.update();
            Thread.sleep(2000);
        }

        double count = 0;

        while (count < 3000/*/timer.seconds() < 25/*/) {
            count += 1;
            Thread.sleep(200);
            try {
                Red = color_sensor.red();
                Green = color_sensor.green();
                Blue = color_sensor.blue();

            } catch (Exception e) {
                //shrug
            }//end try/catch
            try {
                telemetry.addData("R:", Red);
                telemetry.addData("G:", Green);
                telemetry.addData("B:", Blue);
                telemetry.update();
            } catch (Exception e) {
                //nothing, what can you do if telemetry doesn't work?
            }
        }//end while loop

        //LATER: move arm in correct direction


        try {
            telemetry.addData("Done!", "");
        } catch (Exception e) {
            //nothing, what can you do if telemetry doesn't work?
        }

        //  P  E  R  F  E  C  T ! actually not
    }

    private void debugData() {

        telemetry.addData("  Timer reading:", timer.time());

    }
}

