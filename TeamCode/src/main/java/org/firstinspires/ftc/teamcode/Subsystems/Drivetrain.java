package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.robot.Robot;

public class Drivetrain {


    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;

    private enum DriveState{
        MANUAL,
        HOLD,
        FOLLOW;
    }
    public Drivetrain(Robot robot){

        frontLeft = robot.hardwareMap.get(DcMotorEx.class,"frontLeft");
        frontRight = robot.hardwareMap.get(DcMotorEx.class,"fronLeft");
        backLeft = robot.hardwareMap.get(DcMotorEx.class,"fronLeft");
        backRight = robot.hardwareMap.get(DcMotorEx.class,"fronLeft");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

}
