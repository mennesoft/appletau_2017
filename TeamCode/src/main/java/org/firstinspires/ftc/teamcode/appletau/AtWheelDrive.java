package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * Manages the main drive motors. 'At' stands for Apple Tau.
 */
public class AtWheelDrive {
    private static final String FrontLeftMotorName = "drive-fl";
    private static final String BackLeftMotorName = "drive-bl";
    private static final String FrontRightMotorName = "drive-fr";
    private static final String BackRightMotorName = "drive-br";
    private static final int MaxSideDifference = 10;

    private AtMotorEncoder leftFront;
    private AtMotorEncoder leftRear;
    private AtMotorEncoder rightFront;
    private AtMotorEncoder rightRear;

    public boolean frBad = false;
    public boolean flBad = false;
    public boolean brBad = false;
    public boolean blBad = false;


    private int rightMax = 0;
    private int rightSideDif = 0;
    private int leftSideDif = 0;
    private int sideDif = 0;

    private int adjustCount = 0;
    private float backPower, frontPower;
    private float adjustment;// = .20f;

    //private int[][] recentEncoder = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},};
    //private int powerIndex = 0;
    //private boolean[][] recentPower = {{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},};


    public AtWheelDrive() {
        leftFront = new AtMotorEncoder(FrontLeftMotorName);
        leftRear = new AtMotorEncoder(BackLeftMotorName);
        rightFront = new AtMotorEncoder(FrontRightMotorName);
        rightRear = new AtMotorEncoder(BackRightMotorName);
    }

    /**
     * Initialize the drive motors. This succeeds only if all 4 motors named 'drive-fl', 'drive-fr',
     * 'drive-bl' and 'drive-br' are found in the hardware map.
     *
     * @param hardwareMap The map of attached hardware devices.
     * @return True if all motors are found, False otherwise.
     */
    public boolean initialize(HardwareMap hardwareMap) {
        boolean success = leftFront.initialize(hardwareMap, DcMotor.Direction.REVERSE);
        success &= leftRear.initialize(hardwareMap, DcMotor.Direction.REVERSE);
        success &= rightFront.initialize(hardwareMap, DcMotor.Direction.FORWARD);
        success &= rightRear.initialize(hardwareMap, DcMotor.Direction.FORWARD);

        return success;
    }


    /**
     * Set both motors at right side to a power level.  Uses encoders to keep
     * the front and the back wheels of this side fairly aligned
     *
     * @param power A float from -1 (fast backward) to 1 (fast forward)
     */
    public void rightPower(float power) {
        rightSideDif = rightFront.getPosition() - rightRear.getPosition();
        backPower = power;
        frontPower = power;

        //See https://www.desmos.com/calculator/auk5yfulaj for data

        /*/ Another formula that works well is adjustment = .28*power, with a if statement that bumps
        //   *adjustment* up to like .33 when power = +-1
        adjustment = .26f*power; //.27 works well.  It is good to err on the generous side.
        if (Math.abs(power) >= .98){
            adjustment = .35f * Math.signum(power); //Get that sign right
        }
        /*
        //Use of a fancy arcsin graph - didn't work for unknown reason.  Ought to have worked better.  Alas.
        adjustment = (float) (.1f*Math.asin(2.000001*(-Math.abs(power)+.5f))-.15f);
        adjustment *= -Math.signum(power);
        */

        //A 3rd degree polynomial
        adjustment = (float) (.05 * Math.pow(power, 3.0000) + .2075 * power);

        if (rightSideDif > MaxSideDifference) {
            //The front is much farther forward than back so slow down the front
            frontPower -= adjustment;
            backPower += adjustment;
            adjustCount++;
        } else if (rightSideDif < -MaxSideDifference) {
            //The front is much farther behind than back so speed up the front
            frontPower -= adjustment; // (adding a negative value)
            backPower += adjustment;
            adjustCount++;
        }

        if (Math.abs(rightFront.getPosition()) < 3 && frontPower != 0) {
            frBad = true;
            frontPower = power; //Undoes errant adjustments
        } else {
            frBad = false;
        }
        if (Math.abs(rightRear.getPosition()) < 3 && backPower != 0) {
            brBad = true;
            backPower = power; //Undoes errant adjustments
        } else {
            brBad = false;
        }
        frontPower = Range.clip(frontPower, -1, 1); //we shouldn't give a motor a(n) |power| > 1
        backPower = Range.clip(backPower, -1, 1);
        rightFront.setPower(frontPower);
        rightRear.setPower(backPower);
    }


