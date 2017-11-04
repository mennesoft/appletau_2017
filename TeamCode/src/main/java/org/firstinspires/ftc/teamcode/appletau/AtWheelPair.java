package org.firstinspires.ftc.teamcode.appletau;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * Manages the main drive motors. 'At' stands for Apple Tau.
 *
 * Riley uncrossed wires here on 3 Dec 16.  The power1 and power2 methods had been crossing wires.
 * This probably breaks backward compatibility.  I'm not too sorry.
 */
public class AtWheelPair {
    private String LeftMotorName;
    private String RightMotorName;
    private String ControllerName;

    private AtOldMotor wheelOne; // Formerly wheelLeft
    private AtOldMotor wheelTwo; // Formerly wheelRight

    private int leftPower = 0;
    private int rightPower = 0;

    private AtOldMotorController control;

    //private int[][] recentEncoder = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},};
    //private int powerIndex = 0;
    //private boolean[][] recentPower = {{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},{false,false,false,false},};

    public AtWheelPair(String motor1Name, String motor2Name, String controllerName) {
        wheelOne = new AtOldMotor(motor1Name);
        wheelTwo = new AtOldMotor(motor2Name);
        control = new AtOldMotorController(controllerName);

    }

    /**
     * Initialize the drive motors. This succeeds only if all 4 motors named 'wleft', 'wright',
     * 'drive-bl' and 'drive-br' are found in the hardware map.
     *
     * @param hardwareMap The map of attached hardware devices.
     * @return True if all motors are found, False otherwise.
     */
    public boolean initialize(HardwareMap hardwareMap) {
        boolean success = wheelOne.initialize(hardwareMap, DcMotor.Direction.REVERSE);
        success &= wheelTwo.initialize(hardwareMap, DcMotor.Direction.FORWARD);
        success &= control.initialize(hardwareMap);

//        DcMotorController.DeviceMode devMode;
//        devMode = DcMotorController.DeviceMode.WRITE_ONLY;

        return success;
    }


    /**
     * Set both motors at right side to a power level.  Uses encoders to keep
     * the front and the back wheels of this side fairly aligned
     *
     * @param power A float from -1 (fast backward) to 1 (fast forward)
     */
    public void power1(float power) {
        wheelOne.setPower(power);
        rightPower = (int)(power * 100);
    }


    /**
     * Set both motors at left side to a power level.  No encoder magic currently
     *
     * @param power A float from -1 (fast backward) to 1 (fast forward)
     */
    public void power2(float power) {
        wheelTwo.setPower(power);
        leftPower = (int)(power * 100);
    }


    /**
     * Stop all motors.
     */
    public void stop() {
        power1(0);
        power1(0);
    }

    public int getPower1(){
        return leftPower;
    }

    public int getPower2(){
        return rightPower;
    }

    /**
     * @param isMotor1Forward whether motor1 is forward (false is reverse)
     * @param isMotor2Forward whether motor2 is forward (false is reverse)
     */
    public void setMotorDirections(boolean isMotor1Forward, boolean isMotor2Forward){
        wheelTwo.setDirection(isMotor1Forward);
        wheelOne.setDirection(isMotor2Forward);
    }

    /**
     * Move left and right motors at custom speeds.
     *
     * @param leftSpeed  A floating point from -1.00 (fast back) to 1.00 (fast forward).
     * @param rightSpeed A floating point from -1.00 (fast back) to 1.00 (fast forward).
     */

    /**
     * Run the motors for a certain distance (can be negative!) .75 power.
     * Uses encoders in the back of the robot.
     *
     * @param leftRot  a float of how many rotations for the left side to rotate
     * @param rightRot a float of how many rotations for the right side to rotate
     */

    /**
     * Run the motors for a certain distance (can be negative!)
     * Uses encoders in the back of the robot.
     *
     * @param leftRot  a float of how many rotations for the left side to rotate
     * @param rightRot a float of how many rotations for the right side to rotate
     * @param power    a float of how fast the motors usually go
     */


    /**
     * Run the motors until they hit a certain encoder count (can be negative!)
     * Uses encoders in the back of robot.
     *
     * @param leftGoal  an int of desired encored value
     * @param rightGoal an int of desired encoder value
     * @param power    a float of how fast the motors usually go
     */


    ///////////////////The rest of the functions may not work cuz the use ints/////////////////////

    /**
     * Move all motors the same speed in the forward direction.
     *
     An integer from 0 (stopped) to 100 (full speed ahead).
     */



    public void reportPosition(Telemetry telemetry) {

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


    /**
     * Check if robot is stalling.  As a bonus, stop motors if so.  Call this in a loop.
     */
    public boolean stalling(){
return false;

        /* How about, instead of remembering the past values we've put them at, we manually
        wait an insignificant hardware cycle and see if the encoders aren't changed.
        I like that better.

        recentPower[powerIndex][0] = (.04 > wheelOne.getPower() && wheelOne.getPower() > .04 );
        recentPower[powerIndex][1] = (.04 > wheelTwo.getPower() && wheelTwo.getPower() > .04 );
        recentPower[powerIndex][2] = (.04 > leftRear.getPower() && leftRear.getPower() > .04 );
        recentPower[powerIndex][3] = (.04 > rightRear.getPower() && rightRear.getPower() > .04 );

        recentEncoder[powerIndex][0] = wheelOne.getPosition() ;
        recentEncoder[powerIndex][1] = wheelTwo.getPosition();
        recentEncoder[powerIndex][2] = leftRear.getPosition();
        recentEncoder[powerIndex][3] = rightRear.getPosition();

        if (powerIndex == recentPower.length){
            powerIndex = 0;
        }
        powerIndex++;
*/


    }
}
