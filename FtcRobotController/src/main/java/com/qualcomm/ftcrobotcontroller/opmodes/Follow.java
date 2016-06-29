package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by sethideep23 on 6/24/16.
 */
public class Follow extends OpMode {
    /*Data creation: devices*/
    DcMotor br, bl;
    AnalogInput l, c, r;
    GyroSensor spin;

    /*Data creation: other data*/
    int analog_r, analog_c, analog_l; //line values for each line sensor...setting a threshold
    int[] encoders; //encoder values for positional safekeeping when only center is on the line
    int[] encoders_two; //encoder values for positional safekeeping when the center AND another sensor are on the line
    double m = 1.0; //distance between each line sensor(they are equidistant)
    double motor_power;
    boolean doubleHit = false; //triggered true if cases 3 or 4 have been activated

    @Override
    public void init() {
        br = hardwareMap.dcMotor.get("C");
        bl = hardwareMap.dcMotor.get("D");

        bl.setDirection(DcMotor.Direction.REVERSE);

        l = hardwareMap.analogInput.get("L");
        c = hardwareMap.analogInput.get("CE");
        r = hardwareMap.analogInput.get("R");
        spin = hardwareMap.gyroSensor.get("spin");

        br.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        bl.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        analog_r = r.getValue();
        analog_c = c.getValue();
        analog_l = l.getValue();

        encoders = new int[2];
        motor_power = 0.15;
    }

/*
* State Legend:
* c: 0
* r: 1
* l: 2
* c/r: 3
* c/l: 4
* else: -1
* */

    @Override
    public void loop() {

        br.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        bl.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        switch(state()) {
            case 0:
                forward();
                assignEncoderValues(bl.getCurrentPosition(), br.getCurrentPosition());
                break;
            case 1:
                int b_l = bl.getCurrentPosition(); //initial position
                int b_r = br.getCurrentPosition(); //initial position
                while(state() != 0 || state() != -1) {
                    forward();
                    assignEncoderValuesDouble(bl.getCurrentPosition(), br.getCurrentPosition());
                }

                double d_clicks = ((encoders_two[0] - b_l) + (encoders_two[1] - b_r))/2;

                if(state() == 0) {

                } else if(state() == -1) {

                }

                break;
            case 2:
                break;
            case 3:
                forward();
                assignEncoderValuesDouble(bl.getCurrentPosition(), br.getCurrentPosition());
                if(state() == -1) {
                    halt();
                    reverseToPositionDouble();
                    turnRight(90);
                    refreshEncoderValuesDouble();
                }
                break;
            case 4:
                forward();
                assignEncoderValuesDouble(bl.getCurrentPosition(), br.getCurrentPosition());
                if(state() == -1) {
                    halt();
                    reverseToPositionDouble();
                    turnLeft(90);
                    refreshEncoderValuesDouble();
                }
                break;
            case -1:
                halt();
                break;
        }

        telemetry.addData("State: ", state());
    }



    void forward() {
        setPowerAll(motor_power);
    }

    void halt() {
        setPowerAll(0.0);
    }

    void turnLeft(int deg) {
        deg = 360 - deg;
        while(spin.getHeading() != deg) {
            br.setPower(-motor_power);
            bl.setPower(-motor_power);
        }
        spin.calibrate();
    }

    void turnRight(int deg) {
        while(spin.getHeading() != deg) {
            br.setPower(motor_power);
            bl.setPower(motor_power);
        }
        spin.calibrate();
    }

    void setPowerAll(double val) {
        br.setPower(val + 0.1);
        bl.setPower(val + 0.1);
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
                return -1;
            }
        }
    }

    boolean analogEqualsLine(AnalogInput i, int val) {
        return Math.abs(i.getValue() - val) <= 100;
    }

    void refreshEncoderValues() {
        assignEncoderValues(0,0);
        br.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        bl.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    void reverseToPosition() {
        while(br.getCurrentPosition() != 0 && bl.getCurrentPosition() != 0) {
            setPowerAll(-motor_power);
        }
    }

    void reverseToPositionDouble() {
        while(br.getCurrentPosition() != encoders_two[1] && bl.getCurrentPosition() != encoders_two[1]) {
            setPowerAll(-motor_power);
        }
    }

    void assignEncoderValues(int val1, int val2) {
        encoders[0] = val1;
        encoders[1] = val2;
    }

    void assignEncoderValuesDouble(int val1, int val2) {
        encoders_two[0] = val1;
        encoders_two[1] = val2;
    }

    void refreshEncoderValuesDouble() {
        assignEncoderValuesDouble(0,0);
        refreshEncoderValues(); //uncertain
    }

    double distance(double d_clicks) {
        //Kashyap's code here...
        return d_clicks;
    }
}