    /**
     * Set both motors at left side to a power level.  No encoder magic currently
     *
     * @param power A float from -1 (fast backward) to 1 (fast forward)
     */
    public void leftPower(float power) {
        leftSideDif = leftFront.getPosition() - leftRear.getPosition();
        backPower = power;
        frontPower = power;

        //A 3rd degree polynomial
        adjustment = (float) (.05 * Math.pow(power, 3.0000) + .2075 * power);

        if (leftSideDif > MaxSideDifference) {
            //The front is much farther forward than back so slow down the front
            frontPower -= adjustment;
            backPower += adjustment;
            adjustCount++;
        } else if (leftSideDif < -MaxSideDifference) {
            //The front is much farther behind than back so speed up the front
            frontPower -= adjustment; // (adding a negative value)
            backPower += adjustment;
            adjustCount++;
        }

        if (frontPower != 0) { //only updates when motors are running
            if (Math.abs(leftFront.getPosition()) < 3) {
                flBad = true;
                frontPower = power; //Undoes errant adjustments
            } else {
                flBad = false;
            }
        }

        if (backPower != 0) { //doesn't report unpowered motors as good encoders
            if (Math.abs(leftRear.getPosition()) < 3 && backPower != 0) {
                blBad = true;
                backPower = power; //Undoes errant adjustments
            } else {
                blBad = false;
            }
        }

        frontPower = Range.clip(frontPower, -1, 1); //we shouldn't give a motor a(n) |power| > 1
        backPower = Range.clip(backPower, -1, 1); //we also shouldn't do < -1 either!
        leftFront.setPower(frontPower);
        leftRear.setPower(backPower);
    }


    /**
     * Stop all motors.
     */
    public void stop() {
        leftFront.setPower(0);
        leftRear.setPower(0);
        rightFront.setPower(0);
        rightRear.setPower(0);
    }

    /**
     * Move left and right motors at custom speeds.
     *
     * @param leftSpeed  A floating point from -1.00 (fast back) to 1.00 (fast forward).
     * @param rightSpeed A floating point from -1.00 (fast back) to 1.00 (fast forward).
     */
    public void customSpeed(float leftSpeed, float rightSpeed) {
        leftPower(leftSpeed);
        rightPower(rightSpeed);
    }

    public void customPower(float leftSpeed, float rightSpeed) {
        customSpeed(leftSpeed, rightSpeed);
    }

    /**
     * Run the motors for a certain distance (can be negative!) .75 power.
     * Uses encoders in the back of the robot.
     *
     * @param leftRot  a float of how many rotations for the left side to rotate
     * @param rightRot a float of how many rotations for the right side to rotate
     */
    public void customDist(float leftRot, float rightRot) {
        customDist(leftRot, rightRot, .75f);
    }

    /**
     * Run the motors for a certain distance (can be negative!)
     * Uses encoders in the back of the robot.
     *
     * @param leftRot  a float of how many rotations for the left side to rotate
     * @param rightRot a float of how many rotations for the right side to rotate
     * @param power    a float of how fast the motors usually go
     */
    public void customDist(float leftRot, float rightRot, float power) {
        int leftGoal = (int) (leftRot * 1120 + leftRear.getPosition());
        int rightGoal = (int) (rightRot * 1120 + rightRear.getPosition());

        goUntilEncoderReading(leftGoal, rightGoal, power);
    }

    /**
     * Run the motors until they hit a certain encoder count (can be negative!)
     * Uses encoders in the back of robot.
     *
     * @param leftGoal  an int of desired encored value
     * @param rightGoal an int of desired encoder value
     * @param power    a float of how fast the motors usually go
     */
    public void goUntilEncoderReading(float leftGoal, float rightGoal, float power) {

        while (Math.abs(leftGoal - leftRear.getPosition()) > MaxSideDifference // Remember this!!!
                && Math.abs(rightGoal - rightRear.getPosition()) > MaxSideDifference) {
            leftPower(power * Math.signum(leftGoal - leftRear.getPosition())); //Power flips when it needs to go the other way
            rightPower(power * Math.signum(rightGoal - rightRear.getPosition()));
        }
    }

    public int getLeftEncoderValue(){
        return leftRear.getPosition();
    }
    public int getRightEncoderValue(){
        return rightRear.getPosition();
    }

