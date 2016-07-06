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
    boolean testing_turn = false;
    String turn_type = "";
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
        spin = hardwareMap.gyroSensor.get("spin");

        //configure motors for everything to go in the same direction
        bl.setDirection(DcMotor.Direction.REVERSE);
        fl.setDirection(DcMotor.Direction.REVERSE);

        //reset encoders
        br.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        bl.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        //locally store value of tape
        analog_r = r.getValue();
        analog_c = c.getValue();
        analog_l = l.getValue();

        //set values, etc.
        encoders = new int[2];
        encoders_two = new int[2];
        motor_power = 0.1;
        turn_power = 0.25;

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
        angle = spin.getHeading(); //update angle
        br.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        bl.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        boolean center = analogEqualsLine(c, analog_c);
        boolean right = analogEqualsLine(r, analog_r);
        boolean left = analogEqualsLine(l, analog_l);

        if(center && !right && !left) { //if only center
            position = "center only";
            forward(); //run straight
            assignEncoderValues(bl.getCurrentPosition(), br.getCurrentPosition());
        } else if(center && right && !left) { //if center and right
            //save latest position at which "multiple sensors were "true"
            position = "center and right";
            assignEncoderValuesDouble(bl.getCurrentPosition(), br.getCurrentPosition());
            if(testing_turn == false) {
                testing_turn = true;
            } else {
                turnRight(90);
            }
//            if(turn.equals("90deg")) {
//                reverseToPositionDouble();
//                turnRight(90);
//            } else if(turn.equals("angle_down")) {
//                if(center && !right && !left) {
//                    forward();
//                    assignEncoderValues(bl.getCurrentPosition(), br.getCurrentPosition());
//                } else {
//                    halt();
//                }
//                double difference_left = bl.getCurrentPosition() - encoders_two[0];
//                double difference_right = br.getCurrentPosition() - encoders_two[1];
//                double difference = (difference_left + difference_right)/2; //get average of encoder values for most accurate distance measurement
//                double distance = distance(difference);
//                double turn_angle = Math.toDegrees(Math.atan(distance/m));
//                turnRight(180 - turn_angle);
//            } else {
//                halt();
//            }
        } else if(center && !right && left) { //if center and left
            position = "center and left";
            //save latest position at which "multiple sensors were "true"
//            assignEncoderValuesDouble(bl.getCurrentPosition(), br.getCurrentPosition());
//            String turn = determineTurnType();
//            if(turn.equals("90deg")) {
//                reverseToPositionDouble();
//                turnLeft(90);
//            } else if(turn.equals("angle_down")) {
//                if(center && !right && !left) {
//                    forward();
//                    assignEncoderValues(bl.getCurrentPosition(), br.getCurrentPosition());
//                } else {
//                    halt();
//                }
//                double difference_left = bl.getCurrentPosition() - encoders_two[0];
//                double difference_right = br.getCurrentPosition() - encoders_two[1];
//                double difference = (difference_left + difference_right)/2; //get average of encoder values for most accurate distance measurement
//                double distance = distance(difference);
//                double turn_angle = Math.toDegrees(Math.atan(distance/m));
//                turnLeft(180 - turn_angle);
//            } else {
//                halt();
//            }
        } else if(!center && right && !left) { //if right only
            position = "right only";
//            double difference_left = bl.getCurrentPosition() - encoders[0]; //left encoder difference
//            double difference_right = br.getCurrentPosition() - encoders[1]; //right encoder difference
//            double difference = (difference_left + difference_right)/2; //get average of encoder values for most accurate distance measurement
//            reverseToPosition(); //go back to position to turn
//            double distance = distance(difference);
//            double turn_angle = Math.toDegrees(Math.atan(distance/m));
//            turnRight(turn_angle);
//            halt();
        } else if(!center && !right && left) { //if left only
            position = "left only";
//            double difference_left = bl.getCurrentPosition() - encoders[0]; //left encoder difference
//            double difference_right = br.getCurrentPosition() - encoders[1]; //right encoder difference
//            double difference = (difference_left + difference_right)/2; //get average of encoder values for most accurate distance measurement
//            reverseToPosition(); //go back to position to turn
//            double distance = distance(difference);
//            double turn_angle = Math.toDegrees(Math.atan(distance/m));
//            turnLeft(turn_angle);
//            halt();
        } else {
            if(testing_turn) {
                telemetry.addData("status: ", "running...");
                reverseToPositionDouble();
            } else {
                halt();
                position = "weird af messed up stuff";
                //            double time = this.time;
                //            if(this.time - time < 5) {
                //                if(state() == -1) {
                //                    forward();
                //                } else {
                //                    halt();
                //                }
                //            }
            }
        }

        telemetry.addData("Turn_Test Switch", testing_turn);
        telemetry.addData("Turn Type", turn_type);
        telemetry.addData("Position", position);
        telemetry.addData("Left encoder", bl.getCurrentPosition());
        telemetry.addData("Right encoder", br.getCurrentPosition());
        telemetry.addData("Saved Single L", encoders[0]);
        telemetry.addData("Saved Single R", encoders[1]);
        telemetry.addData("Saved Double L", encoders_two[0]);
        telemetry.addData("Saved Double R", encoders_two[1]);
