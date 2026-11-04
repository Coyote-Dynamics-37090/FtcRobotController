package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;

public class Robot {

    public final HardwareMap hardwareMap;
    public Drivetrain drivetrain;


    public Robot(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;

        drivetrain = new Drivetrain(this);


    }
}
