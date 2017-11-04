package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Jared on 16-Jan-17.
 */

public class AtFourOmniwheelMethods extends OpMode {
    AtWheelPair driveWheelsLeft;
    AtWheelPair driveWheelsRight;
    boolean slow = false;
    boolean switchedSlowLastTime = false;
    boolean switchedAbsDirLastTime = false;
    boolean absDir = false;
    double x;
    double y;
    double speed;
    double joy_angle;
    double rotatedAngle;

    float flPower;
    float frPower;
    float blPower;
    float brPower;

    final float slowModeMultiplier = .2f;
    boolean beaconPushMode = false;
    boolean toggledBeaconModePrevious = false;
    boolean middleWhiteFound = false;
    boolean frontWhiteFound = false;

    ColorSensor middleColorSensor;
    private ColorSensor frontColorSensor;
    private float frontInitialLightReading;
    private float middleInitialLightReading;
    private ColorSensor beaconColorSensor;

    ModernRoboticsI2cGyro gyro;
    AtOldMotor thrower;
    AtOldMotor collector;
    Servo acceleratorGate;
    Servo buttonPusher;
    boolean accelGatePositionUp = false;

    float GATE_UP_POS = 0.33f;
    float GATE_DOWN_POS = 0.13f;
    float LEFT_BUTTON_POS = 0.53f;
    float RIGHT_BUTTON_POS = 0.13f;
    float NO_BUTTON_POS = 0.33f;
    float[] buttonPostitions = new float[]{LEFT_BUTTON_POS, NO_BUTTON_POS, RIGHT_BUTTON_POS};
    int currentButtonPos = 1;
    boolean pressedDPadLastCycle = false;

    long startTimeMs;
    long[] loopTimes = {0,0,0,0};
    int nextLoopTimerListIndex = 0;
    private int unstickBallTimer = -1;
    private long lastUnstickBallTime = System.currentTimeMillis();
    boolean throwing = false;
    private Thread throwThread;
    boolean unstickingBall = false;
    private Thread unstickThread;


    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
		/*
         * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        driveWheelsLeft = new AtWheelPair("drive-bl","drive-fl","Left Side");
        driveWheelsRight = new AtWheelPair("drive-br","drive-fr","Right Side");
        telemetry.addData("Drive Left Initialization Good: ", driveWheelsLeft.initialize(hardwareMap));
        driveWheelsLeft.setMotorDirections(true, true);
        telemetry.addData("Drive Right Initialization Good: ", driveWheelsRight.initialize(hardwareMap));
        driveWheelsRight.setMotorDirections(false, false);

        thrower = new AtOldMotor("throw");
        thrower.initialize(hardwareMap, DcMotor.Direction.FORWARD);
        collector = new AtOldMotor("collect");
        collector.initialize(hardwareMap, DcMotor.Direction.REVERSE);

        acceleratorGate = hardwareMap.servo.get("gatekeeper");
        telemetry.addData(">", "Found gatekeeper servo: " + acceleratorGate);
        telemetry.update();
        acceleratorGate.setPosition(GATE_DOWN_POS);

        buttonPusher = hardwareMap.servo.get("button push");
        telemetry.addData(">", "Found button pusher servo: " + buttonPusher);
        telemetry.update();
        buttonPusher.setPosition(NO_BUTTON_POS);

        //Color sensors
        middleColorSensor = hardwareMap.colorSensor.get("middle color");
        middleColorSensor.enableLed(false);    // Set the LED in the beginning
        middleInitialLightReading = getCurrentMiddleLightReading();

        beaconColorSensor = hardwareMap.colorSensor.get("beacon color");
        beaconColorSensor.enableLed(false);    // Set the LED in the beginning

        frontColorSensor = hardwareMap.colorSensor.get("front color");
        frontColorSensor.enableLed(false);    // Set the LED in the beginning
        frontInitialLightReading = getCurrentMiddleLightReading();
        /*
        try {
        } catch (NullPointerException e){
            telemetry.addData("Beacon color sensor not found","");
        } try {
        } catch (NullPointerException e){
            telemetry.addData("Front color sensor not found","");
        }*/

