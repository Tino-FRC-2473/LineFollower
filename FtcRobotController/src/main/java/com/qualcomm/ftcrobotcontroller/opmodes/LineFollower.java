package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Kashyap on 6/11/16.
 */
public class LineFollower extends OpMode {
    private double lineValueL;
    private double lineValueR;
    private double lineValueC;
    private double currentTime;
    private boolean leftSense;
    private boolean rightSense;
    private boolean centerSense;
//    DcMotor a;
//    DcMotor b;
    DcMotor c;
    DcMotor d;
    AnalogInput lineL;
    AnalogInput lineC;
    AnalogInput lineR;

    @Override
    public void init() {
        hardwareMap.logDevices();
        lineL = hardwareMap.analogInput.get("L");
        lineC = hardwareMap.analogInput.get("CE");
        lineR = hardwareMap.analogInput.get("R");
//        a = hardwareMap.dcMotor.get("A");
//        b = hardwareMap.dcMotor.get("B");
        c = hardwareMap.dcMotor.get("C");
        d = hardwareMap.dcMotor.get("D");

        //Reverse the directions of the left motors
//        a.setDirection(DcMotor.Direction.REVERSE);
        c.setDirection(DcMotor.Direction.REVERSE);

//      reset encoders
        c.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        d.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        //Set all line sensing values to false
        leftSense = false;
        rightSense = false;
        centerSense = false;

        //Calibrate sensors for the line
        lineValueL = lineL.getValue();
        lineValueR = lineR.getValue();
        lineValueC = lineC.getValue();

        //Display values on the Driver Station
        telemetry.addData("Left Calibration: ", lineValueL);
        telemetry.addData("Right Calibration: ", lineValueR);
        telemetry.addData("Center Calibraion: ", lineValueC);
    }

    @Override
    public void loop() {
//        If the center sensor detects something other than the line, the robot stops moving
        checkLine();
        if (!centerSense) {
            stopRobot();
        }
        else {
            //activate encoders
            c.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            d.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            //run robot forward
            runRobot(0.1);
        }

//        Display current sensor values on the Driver Station
        telemetry.addData("Encoder Left", c.getCurrentPosition());
        telemetry.addData("Encoder Right", d.getCurrentPosition());
        telemetry.addData("Sensor Output Left", lineL.getValue());
        telemetry.addData("Sensor Output Center", lineC.getValue());
        telemetry.addData("Sensor Output Right", lineR.getValue());
        telemetry.addData("Left Ouput Difference", lineL.getValue() - lineValueL);
        telemetry.addData("Right Ouput Difference", lineR.getValue() - lineValueR);
        telemetry.addData("Center Ouput Difference", lineC.getValue() - lineValueC);
    }

    //Checks if the current sensor values are within 100 of the calibrated values
    public void checkLine() {
        //Left sensor
        if (lineL.getValue() < 100 + lineValueL && lineL.getValue() > lineValueL - 100) {
            leftSense = true;
        }
        else {
            leftSense = false;
        }
        //Right sensor
        if (lineR.getValue() < 100 + lineValueR && lineR.getValue() > lineValueR - 100) {
            rightSense = true;
        }
        else {
            rightSense = false;
        }
        //Center sensor
        if (lineC.getValue() < 100 + lineValueC && lineC.getValue() > lineValueC - 100) {
            centerSense = true;
        }
        else {
            centerSense = false;
        }
    }

    public void stopRobot() {
//        a.setPower(0);
//        b.setPower(0);
        c.setPower(0);
        d.setPower(0);
    }

    public void runRobot(double speed) {
//        a.setPower(speed);
//        b.setPower(speed);
        c.setPower(speed);
        d.setPower(speed);
    }
}

