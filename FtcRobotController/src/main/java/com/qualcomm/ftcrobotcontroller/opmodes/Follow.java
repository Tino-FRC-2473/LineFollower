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
    DcMotor br, bl, fr, fl;
    AnalogInput l, c, r;
    GyroSensor spin;

    final static int ENC_COUNTS = 1120;
    final static double G_RATIO = 1;
    final static double CIRCUMFERENCE = Math.PI * 3.75;



    /*Data creation: other data*/
    int analog_r, analog_c, analog_l; //line values for each line sensor...setting a threshold
    int[] encoders; //encoder values for positional safekeeping when only center is on the line
    int[] encoders_two; //encoder values for positional safekeeping when the center AND another sensor are on the line
    double m = 0.3; //distance between each line sensor(they are equidistant) --> THIS VALUE IS MEASURED IN INCHES, STILL NEEDS TO BE MEASURED
    double motor_power, turn_power; //powers for turns and going straight
    double angle; //locally stored angle value

    @Override
    public void init() {

        //map hardware
        fr = hardwareMap.dcMotor.get("A");
        fl = hardwareMap.dcMotor.get("B");
        br = hardwareMap.dcMotor.get("C");
        bl = hardwareMap.dcMotor.get("D");
        l = hardwareMap.analogInput.get("L");
        c = hardwareMap.analogInput.get("CE");
        r = hardwareMap.analogInput.get("R");
        spin = hardwareMap.gyroSensor.get("spin");

        //configure motors for everything to go in the same direction
        br.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.REVERSE);

        //reset encoders
        br.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        bl.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        //locally store value of tape
        analog_r = r.getValue();
        analog_c = c.getValue();
        analog_l = l.getValue();

        //set values, etc.
        encoders = new int[2];
        motor_power = 0.15;
        turn_power = 0.5;

        //print data
        telemetry.addData("Left Calibration: ", analog_l);
        telemetry.addData("Right Calibration: ", analog_r);
        telemetry.addData("Center Calibraion: ", analog_c);

        //callibrate gyro sensor
        spin.calibrate();
        while (spin.isCalibrating()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //locally store gyro value for later use
        angle = spin.getHeading();
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

        boolean center = analogEqualsLine(c, analog_c);
        boolean right = analogEqualsLine(r, analog_r);
        boolean left = analogEqualsLine(l, analog_l);

        if(center && !right && !left) { //if only center
            forward(); //run straight
        } else if(center && right && !left) { //if center and right
            turnRight(90);
        } else if(center && !right && left) {
            turnLeft(90);
        } else if(!center && right && !left) {

        } else if(!center && !right && left) {

        } else {
            halt();
        }

        telemetry.addData("State: ", state());
        telemetry.addData("Gyro Heading", spin.getHeading());
        telemetry.addData("Local Angle Value", angle);
        telemetry.addData("Left Value: ", l.getValue());
        telemetry.addData("Right Value: ", r.getValue());
        telemetry.addData("Center Value: ", c.getValue());
        telemetry.addData("Left Difference: ", l.getValue() - analog_l);
        telemetry.addData("Right Difference: ", r.getValue() - analog_r);
        telemetry.addData("Center Difference: ", c.getValue() - analog_c);
    }

    @Override
    public void stop() {

    }

    void forward() {
        setPowerAll(motor_power);
    }

    void halt() {
        setPowerAll(0);
    }

    void turnLeft(int deg) {
        deg = 360 - deg;
        if(spin.getHeading() == 0 || spin.getHeading() > deg) {
            fr.setPower(turn_power);
            br.setPower(turn_power);
            fl.setPower(-turn_power);
            bl.setPower(-turn_power);
        }
    }

    void turnRight(int deg) {
        deg += spin.getHeading();
        if(deg >= 360) {
            deg -= 360;
        }

        telemetry.addData("degree to turn", deg);

        if(spin.getHeading() < deg) {
            fr.setPower(-turn_power);
            fl.setPower(turn_power);
            br.setPower(turn_power);
            bl.setPower(-turn_power);
        }
    }

    double setTargetAngle(double deg, String direction) {
        double returner = 0;

        double val = angle - deg;

        if(direction.equals("left")) {
            if(val < 0) {
                
            }
        } else {

        }

        return returner;
    }

    void setPowerAll(double val) {
        br.setPower(val);
        bl.setPower(val);
    }

    //following method is deprecated...will not be using
    public int state() {
        boolean center = analogEqualsLine(c, analog_c);
        boolean right = analogEqualsLine(r, analog_r);
        boolean left = analogEqualsLine(l, analog_l);

        //WARNING: THIS IS BAD CODE
        if(center && !right && !left) {
            return 0;
        } else if(center && right && !left) {
            return 3;
        } else if(center && !right && left) {
            return 4;
        } else if(!center && right && !left) {
            return 1;
        } else if(!center && !right && left) {
            return 2;
        } else {
            return -1;
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
        //distance is in inches
        d_clicks = (d_clicks * CIRCUMFERENCE)/(ENC_COUNTS * G_RATIO);
        return d_clicks;
    }
}