
package org.firstinspires.ftc.teamcode.Opmodes.TeleOp;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.Robot;


@TeleOp
public class Main_Tele extends LinearOpMode {


    boolean yes = true;

    @Override
    public void runOpMode() {

        Robot robot = new Robot(hardwareMap, gamepad1,telemetry);

        Scheduler.reset();
        robot.init();
        robot.periodic();

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();




        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            if(yes){
                robot.shooter.flywheelset();
                yes = false;
            }
            if(gamepad1.aWasPressed()){
                robot.intake.intakeIn().schedule();
            } else if(gamepad1.bWasPressed()){
                robot.intake.intakeOff().schedule();
            }


            if(gamepad1.dpadLeftWasPressed()){
                robot.intake.intakeRSide();
            } else if(gamepad1.dpadRightWasPressed()){
                robot.intake.intakeBSide();
            }

            Scheduler.execute();
            telemetry.update();
        }
    }}
