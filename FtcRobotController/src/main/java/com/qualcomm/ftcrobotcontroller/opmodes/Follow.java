package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class Follow extends OpMode {
    /*Data creation: devices*/
    DcMotor br, bl, fr, fl;
    AnalogInput l, c, r;

    boolean center;
    boolean left;
    boolean right;

    double currentTime;

    /*Data creation: other data*/
    int analog_r, analog_c, analog_l; //line values for each line sensor...setting a threshold
    int[] encoders; //encoder values for positional safekeeping when only center is on the line
    double motor_power, turn_power; //powers for turns and going straight
    double angle; //locally stored angle value
    boolean testing_turn = false;
    String position = "";

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

        //configure motors for everything to go in the same direction
        bl.setDirection(DcMotor.Direction.REVERSE);
        fl.setDirection(DcMotor.Direction.REVERSE);

        //locally store value of tape
        analog_r = r.getValue();
        analog_c = c.getValue();
        analog_l = l.getValue();

        motor_power = 0.075;
        turn_power = 0.2;

        //print data
        telemetry.addData("Left Calibration: ", analog_l);
        telemetry.addData("Right Calibration: ", analog_r);
        telemetry.addData("Center Calibraion: ", analog_c);
    }

    @Override
    public void loop() {
        center = analogEqualsLine(c, analog_c);
        right = analogEqualsLine(r, analog_r);
        left = analogEqualsLine(l, analog_l);

        if(center && !right && !left) { //if only center
            position = "center only";
            forward(); //run straight
        } else if(right && !left) { //if right hit at all, but not left
            position = "center and right";
            turnRightToCenter();
        } else if(!right && left) { //if left hit at all
            position = "center and left";
            turnLeftToCenter();
        }
        else {
            position = "all";
            currentTime = this.time;
            if (this.time - currentTime < 5) {
                forward();
            }
//            checkForEnd();
        }
    }

    @Override
    public void stop() {

    }

    void forward() {
        setPowerAll(motor_power);
    }

    void turnRightToCenter() {
        if (!(center && !right && !left)) {
            fr.setPower(-turn_power);
            fl.setPower(turn_power);
            br.setPower(-turn_power);
            bl.setPower(turn_power);
        }
    }

    void turnLeftToCenter() {
        if (!(center && !right && !left)) {
            fr.setPower(turn_power);
            fl.setPower(-turn_power);
            br.setPower(turn_power);
            bl.setPower(-turn_power);
        }
    }

    void setPowerAll(double val) {
        br.setPower(val);
        bl.setPower(val);
        fr.setPower(val);
        fl.setPower(val);
    }

//    void checkForEnd() {
//        if (position.equals("all")) {
//            stop();
//        }
//        else {
//            forward();
//        }
//    }

    boolean analogEqualsLine(AnalogInput i, int val) {
        return Math.abs(i.getValue() - val) <= 100;
    }
 }