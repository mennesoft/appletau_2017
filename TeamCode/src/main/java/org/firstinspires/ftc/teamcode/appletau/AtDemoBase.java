package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.appletau.AtRevComponents.AtREVModule;
import org.firstinspires.ftc.teamcode.appletau.AtRevComponents.AtREVMotor;
import org.firstinspires.ftc.teamcode.appletau.AtRevComponents.AtREVMotorPair;

/**
 * Created by Riley on 13-Apr-17.
 * Base class that hides as much code as possible from the end user.
 *
 * Done:
 *  Bandit
 *  ScrapBot
 *  MiniBot
 *
 */
abstract class AtDemoBase extends LinearOpMode{
    private String robotName = "undefined";
    private AtWheelPair omnisLeft;
    private AtWheelPair omnisRight;
    private AtWheelPair wheels;

    private AtREVModule revModule;

    void ourInit(String robotName) {
        this.robotName = robotName;
        if (robotName.equals("Bandit") || robotName.equals("MiniBot")) {
            wheels = new AtWheelPair("left", "right", "master");
            telemetry.addData("Drive Initialization Good: ", wheels.initialize(hardwareMap));
            wheels.setMotorDirections(true, false);
        } else if (robotName.equals("Particle Accelerator") || robotName.equals("Phoenix") || robotName.equals("ScrapBot")) {
            omnisLeft = new AtWheelPair("drive-bl", "drive-fl", "Left Side");
            omnisRight = new AtWheelPair("drive-br", "drive-fr", "Right Side");
            telemetry.addData("Drive Left Initialization Good: ", omnisLeft.initialize(hardwareMap));
            omnisLeft.setMotorDirections(false, true);
            telemetry.addData("Drive Right Initialization Good: ", omnisRight.initialize(hardwareMap));
            omnisRight.setMotorDirections(false, true);
        } else if (robotName.equals("REV TEST")){
            revModule = new AtREVModule();
            revModule.add(new AtREVMotorPair("motor0:motor1"));
            revModule.initialize(hardwareMap);
        }
    }

    /**
     * Drive your robot straight!
     * @param power how much power to give the motors.  This ranges from -100 (backward fast) to 0 (stopped) to 100 (forwards fast)
     * @param time how many seconds you should do this for.
     */
    void drive(int power, long time){
        double dpower = (double)power/100;
        switch (robotName){
            case "MiniBot":
            case "Bandit":
                wheels.power1((float) dpower);
                wheels.power2((float) dpower);
                break;
            case "ScrapBot":
            case "Particle Accelerator":
                omnisLeft.power1((float) dpower);
                omnisLeft.power2((float) dpower);
                omnisRight.power1((float) dpower);
                omnisRight.power2((float) dpower);
                break;
            case "REV TEST":
                ((AtREVMotorPair)revModule.get("motor0:motor1")).setPower(1);
                break;
        }
        sleep(time*1000);
    }

    /**
     * Drive your robot straight!
     * @param power how much power to give the motors.  This ranges from -100 (backward fast) to 0 (stopped) to 100 (forwards fast)
     * @param time how many seconds you should do this for.
     */
    void driveLeft(int power, long time){
        double dpower = (double)power/100;
        switch (robotName){
            case "MiniBot":
            case "Bandit":
                wheels.power1((float) dpower);
                break;
            case "ScrapBot":
            case "Particle Accelerator":
                omnisLeft.power1((float) dpower);
                omnisLeft.power2((float) dpower);
                break;
        }
        sleep(time*1000);
    }


    void turn(int power, long time){
        double dpower = (double)power/100;
        switch (robotName){
            case "MiniBot":
            case "Bandit":
                wheels.power1((float) -dpower);
                wheels.power2((float) dpower);
                break;
            case "ScrapBot":
            case "Particle Accelerator":
                omnisLeft.power1((float) dpower);
                omnisLeft.power2((float) dpower);
                omnisRight.power1((float) -dpower);
                omnisRight.power2((float) -dpower);
                break;
        }
        sleep(time*1000);
    }

    /**
     * Make all the motors (and thus the robot) stop.
     */
    void stopDrive(){
        drive(0,0);
    }
}