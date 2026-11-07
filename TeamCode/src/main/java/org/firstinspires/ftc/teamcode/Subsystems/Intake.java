package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.Robot;
import static com.pedropathing.ivy.commands.Commands.*;

public class Intake {

    public final DcMotor intakeB;
    public final DcMotor intakeR;

    private final double INTAKE_POWER = 1;
    private final double OUTAKE_POWER = -1;
    private boolean stateChanged = false;

    public enum State{
        OFF,
        INTAKE,
        OUTAKE
    }

    private State state = State.OFF;

    //TODO: add switcher and make flat when normal intaking, one side when intake is running



    public Intake(Robot robot){
        intakeB = robot.hardwareMap.get(DcMotor.class,"intakeB");
        intakeR = robot.hardwareMap.get(DcMotor.class,"intakeR");
    }


    public Command intake(){
        stateChanged = true;
        return instant(() -> state = State.INTAKE);
    }

    public Command outake(){
        stateChanged = true;
        return instant(() -> state = State.OUTAKE);
    }

    public Command off(){
        stateChanged = true;
        return instant(() -> state = State.OFF);
    }

    public void init(){
        intakeB.setPower(0);
        intakeR.setPower(0);
    }

    public Command periodic(){
        return infinite(() -> {
            switch (state){
                case OFF:
                    if(stateChanged){
                        stateChanged = false;
                        off().schedule();
                    }
                    break;
                case INTAKE:
                    if(stateChanged){
                        stateChanged = false;
                        intake().schedule();
                    }
                    break;
                case OUTAKE:
                    if(stateChanged){
                        stateChanged = false;
                        outake().schedule();
                    }
                    break;

            }

        });

    }
}
