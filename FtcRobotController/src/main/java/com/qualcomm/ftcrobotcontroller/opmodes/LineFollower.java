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
    DcMotor A;
    DcMotor B;
    DcMotor C;
    DcMotor D;
    AnalogInput lineL;
    AnalogInput lineC;
    AnalogInput lineR;

    @Override
    public void init() {
        hardwareMap.logDevices();
        lineL = hardwareMap.analogInput.get("A");
        lineC = hardwareMap.analogInput.get("B");
        lineR = hardwareMap.analogInput.get("C");
        A = hardwareMap.dcMotor.get("A");
        B = hardwareMap.dcMotor.get("B");
        C = hardwareMap.dcMotor.get("C");
        D = hardwareMap.dcMotor.get("D");
    }

    @Override
    public void loop() {


        telemetry.addData("Sensor Output Left", lineL.getValue());
        telemetry.addData("Sensor Output Center", lineC.getValue());
        telemetry.addData("Sensor Output Right", lineR.getValue());
    }
}