//        telemetry.addData("Gyro Heading", spin.getHeading());
//        telemetry.addData("Local Angle Value", angle);

//        telemetry.addData("Left Difference: ", l.getValue() - analog_l);
//        telemetry.addData("Right Difference: ", r.getValue() - analog_r);
//        telemetry.addData("Center Difference: ", c.getValue() - analog_c);
    }

    @Override
    public void stop() {

    }

    void forward() {
        setPowerAll(motor_power);
    }

    String determineTurnType() {
        testing_turn = true;
        String returner = "";
        if(state() == 3 || state() == 4) {
            telemetry.addData("State", state());
        } else {
            if(state() == -1) {
                halt();
                returner = "90deg";
            } else if(state() == 0) {
                halt();
                returner = "angle_down";
            } else if(state() == 1 || state() == 2) {
                halt();
                returner = "angle_up";
            } else {
                halt();
                returner = "stop(ERROR)";
            }
        }
        return returner;
    }

    void halt() {
        setPowerAll(0);
    }

    void turnLeft(double deg) {
        deg = setTargetAngle(deg, "left");

        if(spin.getHeading() != deg) {
            fr.setPower(turn_power);
            br.setPower(turn_power);
            fl.setPower(-turn_power);
            bl.setPower(-turn_power);
        }
    }

    void turnRight(double deg) {
        deg = setTargetAngle(deg, "right");

        telemetry.addData("degree to turn", deg);

        if(spin.getHeading() != deg) {
            fr.setPower(-turn_power);
            fl.setPower(turn_power);
            br.setPower(turn_power);
            bl.setPower(-turn_power);
        } else {
            testing_turn = false;
        }
    }
b
    //confirmed
    double setTargetAngle(double deg, String direction) {
        double returner = 0;


        if(direction.equals("left")) {
            double difference = angle - deg;
            if(difference < 0) {
                returner = 360 + difference;
            } else {
                returner = difference;
            }
        } else {
            double sum = angle + deg;
            if(sum > 359) {
                returner = sum - 360;
            } else {
                returner = sum;
            }
        }

        return returner;
    }

    void forwardForClicks(double clicks) {
        refreshEncoderValuesDouble();
        if(br.getCurrentPosition() != clicks && bl.getCurrentPosition() != clicks) {
            forward();
        } else {
            halt();
        }
    }

    void setPowerAll(double val) {
        br.setPower(val);
        bl.setPower(val);
        fr.setPower(val);
        fl.setPower(val);
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
        if(br.getCurrentPosition() != 0 && bl.getCurrentPosition() != 0) {
            setPowerAll(-motor_power);
        }
    }

    void reverseToPositionDouble() {
        boolean center = analogEqualsLine(c, analog_c);
        boolean right = analogEqualsLine(r, analog_r);
        boolean left = analogEqualsLine(l, analog_l);

        if(!(encoders_two[0] - bl.getCurrentPosition() <= 10 && encoders_two[0] - bl.getCurrentPosition() >= -10) && !(encoders_two[1] - br.getCurrentPosition() <= 10 && encoders_two[1] - br.getCurrentPosition() >= -10)) {
            setPowerAll(-motor_power);
        } else if(position.equals("center and right")) {
            telemetry.addData("Yay!", "Works!!!");
            halt();
//            testing_turn = false;
        } else {
            telemetry.addData("Yay!", "Works!!!");
            halt();
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