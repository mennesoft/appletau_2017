package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Jared on 26-Jan-17.
 */

public class AtVVAutonomousMethods extends LinearOpMode {
    ModernRoboticsI2cGyro gyro;
    AtWheelPair driveWheelsLeft;
    AtWheelPair driveWheelsRight;
    Servo buttonPusher;
    Servo acceleratorGate;
    ColorSensor middleColorSensor;
    ColorSensor beaconColorSensor;
    int MAX_TIME_TO_LINE = 10; // seconds, a constant
    int MAX_TIME_TO_ROTATE = 5; // seconds, a constant
    int MAX_TIME_TO_ALIGN = 8; // seconds, a constant
    int MAX_TIME_TO_APPROACH = 3; // seconds, a constant
    float LEFT_BUTTON_POS = 0.53f;
    float RIGHT_BUTTON_POS = 0.13f;
    float NO_BUTTON_POS = 0.33f;

    float GATE_UP_POS = 0.33f;
    float GATE_DOWN_POS = 0.13f;

    AtOldMotor thrower;
    AtOldMotor collector;
    int TURN_ANGLE = 15; // degrees
    ColorSensor frontColorSensor;
    float frontInitialLightReading;
    float middleInitialLightReading;

    void ourInit() {
        // NOTE: should we make a shared piece of code for initializing our driving stuff?
        driveWheelsLeft = new AtWheelPair("drive-bl","drive-fl","Left Side");
        driveWheelsRight = new AtWheelPair("drive-br","drive-fr","Right Side");
        telemetry.addData("Drive Left Initialization Good: ", driveWheelsLeft.initialize(hardwareMap));
        driveWheelsLeft.setMotorDirections(false, true);
        telemetry.addData("Drive Right Initialization Good: ", driveWheelsRight.initialize(hardwareMap));
        driveWheelsRight.setMotorDirections(false, true);

        buttonPusher = hardwareMap.servo.get("button push");
        telemetry.addData(">", "Found button pusher servo: " + buttonPusher);
        buttonPusher.setPosition(NO_BUTTON_POS);

        acceleratorGate = hardwareMap.servo.get("gatekeeper");
        telemetry.addData(">", "Found gatekeeper servo: " + acceleratorGate);
        telemetry.update();
        acceleratorGate.setPosition(GATE_DOWN_POS);

        thrower = new AtOldMotor("throw");
        thrower.initialize(hardwareMap, DcMotor.Direction.REVERSE);
        collector = new AtOldMotor("collect");
        collector.initialize(hardwareMap, DcMotor.Direction.REVERSE);

        //Color sensors
        frontColorSensor = hardwareMap.colorSensor.get("front color");
        frontColorSensor.enableLed(false);    // Set the LED in the beginning

        beaconColorSensor = hardwareMap.colorSensor.get("beacon color");
        beaconColorSensor.enableLed(false);

        middleColorSensor = hardwareMap.colorSensor.get("middle color");
        middleColorSensor.enableLed(false);    // Set the LED in the beginning

        telemetry.addData("Magic Test Number: ", 4);

        // get a reference to a Modern Robotics GyroSensor object.
        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");

        //telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        //telemetry.update();
    }


    void throwBall() throws InterruptedException {
        thrower.setPower(1f);
        Thread.sleep(1500);
        // ball should now be in hopper; now raise gate to let ball into thrower
        acceleratorGate.setPosition(GATE_UP_POS);
        Thread.sleep(1500);
        acceleratorGate.setPosition(GATE_DOWN_POS);
    }


    @Override
    public void runOpMode() throws InterruptedException {}

    void turnTowardBeaconColor() throws InterruptedException {
        // Middle sensor sees line.  Now pinwheel rotate until front sensor does too.
        setLeftPowers(.9f, .9f);
        setRightPowers(-.9f, -.9f);
        int t=0;
        while (t < 20*MAX_TIME_TO_ROTATE && !frontSeeingLine()){
            Thread.sleep(50); // 20 hertz
            t++;
        }
        if (t > 20*MAX_TIME_TO_ROTATE){ // Just for debug
            System.out.println("front sensor didn't see line in time!!!");
            telemetry.addData("Uh oh: ", "front sensor didn't see line in time!!!");
        }
        stopDrive();
    }

    void turnTowardBeaconGyro() throws InterruptedException {
        // Middle sensor sees line.  Now pinwheel rotate until gyro says that we're perpendicular from starting angle
        setLeftPowers(-.3f, -.3f);
        setRightPowers(.3f, .3f);
        int t = 0;
        while (t < 20 * MAX_TIME_TO_ALIGN && !frontSeeingLine()) {
            Thread.sleep(50); // 20 hertz
            t++;
        }
        if (t > 20 * MAX_TIME_TO_ROTATE) { // Just for debug
            telemetry.addData("Warning: ", "front color sensor found no line");
            telemetry.update();
        }
        stopDrive();
    }

    void calibrateGyro(){
        // start calibrating the gyro.
        telemetry.addData(">", "Gyro Calibrating. Don't move me. Or else.");
        telemetry.update();
        gyro.calibrate();

        // make sure the gyro is calibrated.


        while (gyro.isCalibrating()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void basicAutonomousRoutine() throws InterruptedException {
        calibrateGyro();

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

    }


    // U T I I T Y   F U N C T I O N S
    void stopDrive(){
        driveAtPower(0f);
    }
    void driveAtPower(float power){
        setLeftPowers(power, power);
        setRightPowers(power, power);
    }
    void setLeftPowers(float flPower, float blPower){ // Some of these will probably have to be negated
        driveWheelsLeft.power1(-flPower);
        driveWheelsLeft.power2(blPower);
    }
    void setRightPowers(float frPower, float brPower){// Some of these will probably have to be negated
        driveWheelsRight.power1(frPower);
        driveWheelsRight.power2(-brPower);
    }
    boolean middleSeeingLine(){
        return getCurrentMiddleLightReading() > 3 * middleInitialLightReading;
    }
    boolean frontSeeingLine(){
        return getCurrentFrontLightReading() > 3 * frontInitialLightReading;
    }
    float getCurrentMiddleLightReading(){
        return (middleColorSensor.red() + middleColorSensor.green() + middleColorSensor.blue())/3;
    }
    float getCurrentFrontLightReading(){
        return (frontColorSensor.red() + frontColorSensor.green() + frontColorSensor.blue())/3;
    }
    float getCurrentBeaconRedReading(){
        return beaconColorSensor.red();
    }
    float getCurrentBeaconBlueReading(){
        return beaconColorSensor.blue();
    }
    void pushLeftButton(){
        buttonPusher.setPosition(LEFT_BUTTON_POS);
    }
    void pushRightButton(){
        buttonPusher.setPosition(RIGHT_BUTTON_POS);
    }
    void pushNeitherButton(){
        buttonPusher.setPosition(NO_BUTTON_POS);
    }
}
