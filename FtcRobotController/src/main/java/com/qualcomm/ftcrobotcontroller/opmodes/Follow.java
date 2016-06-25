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
    int[] encoders; //encoder values for positional safekeeping
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

        encoders = new int[2];
    }

/*
* State Legend:
* c: 0
* r: 1
* l: 2
* c/r: 3
* c/l: 4
* else: 100
* */

    @Override
    public void loop() {
        switch(state()) {
            case 0: //c
                break;
            case 1: //r
                break;
            case 2: //l
                break;
            case 3: //c,r
                break;
            case 4: //c,l
                break;
            default: //remaining: none, all
                break;
        }
    }

    public int state() {
        if(analogEqualsLine(c, analog_c)) {
            if(analogEqualsLine(r, analog_r)) {
                return 3;
            } else if(analogEqualsLine(l, analog_l)) {
                return 4;
            }
            return 0;
        } else {
            if(analogEqualsLine(r, analog_r)) {
                return 1;
            } else if(analogEqualsLine(l, analog_l)) {
                return 2;
            } else {
                return 100;
            }
        }
    }

    boolean analogEqualsLine(AnalogInput i, int val) {
        return Math.abs(i.getValue() - val) <= 100;
    }

    void refreshEncoderValues() {
        assignEncoderValues(0,0);
    }

    void assignEncoderValues(int val1, int val2) {
        encoders[0] = val1;
        encoders[1] = val2;
    }
}