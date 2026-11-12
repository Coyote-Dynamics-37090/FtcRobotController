package org.firstinspires.ftc.teamcode.Subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.util.Alliance;

@Configurable
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

    public static double Kp = 1.5;
    public static double Kd = 0;
    //coefficients for absolute heading control
    double lastError;
    double currentError;
    double lastTime;
    double currentTime;
    ElapsedTime time;

    double headingError;
    double targetHeading;
    double anglularVelo;
    double currentHeading;

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
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );

        IMU.Parameters parameters = new IMU.Parameters(orientation);
        imu.initialize(parameters);

        headingError = 0;
        lastError = 0;
        lastTime = 0;
        currentError = 0;
        currentTime = 0;
        targetHeading = 0;
        anglularVelo = 0;
        currentHeading = 0;
        time = new ElapsedTime();
        time.reset();


        frontLeft = robot.hardwareMap.get(DcMotorEx.class,"frontLeft");
        frontRight = robot.hardwareMap.get(DcMotorEx.class,"frontRight");
        backLeft = robot.hardwareMap.get(DcMotorEx.class,"backLeft");
        backRight = robot.hardwareMap.get(DcMotorEx.class,"backRight");
        //hardware init
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //motor behavior
    }


    private void updateHeadingVelocity(){
        currentHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        anglularVelo = imu.getRobotAngularVelocity(AngleUnit.RADIANS).xRotationRate;
    }
    public double getHeading(){
        return currentHeading;
    }
    public double getAngularVelo(){
        return anglularVelo;
    }

    //TODO: use pinpoint with pedro instead of imu
    public void fieldCentric(){


        updateJoysticks();


        double turnPower=0;

        double currentHeading = getHeading();
        //follower.pose().heading();
        currentTime = time.time();


        if (Math.hypot(rx, ry) > 0.2) {
            //stick deadzone

            targetHeading = Math.atan2(rx, ry);
            //calculate stick angle
        }


            headingError = AngleUnit.normalizeRadians(targetHeading - currentHeading);

            //calculate angle wrapping by normalizing head on [-pi, pi]



            double D = Kd * getAngularVelo();
            double P = headingError * Kp;

            turnPower = P + D;



        lastError = headingError;
        lastTime = currentTime;

        double rotx = x * Math.cos(-currentHeading) - y * Math.sin(-currentHeading);
        double roty = x * Math.sin(-currentHeading) + y * Math.cos(-currentHeading);

        rotx = rotx * 1.1;

        double denominator = Math.max(Math.abs(roty) + Math.abs(rotx) + Math.abs(turnPower), 1);
        //normalize motor power


        double frontLeftPower = (roty + rotx + turnPower) / denominator;
        double backLeftPower = (roty - rotx + turnPower) / denominator;
        double frontRightPower = (roty - rotx - turnPower) / denominator;
        double backRightPower = (roty + rotx - turnPower) / denominator;
        //calc power

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
        //apply power
    }

    public void setDriveState(DriveState state){
        driveState = state;
    }


    //TODO: fix ts
    public Pose getPose() {
        //return currentPose;
        return null;
    }

    //TODO: fix ts aswell
    public void setPose(Pose pose) {
       // follower.setPose(pose);
    }


    public void updateJoysticks(){
        rx = gamepad.right_stick_x;
        ry = -gamepad.right_stick_y;
        //field centric

        x = gamepad.left_stick_x;
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
        telemetry.addData("backR",backRight.getPower());
        telemetry.addData("time",time.time());
        telemetry.update();
    }
    public Command periodic(){

        return infinite(() -> {

            updateJoysticks();
            updateHeadingVelocity();
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
