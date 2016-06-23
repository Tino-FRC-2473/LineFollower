package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Kashyap on 6/11/16.
 */
public class LineFollower extends OpMode {
    private int state = 0;
    private double lineValueL;
    private double lineValueR;
    private double lineValueC;
    private double currentTime;
    private boolean leftSense = false;
    private boolean rightSense = false;
    private boolean centerSense = false;
    DcMotor a;
    DcMotor b;
    DcMotor c;
    DcMotor d;
    AnalogInput lineL;
    AnalogInput lineC;
    AnalogInput lineR;

    @Override
    public void init() {
        hardwareMap.logDevices();
        lineL = hardwareMap.analogInput.get("A");
        lineC = hardwareMap.analogInput.get("B");
        lineR = hardwareMap.analogInput.get("C");
        a = hardwareMap.dcMotor.get("A");
        b = hardwareMap.dcMotor.get("B");
        c = hardwareMap.dcMotor.get("C");
        d = hardwareMap.dcMotor.get("D");

        //Calibrate sensors for the line
        lineValueL = lineL.getValue();
        lineValueR = lineR.getValue();
        lineValueC = lineC.getValue();
    }

    @Override
    public void loop() {
        switch (state) {
            case 0:
                a.setPower(0.3);
                b.setPower(0.3);
                c.setPower(0.3);
                d.setPower(0.3);
                state++;
                break;
            case 1:
                checkForLine();
                if (leftSense && !rightSense && !centerSense) {
                    while (!centerSense) {
                        a.setPower(0.3);
                        b.setPower(0.5);
                        c.setPower(0.3);
                        d.setPower(0.5);
                    }
                }
                break;
            default:
                break;
        }

        telemetry.addData("Sensor Output Left", lineL.getValue());
        telemetry.addData("Sensor Output Center", lineC.getValue());
        telemetry.addData("Sensor Output Right", lineR.getValue());
    }

    public void checkForLine() {
        //Left
        if (lineL.getValue() < 100 + lineValueL || lineL.getValue() > lineValueL - 100) {
            leftSense = true;
        }
        else {
            leftSense = false;
        }

        //Right
        if (lineR.getValue() < 100 + lineValueR || lineR.getValue() > lineValueR - 100) {
            rightSense = true;
        }
        else {
            rightSense = false;
        }

        //Center
        if (lineC.getValue() < 100 + lineValueC || lineC.getValue() > lineValueC - 100) {
            centerSense = true;
        }
        else {
            centerSense = false;
        }
    }
}
