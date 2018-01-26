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

    boolean redTeam = true;

    //we only need the drive motors
    AtREVMotor LeftDrive;
    boolean LeftDriveGood;
    float lDrive;
    AtREVMotor RightDrive;
    boolean RightDriveGood;
    float rDrive;

    AtREVServo ArmServo;
    boolean ArmGood;
    float ArmPos;

    ColorSensor color_sensor;//color sensor
    boolean colorGood = true;
    float Red;
    float Green;
    float Blue;

    //ElapsedTime timer;


    public void ourInit() {
        /////// I N I T ///////

        telemetry.addLine("Starting init...");

        //initialize RightDrive motor. If it didn't work, still run the rest of the code.
        try {
            telemetry.addLine("initing right drive");
            RightDrive = new AtREVMotor("RD");
            telemetry.addData("R Drive Initialization Good: ", RightDrive.init(hardwareMap));
            RightDriveGood = true;
        } catch (Exception e) {
            telemetry.addData("R Drive Initialization Bad: ", e.getMessage());
            RightDriveGood = false;
        }

        //initialize Left Drive
        try {
            telemetry.addLine("initing left drive");
            LeftDrive = new AtREVMotor("LD");
            telemetry.addData("L Drive Initialization Good: ", LeftDrive.init(hardwareMap));
            LeftDriveGood = true;
        } catch (Exception e) {
            telemetry.addData("L Drive Initialization Bad: ", e.getMessage());
            LeftDriveGood = false;
        }

        //initialize servo
        try {
            telemetry.addLine("initing servo");
            ArmServo = new AtREVServo("a");
            telemetry.addData("Arm Servo Initialization Good:", ArmServo.init(hardwareMap));
            ArmGood = true;
        } catch (Exception e) {
            telemetry.addData("Arm Servo Initialization Bad: ", e.getMessage());
            ArmGood = false;
        }


        //initialize color sensor
        try {
            telemetry.addLine("initing color sensor");
            color_sensor = hardwareMap.colorSensor.get("C");
            telemetry.addData("Color Initialization Good: ", color_sensor);
            colorGood = true;
        } catch (Exception e) {
            telemetry.addData("Color Initialization Bad: ", e.getMessage());
            colorGood = false;
        }

        telemetry.addLine("done initing");
        telemetry.update();

        //for the future: add a thing that keeps trying to init if a critical component doesn't init

        /////// I N I T ///////
    }

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addLine("starting initializing");

        try {
            ourInit();
        } catch (Exception e) {
            telemetry.addData("Error with init:", e.getMessage());
        }

        // wait for the start button to be pressed

        telemetry.addData("Hi there!  Press start when you're ready.", "");
        telemetry.update();

        waitForStart();//this is needed, waits for the user to press the play button on the DS

        //move out servo arm
        try {
            ArmServo.setPosition(90);//not sure what the position's value should be, just do trial and error I guess
        } catch (Exception e) {
            telemetry.addData("Error w/ servo arm:", e.getMessage());
        }

        //turn on LED of color sensor
        try {
            color_sensor.enableLed(true);
        } catch (Exception e) {
            telemetry.addData("enable LED no work:",e.getMessage());
        }

        //get off balance stone: drive forward slowly for some amount of time
        ///*/disabled for now
        try {
            LeftDrive.setPower(-.15);
            RightDrive.setPower(.15);
            Thread.sleep(1000);
            LeftDrive.setPower(0);
            RightDrive.setPower(0);
        } catch (Exception e) {
            telemetry.addData("Error w/ driving:", e.getMessage());
        }
        ///*/

        //read the value from the color sensor and take an average value

        double count = 0;
        double redSum = 0;
        double blueSum = 0;

        while (count < 3000/*/timer.seconds() < 25/*/) {
            count += 1;
            Thread.sleep(200);
            try {
                Red = color_sensor.red();
                Green = color_sensor.green();
                Blue = color_sensor.blue();

            } catch (Exception e) {
                telemetry.addData("Error w/ color sensor:", e.getMessage());
            }

            redSum += Red;
            blueSum += Blue;

            try {
                telemetry.addData("R:", Red);
                telemetry.addData("G:", Green);
                telemetry.addData("B:", Blue);
                telemetry.update();
            } catch (Exception e) {
                //nothing, what can you do if telemetry doesn't work?
            }


        }//end while loop

        double redAvg = redSum/count;
        double blueAvg = blueSum/count;

        //color sensor faces toward the left jewel

        //move arm in correct direction


        try {
            telemetry.addData("Done!", "");
        } catch (Exception e) {
            //nothing, what can you do if telemetry doesn't work?
        }

        //  P  E  R  F  E  C  T ! actually not
    }

    private void debugData() {

        //telemetry.addData("  Timer reading:", timer.time());

    }
}

