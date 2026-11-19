
package org.firstinspires.ftc.teamcode.Opmodes.Testing;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Robot;


@TeleOp(group = "Testing")
public class intake extends LinearOpMode {


    @Override
    public void runOpMode() {

        Robot robot = new Robot(hardwareMap, gamepad1, telemetry);

        Scheduler.reset();
        robot.intake.init();


        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            if (gamepad1.bWasPressed()) {
                robot.intake.intakeIn().schedule();
            }
            if (gamepad1.bWasReleased()) {
                robot.intake.intakeOff().schedule();
            }


            if (gamepad1.rightBumperWasPressed()) {
                robot.intake.shootIn().schedule();
            }
            if (gamepad1.rightBumperWasReleased()) {
                robot.intake.intakeOff().schedule();

            }

            if (gamepad1.dpadLeftWasPressed()) {
                robot.intake.intakeBSide().schedule();
            }
            if (gamepad1.dpadLeftWasReleased()) {
                robot.intake.intakeOff().schedule();

            }

            if (gamepad1.dpadRightWasPressed()) {
                robot.intake.intakeRSide().schedule();
            }
            if (gamepad1.dpadRightWasReleased()) {
                robot.intake.intakeOff().schedule();

            }

            Scheduler.execute();
            telemetry.update();


        }
    }
}