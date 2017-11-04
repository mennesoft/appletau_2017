package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * CURRENTLY NOT TESTED
 * Created by Jared and Riley on 12-Nov-16.
 */

@Autonomous(name = "VV Autonomous (Red Side)", group = "Autonomous")
public class AtVVAutonomousRed extends LinearOpMode {
    private ModernRoboticsI2cGyro gyro;
    private AtWheelPair driveWheelsLeft;
    private AtWheelPair driveWheelsRight;
    private Servo buttonPusher;
    private Servo acceleratorGate;
    private ColorSensor middleColorSensor;
    private ColorSensor beaconColorSensor;
    private int MAX_TIME_TO_LINE = 10; // seconds, a constant
    private int MAX_TIME_TO_ROTATE = 5; // seconds, a constant
    private int MAX_TIME_TO_ALIGN = 8; // seconds, a constant
    private int MAX_TIME_TO_APPROACH = 3; // seconds, a constant

    float LEFT_BUTTON_POS = 0.53f;
    float RIGHT_BUTTON_POS = 0.13f;
    float NO_BUTTON_POS = 0.33f;

    float GATE_UP_POS = 0.33f;
    float GATE_DOWN_POS = 0.13f;

    private AtOldMotor thrower;
    private AtOldMotor collector;

    private int TURN_ANGLE = 25; // degrees

    private ColorSensor frontColorSensor;
    private float frontInitialLightReading;
    private float middleInitialLightReading;


    /**
     * Constructor
     */
    public AtVVAutonomousRed() {

    }

    private void ourInit() {
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
        frontInitialLightReading = getCurrentFrontLightReading();

        beaconColorSensor = hardwareMap.colorSensor.get("beacon color");
        beaconColorSensor.enableLed(false);

        middleColorSensor = hardwareMap.colorSensor.get("middle color");
        middleColorSensor.enableLed(false);    // Set the LED in the beginning
        middleInitialLightReading = getCurrentMiddleLightReading();


        telemetry.addData("Magic Test Number: ", 4);

        // get a reference to a Modern Robotics GyroSensor object.
        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");

        //telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        //telemetry.update();
    }


