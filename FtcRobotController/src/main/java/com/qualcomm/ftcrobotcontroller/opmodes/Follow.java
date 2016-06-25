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
    DcMotor FR, FL, BR, BL;
    AnalogInput L, C, R;
    GyroSensor spin;

    /*Data creation: other data*/
    int analog_r, analog_c, analog_l; //sensor line values
    double[] encoders; //encoder values for positional safekeeping
    double m = 1.0; //distance between each line sensor(they are equidistant)

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