    ///////////////////The rest of the functions may not work cuz the use ints/////////////////////

    /**
     * Move all motors the same speed in the forward direction.
     *
     * @param speedTo100 An integer from 0 (stopped) to 100 (full speed ahead).
     */
    public void forward(int speedTo100) {
        double power = Math.min(1.0, Math.abs(speedTo100) / 100.0);
//        leftFront.setPower(power);
//        leftRear.setPower(power);
        rightFront.setPower(power);
        rightRear.setPower(power);
    }

    /**
     * Move all motors the same speed in the reverse direction.
     *
     * @param speedTo100 An integer from 0 (stopped) to 100 (full speed reverse).
     */
    public void reverse(int speedTo100) {
        double power = Math.max(-1.0, -Math.abs(speedTo100) / 100.0);
//        leftFront.setPower(power);
//        leftRear.setPower(power);
        rightFront.setPower(power);
        rightRear.setPower(power);
    }


    /**
     * Move all motors so that the robot turns to the left.
     *
     * @param speedTo100 An integer from 0 (stopped) to 100 (full speed).
     */
    public void turnLeft(int speedTo100) {
        double power = Math.min(1.0, Math.abs(speedTo100) / 100.0);
//        leftFront.setPower(-power);
//        leftRear.setPower(-power);
        rightFront.setPower(power);
        rightRear.setPower(power);
    }

    /**
     * Move all motors so that the robot turns to the right.
     *
     * @param speedTo100 An integer from 0 (stopped) to 100 (full speed).
     */
    public void turnRight(int speedTo100) {
        double power = Math.min(1.0, Math.abs(speedTo100) / 100.0);
//        leftFront.setPower(power);
//        leftRear.setPower(power);
        rightFront.setPower(-power);
        rightRear.setPower(-power);
    }


    public void reportPosition(Telemetry telemetry) {
        rightSideDif = (rightFront.getPosition() - rightRear.getPosition());
        leftSideDif = (leftFront.getPosition() - leftRear.getPosition());
        rightMax = Math.max(rightMax, Math.abs(rightSideDif));
        //telemetry.addData("FR real power: ", frontPower);
        //telemetry.addData("FB real power: ", backPower);

        telemetry.addData("[FL_MotorPos: ", leftFront.getPosition());
        telemetry.addData("[BL_MotorPos: ", leftRear.getPosition());
        telemetry.addData("[FR_MotorPos: ", rightFront.getPosition());
        telemetry.addData("[BR_MotorPos: ", rightRear.getPosition());
        telemetry.addData("(FL_MotorPow: ", leftFront.getPower());
        telemetry.addData("(BL_MotorPow: ", leftRear.getPower());
        telemetry.addData("(FR_MotorPow: ", rightFront.getPower());
        telemetry.addData("(BR_MotorPow: ", rightRear.getPower());
        //telemetry.addData("[@] Right Side Max difference: ",rightMax);
        telemetry.addData("[@] FR - BR = ", rightSideDif);
        telemetry.addData("[@] FL - BL = ", leftSideDif);
        telemetry.addData("[@]  R - L  = ", sideDif);

        telemetry.addData("Adjustments: ", adjustCount);
        //telemetry.addData("Adjustment #: ",adjustment); confused


//        if (rightSped){
//            telemetry.addData("Speeding up right: ","by .1");
//        }
//        if (rightSlowed){
//            telemetry.addData("Slowing right: ","by .1");
//        }
    }

    /**
     * Align the right side of the robot, smartly
     */
    public void alignr() { //There are 1120 encoder counts in a revolution.
        rightSideDif = rightSideDif % 1120; ///Loop it all.  Without a bar, this would likely never happen.
        if (rightSideDif > 1120 / 2) {
            rightSideDif = 1120 - rightSideDif;
        }
        float power = Math.abs(rightSideDif / 250.00f);
        power = Range.clip(power, -1, 1);

        if (MaxSideDifference < rightSideDif) { //&& rightSideDif < 1120/2) {  //Go inwards
            rightFront.setPower(-power);
            rightRear.setPower(power);
        } else if (-MaxSideDifference > rightSideDif) {//) && rightSideDif > -1120/2){ //Go outwards
            rightFront.setPower(power);
            rightRear.setPower(-power);
        } else {
            //leftPower(20);
        }
    }