    @Override
    public void runOpMode() throws InterruptedException {
        // Init
        ourInit();
        telemetry.update();
        // wait for the start button to be pressed
        telemetry.addData("Hi there!  Press start when you're ready.", "");
        waitForStart();

        calibrateGyro();

        /*
        frontColorSensor.enableLed(true);    // Set the LED in the beginning
        middleColorSensor.enableLed(true);    // Set the LED in the beginning
        beaconColorSensor.enableLed(true);


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

        //get out from the wall so we can rotate

        driveAtPower(.6f);
        telemetry.addData("GRYO VAL: ", gyro.getIntegratedZValue());
        telemetry.update();
        Thread.sleep(1000);

        stopDrive();

        // Rotate a little, like 40ish degrees, so we point toward the white line.
        setLeftPowers(.4f, .4f);
        setRightPowers(-.4f, -.4f);

        int t = 0;
        while (t < 20*MAX_TIME_TO_ROTATE && gyro.getIntegratedZValue() < TURN_ANGLE){
            telemetry.addData("GRYO VAL: ", gyro.getIntegratedZValue());
            telemetry.update();
            Thread.sleep(50); // 20 hertz
            t++;
        }
        if (t > 20*MAX_TIME_TO_ROTATE){ // Just for debug
            System.out.println("Gyro didn't reach 45 deg in time!!!");
            telemetry.addData("Uh oh: ", "Gyro didn't reach 45 deg in time!!!");
            telemetry.update();
        }
        stopDrive();
        int preStopVal = gyro.getIntegratedZValue();
        Thread.sleep(750);

        //Throw a ball
        thrower.setPower(1f);
        Thread.sleep(1500);
        acceleratorGate.setPosition(GATE_UP_POS);
        Thread.sleep(1500);
        thrower.setPower(0);

        /*

        // Go forward until sees the line
        driveAtPower(.7f); //Go fast initially
        Thread.sleep(1000);
        driveAtPower(.15f); //Creep Forward for maximum German precision
        t=0;
        while (t < 20*MAX_TIME_TO_LINE && !middleSeeingLine()){
            telemetry.addData("Sensor Read:", getCurrentMiddleLightReading());
            telemetry.update();
            Thread.sleep(50); // 20 hertz
            t++;
        }
        if (t > 20*MAX_TIME_TO_LINE){ // Just for debug
            telemetry.addData("Uh oh: ", "middle sensor didn't see line in time!!!");
        } else {
            telemetry.addData("STOPPED DUE TO MIDDLE SENSOR! ", "");
            telemetry.update();
        }
        stopDrive();

        // Creep backwards, cuz we consistently overshoot a bit.
        driveAtPower(-.15f);
        Thread.sleep(200);
        stopDrive();

        telemetry.addData("POST-TURN GRYO VAL: ", preStopVal);
        //telemetry.addData("FINAL GRYO VAL: ", finalTurnAngle); -- See note on sanity
        telemetry.update();

        Thread.sleep(500);

        turnTowardBeaconColor();
        //turnTowardBeaconGyro();

        // Now creep forward towards the beacon.  As we get closer, the color sensor on the pusher
        //  should read ever-increasing values.  Use this to tell how close we are.
        driveAtPower(.4f);
        t=0;
        while (t < 20*MAX_TIME_TO_APPROACH && !(getCurrentBeaconRedReading()<4 && getCurrentBeaconBlueReading()<4)){
            telemetry.addData("", "Waiting for beacon color to be big");
            telemetry.update();
            Thread.sleep(50); // 20 hertz
            t++;
        }
        stopDrive();
        telemetry.addData("", "Done approaching beacon");
        telemetry.addData("Beacon red: ", getCurrentBeaconRedReading());
        telemetry.addData("Beacon blue: ", getCurrentBeaconBlueReading());
        telemetry.addData("", "Now pushing button on beacon");
        telemetry.update();

        if (getCurrentBeaconRedReading() > getCurrentBeaconBlueReading()){
            pushLeftButton();
        } else if (getCurrentBeaconRedReading() < getCurrentBeaconBlueReading()){
            pushRightButton();
        }
        // Pause, and then back up a little.
        Thread.sleep(500);
        telemetry.addData("", "Done with beacon!");
        telemetry.update();

        driveAtPower(-.4f);
        Thread.sleep(900);
        pushNeitherButton();

        stopDrive();
        telemetry.addData("", "That's all for now!");
        telemetry.update();


        // Done.
        telemetry.addData("Done!", "");

        //  P  E  R  F  E  C  T !
        */
    }

    private void turnTowardBeaconColor() throws InterruptedException {
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

    private void turnTowardBeaconGyro() throws InterruptedException {
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

    private void calibrateGyro(){
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


    // U T I I T Y   F U N C T I O N S
    private void stopDrive(){
        driveAtPower(0f);
    }
    private void driveAtPower(float power){
        setLeftPowers(power, power);
        setRightPowers(power, power);
    }
    private void setLeftPowers(float flPower, float blPower){ // Some of these will probably have to be negated
        driveWheelsLeft.power1(-flPower);
        driveWheelsLeft.power2(blPower);
    }
    private void setRightPowers(float frPower, float brPower){// Some of these will probably have to be negated
        driveWheelsRight.power1(frPower);
        driveWheelsRight.power2(-brPower);
    }
    private boolean middleSeeingLine(){
        return getCurrentMiddleLightReading() > 3 * middleInitialLightReading;
    }
    private boolean frontSeeingLine(){
        return getCurrentFrontLightReading() > 3 * frontInitialLightReading;
    }
    private float getCurrentMiddleLightReading(){
        return (middleColorSensor.red() + middleColorSensor.green() + middleColorSensor.blue())/3;
    }
    private float getCurrentFrontLightReading(){
        return (frontColorSensor.red() + frontColorSensor.green() + frontColorSensor.blue())/3;
    }
    private float getCurrentBeaconRedReading(){
        return beaconColorSensor.red();
    }
    private float getCurrentBeaconBlueReading(){
        return beaconColorSensor.blue();
    }
    private void pushLeftButton(){
        buttonPusher.setPosition(LEFT_BUTTON_POS);
    }
    private void pushRightButton(){
        buttonPusher.setPosition(RIGHT_BUTTON_POS);
    }
    private void pushNeitherButton(){
        buttonPusher.setPosition(NO_BUTTON_POS);
    }
}
