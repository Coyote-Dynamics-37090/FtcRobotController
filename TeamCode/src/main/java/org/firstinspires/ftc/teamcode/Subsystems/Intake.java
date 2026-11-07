package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.Robot;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

public class Intake {

    public final DcMotor intakeB;
    public final DcMotor intakeR;
    public final Servo ISwitcher;

    private final double INTAKE_POWER = 1;
    private final double OUTAKE_POWER = -1;

    public double SB = 0;
    public double SR = 1;
    public double SM = 0.5;

    //TODO: add switcher and make flat when normal intaking, one side when intake is running

    public Intake(Robot robot){
        intakeB = robot.hardwareMap.get(DcMotor.class,"intakeB");
        intakeR = robot.hardwareMap.get(DcMotor.class,"intakeR");
        ISwitcher = robot.hardwareMap.get(Servo.class,"ISwitcher");
    }

    public Command intakeBIn() {
        return instant(() -> intakeB.setPower(INTAKE_POWER));
    }
    public Command intakeBOut() {
        return instant(() -> intakeB.setPower(OUTAKE_POWER));
    }
    public Command intakeBOff() {
        return instant(() -> intakeB.setPower(0));
    }

    public Command intakeRIn() {
        return instant(() -> intakeR.setPower(INTAKE_POWER));
    }
    public Command intakeROut() {
        return instant(() -> intakeR.setPower(OUTAKE_POWER));
    }
    public Command intakeROff() {
        return instant(() -> intakeR.setPower(0));
    }

    public Command intakeOff() {
        return parallel(intakeBOff(), intakeROff());
    }

    public Command intakeIn() {
        return parallel(intakeBIn(), intakeRIn(),switcherMid());
    }
    public Command switcherPos(double pos){
        return instant(() -> ISwitcher.setPosition(pos));
    }

    public Command switcherBlack(){
        return instant(() -> ISwitcher.setPosition(SB));
    }
    public Command switcherRed(){
        return instant(() -> ISwitcher.setPosition(SR));
    }
    public Command switcherMid(){
        return instant(() -> ISwitcher.setPosition(SM));
    }

    public Command intakeBSide(){
        return sequential(
                intakeOff(),
                switcherBlack(),
                intakeBIn()
        );
    }

    public Command intakeRSide(){
        return sequential(
                intakeOff(),
                switcherRed(),
                intakeRIn()
        );
    }

    public void init(){
        intakeB.setPower(0);
        intakeR.setPower(0);
        ISwitcher.setPosition(SM);
    }
}
