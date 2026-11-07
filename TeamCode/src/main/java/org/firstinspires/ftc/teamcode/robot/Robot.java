package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Shooter;

public class Robot {

    public final HardwareMap hardwareMap;
    public final Gamepad gamepad;
    public Drivetrain drivetrain;
    public Intake intake;
    public Shooter shooter;

    //declare subsystems

    public Robot(HardwareMap hardwareMap, Gamepad gamepad){
        this.hardwareMap = hardwareMap;
        this.gamepad = gamepad;

        // pass in hardware map and gamepad from opmode
        drivetrain = new Drivetrain(this, gamepad);
        intake = new Intake(this);
        shooter = new Shooter(this);
        //initialize subsystems

    }

    public void init(){
        drivetrain.init();
        intake.init();
        shooter.init();
    }
    //activate initial states of all subsystems

    public void periodic(){
        drivetrain.periodic().schedule();
        intake.periodic().schedule();
        shooter.periodic().schedule();
    }

}
