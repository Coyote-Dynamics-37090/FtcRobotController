package org.firstinspires.ftc.teamcode.Subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.util.Alliance;

public class Drivetrain {


    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;
    public final Follower follower;
    public final Gamepad gamepad;

    public double x, y, rx, ry = 0;

    private Pose currentPose = new Pose(0,0,0);

    double Kp = 1.5;
    double Kd = 0;
    //coefficients for absolute heading control



    private enum DriveState{
        MANUAL,
        HOLD,
        FOLLOW;
    }


    private DriveState driveState = DriveState.HOLD;


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
        updateJoysticks();

        double turnPower=0;

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

    public void setDriveState(DriveState state){
        driveState = state;
    }


    public Pose getPose() {
        return currentPose;
    }

    public void setPose(Pose pose) {
        follower.setPose(pose);
    }


    public void updateJoysticks(){
        x = gamepad.left_stick_y;
        y = gamepad.left_stick_x;
        //field centric

        rx = gamepad.right_stick_x;
        ry = gamepad.right_stick_y;
        //absolute heading
    }

/*
    public void followLeft(){


        if(Alliance.Alliance == Alliance.alliance.BLUE){
            follower.follow();
        } else {

        }
    }
*/
    public Command periodic(){
        return infinite(() -> {

            boolean joysticksActive = (Math.abs(y) > 0.05 || Math.abs(x) > 0.05 || Math.abs(rx) > 0.05 || Math.abs(ry) > 0.05);

            follower.update();

            currentPose = follower.pose();


            switch(driveState){
                case HOLD:
                    if(joysticksActive){
                        follower.stop();
                        setDriveState(DriveState.MANUAL);
                    }
                    break;

                case MANUAL:
                    fieldCentric();
                    if(!joysticksActive){
                        follower.hold(currentPose);
                        setDriveState(DriveState.HOLD);
                    }
                    break;
                case FOLLOW:

                    if(!follower.isBusy()){
                        follower.hold(currentPose);
                        setDriveState(DriveState.HOLD);
                    }
                    //check if path is finished

                    if(joysticksActive){
                        follower.stop();
                        setDriveState(DriveState.MANUAL);
                    }
                    //check for joystick interference
                    break;
            }


        });




    }
}
