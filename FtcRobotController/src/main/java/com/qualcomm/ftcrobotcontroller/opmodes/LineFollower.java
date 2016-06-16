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
public class LineFollower extends LinearOpMode {
    DcMotor A;
    DcMotor B;
    DcMotor C;
    DcMotor D;
    Servo L;
    AnalogInput line;

    @Override
    public void runOpMode() throws InterruptedException {
        hardwareMap.logDevices();
        line = hardwareMap.analogInput.get("LI");
        A = hardwareMap.dcMotor.get("A");
        B = hardwareMap.dcMotor.get("B");
        C = hardwareMap.dcMotor.get("C");
        D = hardwareMap.dcMotor.get("D");
        L = hardwareMap.servo.get("L");

        waitOneFullHardwareCycle();

        waitForStart();

        telemetry.addData("Line Output", line.getValue());
    }
}
