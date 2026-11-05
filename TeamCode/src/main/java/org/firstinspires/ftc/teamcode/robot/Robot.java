package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;

public class Robot {

    public final HardwareMap hardwareMap;
    public final Gamepad gamepad;
    public Drivetrain drivetrain;

    //declare subsystems

    public Robot(HardwareMap hardwareMap, Gamepad gamepad){
        this.hardwareMap = hardwareMap;
        this.gamepad = gamepad;

        // pass in hardware map and gamepad from opmode
        drivetrain = new Drivetrain(this, gamepad);



        //intialize subsystems

    }
}