        // get a reference to a Modern Robotics GyroSensor object.
        gyro = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro");

        // start calibrating the gyro.
        telemetry.addData(">", "Gyro Calibrating. Do Not move!");
        telemetry.update();

        gyro.calibrate();

        /*

        // |!| IMPORTANT |!| init() caught in loop when while loop uncommented! (Investigation needed)

        // make sure the gyro is calibrated.
        while (gyro.isCalibrating())  {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */

        telemetry.addData(">", "Gyro Calibrated.  Press Start.");
        telemetry.update();


        //attachments = new AtAttachment();
        //telemetry.addData("Attachment Initialization Good: ", attachments.initialize(hardwareMap));
        //attachments.flapWings();
        startTimeMs = System.nanoTime();
    }

    // Override this
    @Override
    public void loop(){ }

/*
    void unstickBall(){
        if (unstickBallTimer == -1) {
            unstickBallTimer = 0;
        }
    }*/

    void unstickBallBackgroundRoutine(){/*
        int START_BACKWARD_TIME = 1000;
        int END_BACKWARD_TIME = START_BACKWARD_TIME + 1000;
        int END_FOREWARD_TIME = END_BACKWARD_TIME + 700;
        if (0 <= unstickBallTimer && unstickBallTimer < START_BACKWARD_TIME) {
            acceleratorGate.setPosition(GATE_UP_POS);
            accelGatePositionUp = true;
            //run thrower forward for half second (hopperward)
            //thrower.setPower(-.2-((700-unstickBallTimer) % 700) / 600);
            thrower.setPower(.75);
        } else if (START_BACKWARD_TIME <= unstickBallTimer && unstickBallTimer < END_BACKWARD_TIME) {
            // and run spinner to suck balls up (goalward)
            thrower.setPower(-.85);
        } else if (END_BACKWARD_TIME <= unstickBallTimer && unstickBallTimer < END_FOREWARD_TIME) {
            //run thrower backward for half second, open gate (hopperward)
            thrower.setPower(60);
        } else if (END_FOREWARD_TIME <= unstickBallTimer || unstickBallTimer == -1) {
            // when out of bounds, set stuff to normal levels.
            acceleratorGate.setPosition(GATE_DOWN_POS);
            accelGatePositionUp = false;
            unstickBallTimer = -1;
            lastUnstickBallTime = System.currentTimeMillis();
            return;
        }
        long currentTime = System.currentTimeMillis();
        unstickBallTimer += (currentTime - lastUnstickBallTime);
        lastUnstickBallTime = currentTime;*/
    }

    void unstickBall(){
        if (unstickingBall){
            return;
        }
        unstickingBall = true;
        unstickThread = new Thread(new Runnable(){
            public void run(){
                try {
                    acceleratorGate.setPosition(GATE_UP_POS);
                    accelGatePositionUp = true;
                    //run thrower forward for half second (hopperward)
                    //thrower.setPower(-.2-((700-unstickBallTimer) % 700) / 600);
                    thrower.setPower(.75);
                    Thread.sleep(1000);

                    // and run spinner to suck balls up (goal-ward)
                    thrower.setPower(-.85);
                    Thread.sleep(1000);

                    //run thrower backward for half second, open gate (hopper-ward)
                    thrower.setPower(1);
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    acceleratorGate.setPosition(GATE_DOWN_POS);
                    thrower.setPower(0);
                    unstickingBall = false;
                }
            }
        });
        unstickThread.start();
    }

    void startShooting(){
        if (throwing){
            return;
        }
        throwing = true;
        throwThread = new Thread(new Runnable(){
            public void run(){
                try {
                    thrower.setPower(1);
                    Thread.sleep(1200);
                    acceleratorGate.setPosition(GATE_UP_POS);
                    Thread.sleep(700);
                    acceleratorGate.setPosition(GATE_DOWN_POS);
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    acceleratorGate.setPosition(GATE_DOWN_POS);
                    thrower.setPower(0);
                    throwing = false;
                }
            }
        });
        throwThread.start();
    }

    void joy1stick1OmniDrive(){
        // Maths at https://docs.google.com/spreadsheets/d/1OgPl5HwFHhcrxL53pKGzzdltQKPVZg9Mb06K4MN4qeI/edit?usp=sharing
        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;
        joy_angle = Math.atan2(y, x);
        rotatedAngle = joy_angle - Math.PI/4;
        speed = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));

        if (absDir) {    //Absolute direction calculations according to gyro
            int heading = gyro.getHeading();
            rotatedAngle -= Math.toRadians(heading);
            rotatedAngle = Math.abs(rotatedAngle - 0)             < .09 ? 0             : rotatedAngle;  // if we're close to going a cardinal direction, snap to that.
            rotatedAngle = Math.abs(rotatedAngle - Math.PI / 2)   < .09 ? Math.PI / 2   : rotatedAngle;
            rotatedAngle = Math.abs(rotatedAngle - Math.PI)       < .09 ? Math.PI       : rotatedAngle;
            rotatedAngle = Math.abs(rotatedAngle - Math.PI * 3/2) < .09 ? Math.PI * 3/2 : rotatedAngle;
        }

        frPower = (float)(speed * Math.cos(rotatedAngle));
        flPower = (float)(speed * Math.sin(rotatedAngle));

        if (frPower > .02 && frPower < -.02) frPower =  0; //Deadbanding
        if (flPower >  .02 && flPower < -.02) flPower =  0; //Clipping power
    }

    void dpadBeaconer(boolean onFirstGamepad){
        boolean left = (onFirstGamepad) ? gamepad1.dpad_left : gamepad2.dpad_left;
        boolean right = (onFirstGamepad) ? gamepad1.dpad_right : gamepad2.dpad_right;
        // D pad left and right moves the pusher more left and more right
        if (left) {
            if (!pressedDPadLastCycle){
                if (currentButtonPos > 0) {
                    currentButtonPos--;
                }
                buttonPusher.setPosition(buttonPostitions[currentButtonPos]);
            }
            pressedDPadLastCycle = true;
        }
        else if (right){
            if (!pressedDPadLastCycle){
                if (currentButtonPos < 2){
                    currentButtonPos++;
                }
                buttonPusher.setPosition(buttonPostitions[currentButtonPos]);
            }
            pressedDPadLastCycle = true;
        }
        else {
            pressedDPadLastCycle = false;
        }
    }

    private void stopAttachments(){
        thrower.setPower(0);
        collector.setPower(0);
        buttonPusher.setPosition(NO_BUTTON_POS);
    }

    private void stopDrive(){
        driveWheelsLeft.stop();
        driveWheelsRight.stop();
    }

    void stopAll(){
        stopDrive();
        stopAttachments();
        if (throwThread != null){
            throwThread.interrupt();
        }if (throwThread != null){
            unstickThread.interrupt();
        }
    }

    //Updates telemetry
    public void telemetry() {
        //attachments.reportPosition(telemetry);
        //driveWheelsLeft.reportPosition(telemetry);
        telemetry.addData("Joystick1 X:", x);
        telemetry.addData("Joystick1 Y:", y);
        telemetry.addData("L Joystick2 Y:", gamepad2.left_stick_y);
        telemetry.addData("R Joystick2 Y:", gamepad2.right_stick_y);
        telemetry.addData("FL pwr:", flPower);
        telemetry.addData("BL pwr:", blPower);
        telemetry.addData("FR pwr:", frPower);
        telemetry.addData("BR pwr:", brPower);
        telemetry.addData("Slow:", slow ? "on" : "off");
        telemetry.addData("Abs direction:", absDir ? "on" : "off");
        telemetry.addData("Beacon Mode:", beaconPushMode ? "on" : "off");
        telemetry.addData("Accelerator pos:", acceleratorGate.getPosition());
        telemetry.addData("Button pusher pos:", buttonPusher.getPosition());
        //telemetry.addData("Heading angle:", gyro.getHeading());
        telemetry.addData("Raw angle:", Math.toDegrees(joy_angle));
        telemetry.addData("Corrected angle:", Math.toDegrees(rotatedAngle));
        telemetry.addData("Throw routine active:", throwing);
        telemetry.addData("Throw thread:", (throwThread != null) ? throwThread.toString() : "null");
        telemetry.addData("Unstick routine active:", unstickingBall);
        telemetry.addData("Unstick thread:",  (unstickThread != null) ? unstickThread.toString() : "null");
        //telemetry.addData("unstickBallTimer=", unstickBallTimer);
        //telemetry.addData("lastUnstickBallTime=", lastUnstickBallTime);
        /*
        telemetry.addData("Gatekeeper pos:", acceleratorGate.getPosition());
        telemetry.addData("Gatekeeper dist to DOWN: ", Math.abs(acceleratorGate.getPosition() - GATE_DOWN_POS));
        telemetry.addData("Gatekeeper dist to UP: ", Math.abs(acceleratorGate.getPosition() - GATE_UP_POS));
        */
        telemetry.addData("Time elapsed in loop (ms):", "");
        telemetry.addData(" :", (int)(loopTimes[0]/1000));
        telemetry.addData(" :", (int)(loopTimes[1]/1000));
        telemetry.addData(" :", (int)(loopTimes[2]/1000));
        telemetry.addData(" :", (int)(loopTimes[3]/1000));
        //telemetry.addData("Everything(1): ", gamepad1.toString());
        //telemetry.addData("Everything(2): ", gamepad2.toString());
        /*  | Code above compresses data into a neat 4 lines |
        if (driveWheelsLeft.frBad) {
            telemetry.addData("\nFR encoder is bad!\n", "");
        }
        if (driveWheelsLeft.flBad) {
            telemetry.addData("\nFL encoder is bad!\n", "");
        }
        if (driveWheelsLeft.brBad) {
            telemetry.addData("\nBR encoder is bad!\n", "");
        }
        if (driveWheelsLeft.blBad) {
            telemetry.addData("\nBL encoder is bad!\n", "");
        }
        */
    }

    private void testMotors(){
        if (gamepad2.dpad_left && gamepad2.dpad_up)
            flPower = .50f;
        else
            flPower = 0f;
        if (gamepad2.dpad_right && gamepad2.dpad_up){
            frPower = .50f;
        }
        else
            frPower = 0f;
        if (gamepad2.dpad_left && gamepad2.dpad_down){
            blPower = .50f;
        }
        else
            blPower = 0f;
        if (gamepad2.dpad_right && gamepad2.dpad_down){
            brPower = .50f;
        }
        else
            brPower = 0f;

        driveWheelsLeft.power1(blPower);
        driveWheelsLeft.power2(flPower);
        driveWheelsRight.power1(brPower);
        driveWheelsRight.power2(frPower);
    }


    void testServos() {
        double adjust = 0.3;
        if (gamepad2.dpad_right) {
            buttonPusher.setPosition(buttonPusher.getPosition() + adjust);
        }
        if (gamepad2.dpad_left) {
            buttonPusher.setPosition(buttonPusher.getPosition() - adjust);
        }
        if (gamepad2.dpad_up) {
            acceleratorGate.setPosition(acceleratorGate.getPosition() + adjust);
        }
        if (gamepad2.dpad_down) {
            acceleratorGate.setPosition(acceleratorGate.getPosition() - adjust);
        }
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {
        stopAll();
    }

    private float getCurrentMiddleLightReading(){
        return (middleColorSensor.red() + middleColorSensor.green() + middleColorSensor.blue())/3;
    }
    private float getCurrentFrontLightReading(){
        return (frontColorSensor.red() + frontColorSensor.green() + frontColorSensor.blue())/3;
    }
    boolean middleSeeingLine(){
        return getCurrentMiddleLightReading() > 3 * middleInitialLightReading;
    }
    private boolean frontSeeingLine(){
        return getCurrentFrontLightReading() > 3 * frontInitialLightReading;
    }
}

