package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 * Created by Eric D'Urso on 10/6/2017.
 */

public class T_Gyro_Drive extends LinearOpMode {
    H_RobotHardware robot = new H_RobotHardware();

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        waitForStart();
        Drive(10, 0.1, 0.1);
    }

    public void Drive(double DesiredDistance, double RightPower, double LeftPower) {
        double dg10incrs = 0.01;
        double dg22incrs = 0.05;
        double dg45incrs = 0.1;
        float zAngle = robot.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        float DesiredAngle = zAngle;
        int newLeftTarget;
        int newRightTarget;
        int startPositionR;
        int startPositionL;
        int currentPositionRight;
        int currentPositionLeft;
        int rightPositionTraveled;
        int leftPositionTraveled;
        float displacement;
        final double COUNTS_PER_MOTOR_REV = 1120;    // TETRIX MOTORS = 1440, andymark = 1120
        final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
        final double WHEEL_DIAMETER_INCHES = 3.8125;     // For figuring circumference
        final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
        if (opModeIsActive()) {
            startPositionR = robot.rightDrive.getCurrentPosition();
            startPositionL = robot.leftDrive.getCurrentPosition();
            newLeftTarget = robot.leftDrive.getCurrentPosition() + (int) (DesiredDistance * COUNTS_PER_INCH);
            newRightTarget = robot.rightDrive.getCurrentPosition() + (int) (DesiredDistance * COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftDrive.setPower(Math.abs(LeftPower));
            robot.rightDrive.setPower(Math.abs(RightPower));
            while (opModeIsActive() && (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {
                currentPositionRight = robot.rightDrive.getCurrentPosition();
                currentPositionLeft = robot.leftDrive.getCurrentPosition();
                rightPositionTraveled = currentPositionRight - startPositionR;
                leftPositionTraveled = currentPositionLeft - startPositionL;
                if (rightPositionTraveled > leftPositionTraveled){
                    LeftPower = LeftPower + 0.01;
                }
                if (rightPositionTraveled < leftPositionTraveled){
                    RightPower = RightPower + 0.01;
                }
            }
        }
    }
}


