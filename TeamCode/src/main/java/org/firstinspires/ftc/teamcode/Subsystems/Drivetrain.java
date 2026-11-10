package org.firstinspires.ftc.teamcode.Subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.util.Alliance;

public class Drivetrain {


    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;

    IMU imu;
   // public final Follower follower;
    public final Gamepad gamepad;
    public final Telemetry telemetry;

    public double x, y, rx, ry = 0;

    private double currentPose = 0;

    double Kp = 1.5;
    double Kd = 0;
    //coefficients for absolute heading control



    public enum DriveState{
        MANUAL,
        HOLD,
        FOLLOW
    }


    private DriveState driveState = DriveState.MANUAL;


    public Drivetrain(Robot robot, Gamepad gamepad, Telemetry telemetry){

        this.gamepad = gamepad;
        //pass in gamepad from robot

        this.telemetry = telemetry;

       // follower = Constants.create(robot.hardwareMap);

        imu = robot.hardwareMap.get(IMU.class,"imu");

        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );

        IMU.Parameters parameters = new IMU.Parameters(orientation);
        imu.initialize(parameters);

        frontLeft = robot.hardwareMap.get(DcMotorEx.class,"frontLeft");
        frontRight = robot.hardwareMap.get(DcMotorEx.class,"frontRight");
        backLeft = robot.hardwareMap.get(DcMotorEx.class,"backLeft");
        backRight = robot.hardwareMap.get(DcMotorEx.class,"backRight");
        //hardware init
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //motor behavior
    }


    public double getHeading(){
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }
    public void fieldCentric(){
        updateJoysticks();

        double turnPower=0;

        double currentHeading = getHeading();
        //follower.pose().heading();

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

        double denominator = Math.max(Math.abs(roty) + Math.abs(rotx) + Math.abs(turnPower), 1);
        //normalize motor power


        double frontLeftPower = (roty + rotx + rx) / denominator;
        double backLeftPower = (roty - rotx + rx) / denominator;
        double frontRightPower = (roty - rotx - rx) / denominator;
        double backRightPower = (roty + rotx - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    public void setDriveState(DriveState state){
        driveState = state;
    }


    public Pose getPose() {
        //return currentPose;
        return null;
    }

    public void setPose(Pose pose) {
       // follower.setPose(pose);
    }


    public void updateJoysticks(){
        rx = -gamepad.right_stick_x;
        ry = -gamepad.right_stick_y;
        //field centric

        x = -gamepad.left_stick_x;
        y = -gamepad.left_stick_y;
        //absolute heading
    }


    public void reHeading(){
        imu.resetYaw();
    }
/*
    public void followLeft(){


        if(Alliance.Alliance == Alliance.alliance.BLUE){
            follower.follow();
        } else {

        }
    }
*/

    public void init(){
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }


    public void telemetry(){
        telemetry.addData("x", x);
        telemetry.addData("y",y);
        telemetry.addData("ry", ry);
        telemetry.addData("rx",rx);
        telemetry.addData("heading",getHeading());
        telemetry.addData("frontL",frontLeft.getPower());
        telemetry.addData("frontR",frontRight.getPower());
        telemetry.addData("backL",backLeft.getPower());
        telemetry.addData("frontL",frontLeft.getPower());
        telemetry.update();
    }
    public Command periodic(){
        return infinite(() -> {

            updateJoysticks();
            boolean joysticksActive = (Math.abs(y) > 0.05 || Math.abs(x) > 0.05 || Math.abs(rx) > 0.05 || Math.abs(ry) > 0.05);

            //follower.update();




            switch(driveState){
                case HOLD:
                    if(joysticksActive){
                       // follower.stop();
                        setDriveState(DriveState.MANUAL);
                    }
                    break;

                case MANUAL:
                    fieldCentric();
                    if(!joysticksActive){
                      //  follower.hold(currentPose);
                      //  setDriveState(DriveState.HOLD);
                    }
                    break;
                case FOLLOW:

                  //  if(!follower.isBusy()){
                  //      follower.hold(currentPose);
                  //      setDriveState(DriveState.HOLD);
                 //   }
                    //check if path is finished

                    if(joysticksActive){
                       // follower.stop();
                        setDriveState(DriveState.MANUAL);
                    }
                    //check for joystick interference
                    break;
                default: setDriveState(DriveState.MANUAL);
            }


        });





    }
}