    public void alignl() { //There are 1120 encoder counts in a revolution.
        leftSideDif = leftSideDif % 1120; ///Loop it all.  Without a bar, this would likely never happen.
        if (leftSideDif > 1120 / 2) {
            leftSideDif = 1120 - leftSideDif;
        }
        float power = Math.abs(leftSideDif / 250.00f);
        power = Range.clip(power, -1, 1);

        if (MaxSideDifference < leftSideDif) { //&& leftSideDif < 1120/2) {  //Go inwards
            leftFront.setPower(-power);
            leftRear.setPower(power);
        } else if (-MaxSideDifference > leftSideDif) {//) && leftSideDif > -1120/2){ //Go outwards
            leftFront.setPower(power);
            leftRear.setPower(-power);
        } else {
            //rightPower(20);
        }
    }

    public void newAlign() {
        leftSideDif = leftSideDif % 1120; //Loops encoder values so that re-alignment doesn't take forever
        rightSideDif = rightSideDif % 1120;

        float adjust = (float) 0.3;
        float base = (float) 0.65;  //Inits wheel base powers
        float frpow = base;
        float flpow = base;// float brpow = base; float blpow = base;

        if (rightSideDif > MaxSideDifference) {
            frpow -= adjust;
        }
        if (rightSideDif < -MaxSideDifference) {
            frpow += adjust;
        }
        if (leftSideDif > MaxSideDifference) {
            flpow -= adjust;
        }
        if (leftSideDif < -MaxSideDifference) {
            flpow += adjust;
        }

        leftFront.setPower(flpow);
        leftRear.setPower(base);
        rightFront.setPower(frpow);
        rightRear.setPower(base);
    }

    public void directRight(float power) { //Discouraged
        rightFront.setPower(power);
        rightRear.setPower(power);
    }
    public void directLeft(float power) {
        leftFront.setPower(power);
        leftRear.setPower(power);
    }


    public void linkedDrive(float power) {
        float leftPower = power;
        float rightPower = power;

        sideDif = (leftRear.getPosition() - rightRear.getPosition()) % 1120;
        // Change range (-infinity, infinity) to [0, 1120)
        sideDif += Math.ceil(sideDif/1120);
        //If it was negative, we should have floored.  Compensate.
        if (sideDif < 0){      sideDif += 1120;                      }//Yay!
        //Change range [0, 1120) to [-560, 560)
        if (560 <= sideDif){   sideDif = -1*(1120 - sideDif);        }//Yay!


//sides:adjustment = (float) (.05 * Math.pow(power, 3.0000) + .2075 * power);
/*this*/adjustment = (float) (.07 * Math.pow(power, 3.0000) + .4000 * power); //Super aggressive

        if (sideDif > MaxSideDifference) {
            //The left is much farther forward than back so slow it down
            leftPower -= adjustment;
            rightPower += adjustment;
            adjustCount++;
        } else if (sideDif < -MaxSideDifference) {
            leftPower -= adjustment; // (adding a negative value)
            rightPower += adjustment;
            adjustCount++;
        }

        //alignl();
        //I could range.clip, but I don't feel like it (anyways
        leftPower(leftPower);
        rightPower(rightPower);
        //alignr();

    }

    /**
     * Check if robot is stalling.  As a bonus, stop motors if so.  Call this in a loop.
     */
    public boolean stalling(){
return false;

        /* How about, instead of remembering the past values we've put them at, we manually
        wait an insignificant hardware cycle and see if the encoders aren't changed.
        I like that better.

        recentPower[powerIndex][0] = (.04 > leftFront.getPower() && leftFront.getPower() > .04 );
        recentPower[powerIndex][1] = (.04 > rightFront.getPower() && rightFront.getPower() > .04 );
        recentPower[powerIndex][2] = (.04 > leftRear.getPower() && leftRear.getPower() > .04 );
        recentPower[powerIndex][3] = (.04 > rightRear.getPower() && rightRear.getPower() > .04 );

        recentEncoder[powerIndex][0] = leftFront.getPosition() ;
        recentEncoder[powerIndex][1] = rightFront.getPosition();
        recentEncoder[powerIndex][2] = leftRear.getPosition();
        recentEncoder[powerIndex][3] = rightRear.getPosition();

        if (powerIndex == recentPower.length){
            powerIndex = 0;
        }
        powerIndex++;
*/


    }
}
