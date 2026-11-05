package org.firstinspires.ftc.teamcode.Subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;

public class Drivetrain {


    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;
    public final Follower follower;
    public final Gamepad gamepad;

    double Kp = 1.5;
    double Kd = 0;
    //coefficients for absolute heading control


    private enum DriveState{
        MANUAL,
        HOLD,
        FOLLOW;
    }
    public Drivetrain(Robot robot, Gamepad gamepad){

        this.gamepad = gamepad;
        //pass in gamepad from robot

        follower = Constants.create(robot.hardwareMap);

        frontLeft = robot.hardwareMap.get(DcMotorEx.class,"frontLeft");
        frontRight = robot.hardwareMap.get(DcMotorEx.class,"fronLeft");
        backLeft = robot.hardwareMap.get(DcMotorEx.class,"fronLeft");
        backRight = robot.hardwareMap.get(DcMotorEx.class,"fronLeft");
        //hardware init
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //motor behavior
    }

    public void fieldCentric(){

        double turnPower=0;

        double x = gamepad.left_stick_y;
        double y = gamepad.left_stick_x;
        //field centric

        double rx = gamepad.right_stick_x;  //strafe
        double ry = gamepad.right_stick_y;  //forward
        //absolute heading



        double currentHeading = follower.pose().heading();

        if (Math.hypot(rx, ry) > 0.2) {

            double targetHeading = Math.atan2(ry, rx);
            //calculate stick angle


            double headingError = targetHeading - currentHeading;
            while (headingError > Math.PI)  headingError -= 2 * Math.PI;
            while (headingError < -Math.PI) headingError += 2 * Math.PI;
            //calculate angle wrapping


            turnPower = headingError * Kp;
        }




        double rotx = x * Math.cos(-currentHeading) - y * Math.sin(-currentHeading);
        double roty = x * Math.sin(-currentHeading) + y * Math.cos(-currentHeading);

        rotx = rotx * 1.1;

        double denominator = Math.max(Math.abs(roty) + Math.abs(rotx) + Math.abs(rx), 1);
        //normalize motor power

        
        double frontLeftPower = (roty + rotx + turnPower) / denominator;
        double backLeftPower = (roty - rotx + turnPower) / denominator;
        double frontRightPower = (roty - rotx - turnPower) / denominator;
        double backRightPower = (roty + rotx - turnPower) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }




}
