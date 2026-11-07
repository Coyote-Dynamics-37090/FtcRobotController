package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.robot.Robot;
import static com.pedropathing.ivy.commands.Commands.*;

public class Shooter {



    public final DcMotorEx shooterRed;
    public final DcMotorEx shooterBlack;
    //motor definitions

    public final static double  kP = 0;
    public final static double  kV = 0;
    public final static double  kS = 0;
    //flywheel PID coefficients
    private double flywheelVelocity = 0;
    private  double flywheelTarget = 0;
    //flywheel target and velocity

    private  double errorTolerance = 10;
    //maximum error tolerance for flywheels




    public Shooter(Robot robot){
        shooterRed = robot.hardwareMap.get(DcMotorEx.class,"shooterRed");
        shooterBlack = robot.hardwareMap.get(DcMotorEx.class,"shooterBlack");
        //initialize motors

        shooterRed.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRed.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        //set motor zero power behaviors
    }

    public double getErrorTolerance(){
        return errorTolerance;
    }
    public void setErrorTolerance(double tolerance){
        errorTolerance = tolerance;
    }
    //getters and setters for error tolerance
    public double getFlywheelVelocity(){
        return flywheelVelocity;
    }
    public void setFlywheelVelocity(double speed){
        flywheelVelocity = speed;
    }
    //flywheel velocity getters/ setters
    public void updateFlywheelVelocity(){
        setFlywheelVelocity(
                ( shooterBlack.getVelocity() + shooterRed.getVelocity() ) /2
                );
    }
    //update flywheel velocity as average velocity of both motors

    public double getFlywheelTarget(){
        return  flywheelTarget;
    }
    public void setFlywheelTarget(double target){
        flywheelTarget = target;
    }
    //flywheel target getters/setters

    public boolean isUpToSpeed(){
        return getFlywheelVelocity() >= getFlywheelTarget() - getErrorTolerance();
    }

    public void updatePID(){
        updateFlywheelVelocity();
        double currentVelocity =getFlywheelVelocity();

        double power = ((kP*(flywheelTarget-currentVelocity))+(kV*flywheelTarget)+(kS*Math.signum(flywheelTarget)));
        shooterRed.setPower(power);
        shooterBlack.setPower(power);
    }



    public Command periodic(){
        return infinite(() -> {

            updatePID();
        });

    }
}
