package org.firstinspires.ftc.teamcode.appletau;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * A sensor reading class
 * Created by Riley on 08-Oct-16.
 */
@TeleOp(name = "Sensor: MR Color", group = "Sensor")
public class AtSensorReader extends OpMode {
    // bPrevState and bCurrState represent the previous and current state of the button.
    private boolean bPrevState = false;
    private boolean bCurrState;

    // bLedOn represents the state of the LED.
    private boolean bLedOn = true;
    private ColorSensor colorSensor;    // Max dist = 1 1/4 inches


    @Override
    public void init() {
        try {
            // get a reference to our ColorSensor object.
            colorSensor = hardwareMap.colorSensor.get("color sensor");

            // Set the LED in the beginning
            colorSensor.enableLed(true);
        }
        catch (Exception e){
            e.printStackTrace();
            telemetry.addLine(e.toString());

            telemetry.addLine();
            telemetry.addLine();

            String err = e.toString();
            for (String s : err.split("\n")){
                telemetry.addLine(s);
            }
        }
    }

    @Override
    public void loop() {
        bCurrState = gamepad1.x;

        // check for button state transitions.
        if ((bCurrState) && (!bPrevState)) {
            bLedOn = !bLedOn;   // button is transitioning to a pressed state. So Toggle LED
            colorSensor.enableLed(bLedOn);
        }
        bPrevState = bCurrState;

        float[] hsvValues = {0f,0f,0f};

        // convert the RGB values to HSV values.
        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

        telemetry.addData("LED", bLedOn ? "On" : "Off");
        telemetry.addData("Clear", colorSensor.alpha());
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.addData("Hue", hsvValues[0]);

        telemetry.update();
    }
}

