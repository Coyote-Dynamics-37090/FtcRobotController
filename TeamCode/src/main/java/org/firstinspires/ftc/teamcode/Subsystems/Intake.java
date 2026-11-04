package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.Robot;
import static com.pedropathing.ivy.commands.Commands.*;

public class Intake {

    public final DcMotor intakeLeft;
    public final DcMotor intakeRight;
    //Intake motor direction is relative to the control hub
    private final double INTAKE_POWER = 1;
    private final double OUTAKE_POWER = -1;

    public enum State{
        INTAKE,
        OUTAKE
    }





    public Intake(Robot robot){
        intakeLeft = robot.hardwareMap.get(DcMotor.class,"intakeLeft");
        intakeRight = robot.hardwareMap.get(DcMotor.class,"intakeRight");
    }




}
