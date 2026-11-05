package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.Robot;
import static com.pedropathing.ivy.commands.Commands.*;

public class Intake {

    public final DcMotor intakeLeft;
    public final DcMotor intakeRight;
    //intake motor direction is relative to the control hub
    private final double INTAKE_POWER = 1;
    private final double OUTAKE_POWER = -1;

    public enum State{
        OFF,
        INTAKE,
        OUTAKE
    }

    private State state = State.OFF;




    public Intake(Robot robot){
        intakeLeft = robot.hardwareMap.get(DcMotor.class,"intakeLeft");
        intakeRight = robot.hardwareMap.get(DcMotor.class,"intakeRight");
    }


    public Command intake(){
        return instant(() -> state = State.INTAKE);
    }

    public Command outake(){
        return instant(() -> state = State.OUTAKE);
    }

    public Command off(){
        return instant(() -> state = State.OFF);
    }
}
