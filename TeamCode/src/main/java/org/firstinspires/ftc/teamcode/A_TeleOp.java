package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Eric D'Urso on 10/5/2017.
 */
@TeleOp(name = "TeleOp")
public class A_TeleOp extends OpMode {
    A_Main main = new A_Main();
    @Override
    public void init() {
        main.init(hardwareMap, false);
    }
    @Override
    public void loop() {
        double x = gamepad1.right_stick_x;
        double y = -gamepad1.right_stick_y;
        /* ry -1 is up, 1 is down, -1 is left, 1 is right*/
        if ((Math.abs(x)<0.1)&&(Math.abs(y)<0.1)){
            //Do Nothing based on controller pos in middle
            //main.leftDrive.setPower(0);
            //main.rightDrive.setPower(0);
            telemetry.addLine("ME DONT DO NOTHIN");
            telemetry.update();
            if (gamepad1.left_trigger > 0.05 || gamepad1.right_trigger > 0.05){
                main.rightDrive.setPower(gamepad1.right_trigger);
                main.leftDrive.setPower(gamepad1.left_trigger);
            } else {
                main.rightDrive.setPower(0);
                main.leftDrive.setPower(0);
            }
        } else {
            //do something base on controller position
            if ((y>0)&&(y>(Math.abs(x)))){
                //Top Part, Single stick forward
                main.rightDrive.setPower(main.scaleController(y));
                main.leftDrive.setPower(main.scaleController(y));
                telemetry.addLine("ME MOVE FORWARD");
                telemetry.update();
            }
            else if ((y<0)&&(y<((-1.0)*(Math.abs(x))))){
                //bottom part, single stick reverse
                main.rightDrive.setPower(main.scaleController(y));
                main.leftDrive.setPower(main.scaleController(y));
                telemetry.addLine("ME MOVE BACKWARD");
                telemetry.update();
            } else if (x>0){
                //right part, turn right
                main.rightDrive.setPower(main.scaleController(x));
                main.leftDrive.setPower(main.scaleController(-x));
                telemetry.addLine("ME TURN RIGHT");
                telemetry.update();
            }
            else {
                //left part, turn left
                main.rightDrive.setPower(main.scaleController(x));
                main.leftDrive.setPower(main.scaleController(-x));
                telemetry.addLine("ME TURN LEFT");
                telemetry.update();
            }
        }

        if (gamepad1.dpad_up) {
            main.Lift.setPower(0.3);
        }
        if (gamepad1.dpad_down) {
            main.Lift.setPower(-0.3);
        }
        if (((!gamepad1.dpad_up) && (!gamepad1.dpad_down))) {
            main.Lift.setPower(0);
        }
        if (gamepad1.left_bumper) {
            main.CloseClaw();
        } else {
            main.OpenClaw();
        }
        if (gamepad2.left_bumper){
            main.CloseRelic();
        } else {
            main.OpenRelic();
        }
        if (gamepad2.dpad_left){
            main.ExtendRelic.setPower(0.3);
        } else if (gamepad2.dpad_right){
            main.ExtendRelic.setPower(-0.3);
        } else {
            main.ExtendRelic.setPower(0.0);
        }
        if (gamepad2.dpad_up){
            main.RotateRelic.setPower(0.3);
        } else if (gamepad2.dpad_down){
            main.RotateRelic.setPower(-0.3);
        } else {
            main.RotateRelic.setPower(0.0);
        }
        if (!gamepad2.start){
            main.MMS.setPosition(0.3);//up
        }
    }
}