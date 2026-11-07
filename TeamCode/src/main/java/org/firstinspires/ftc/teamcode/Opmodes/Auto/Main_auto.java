
package org.firstinspires.ftc.teamcode.Opmodes.Auto;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Robot;

import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.commands.Commands.*;

@Autonomous
public class Main_auto extends LinearOpMode {


    boolean yes = true;

    @Override
    public void runOpMode() {

        Robot robot = new Robot(hardwareMap, gamepad1,telemetry);

        Scheduler.reset();
        robot.init();
        robot.shooter.periodic().schedule();

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Command auto = sequential(
                waitMs(500),
                robot.intake.shootIn()

        );
        waitForStart();




        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            robot.shooter.flywheelset();
            auto.schedule();


            Scheduler.execute();
            telemetry.update();
        }
    }}
