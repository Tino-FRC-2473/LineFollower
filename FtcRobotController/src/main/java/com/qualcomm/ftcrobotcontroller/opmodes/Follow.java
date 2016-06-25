package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by sethideep23 on 6/24/16.
 */
public class Follow extends OpMode {
    /*Data creation: devices*/
    DcMotor fr, fl, br, bl;
    AnalogInput l, c, r;
    GyroSensor spin;

    /*Data creation: other data*/
    int analog_r, analog_c, analog_l; //line values for each line sensor...setting a threshold
    double[] encoders; //encoder values for positional safekeeping
    double m = 1.0; //distance between each line sensor(they are equidistant)

    @Override
    public void init() {
        fr = hardwareMap.dcMotor.get("FR");
        fl = hardwareMap.dcMotor.get("FL");
        br = hardwareMap.dcMotor.get("BR");
        bl = hardwareMap.dcMotor.get("BL");
        l = hardwareMap.analogInput.get("L");
        c = hardwareMap.analogInput.get("C");
        r = hardwareMap.analogInput.get("R");
        spin = hardwareMap.gyroSensor.get("spin");

        analog_r = r.getValue();
        analog_c = c.getValue();
        analog_l = l.getValue();

        encoders = new double[2];
        encoders[0] = 0.0;
        encoders[1] = 0.0;
    }

    @Override
    public void loop() {

    }
}