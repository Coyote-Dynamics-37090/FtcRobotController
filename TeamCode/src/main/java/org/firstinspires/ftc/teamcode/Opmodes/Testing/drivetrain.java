
package org.firstinspires.ftc.teamcode.Opmodes.Testing;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Robot;


@TeleOp
public class drivetrain extends LinearOpMode {




    @Override
    public void runOpMode() {

        Robot robot = new Robot(hardwareMap, gamepad1,telemetry);

        Scheduler.reset();
        robot.drivetrain.init();
        robot.drivetrain.periodic().schedule();

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();




        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {




            robot.drivetrain.telemetry();

            Scheduler.execute();
            telemetry.update();
        }
    }}
