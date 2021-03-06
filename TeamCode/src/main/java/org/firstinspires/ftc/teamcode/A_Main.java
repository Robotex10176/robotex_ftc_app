package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class A_Main
{
    public com.qualcomm.robotcore.hardware.ColorSensor ColorSensor;
    public com.qualcomm.robotcore.hardware.ColorSensor GlyphSensor;
    IntegratingGyroscope gyro;
    ModernRoboticsI2cGyro modernRoboticsI2cGyro;
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor Lift;
    public DcMotor arm;
    public DcMotor ExtendRelic;
    public DcMotor RotateRelic;
    public Servo RightClaw;
    public Servo LeftClaw;
    public Servo moveFlick;
    public Servo MoveSensor;
    public Servo MMS;
    public Servo RelicClaw;
    public static final ElapsedTime timer = new ElapsedTime();
    public static final boolean A = true;
    HardwareMap hwMap           = null;

    //Constructor
    public A_Main(){}

    public void init(HardwareMap ahwMap, boolean auto) {
        hwMap = ahwMap;
        ColorSensor = hwMap.colorSensor.get("ColorSensor");
        GlyphSensor = hwMap.colorSensor.get("GlyphSensor");
        modernRoboticsI2cGyro = hwMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro = (IntegratingGyroscope)modernRoboticsI2cGyro;
        leftDrive = hwMap.dcMotor.get("leftDrive");
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftDrive.setPower(0);
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive = hwMap.dcMotor.get("rightDrive");
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setPower(0);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Lift = hwMap.dcMotor.get("Lift");
        Lift.setDirection(DcMotorSimple.Direction.FORWARD);
        Lift.setPower(0);
        arm = hwMap.dcMotor.get("Arm");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setPower(0);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ExtendRelic = hwMap.dcMotor.get("ER");
        ExtendRelic.setDirection(DcMotorSimple.Direction.FORWARD);
        ExtendRelic.setPower(0);
        RotateRelic = hwMap.dcMotor.get("RR");
        RotateRelic.setDirection(DcMotorSimple.Direction.FORWARD);
        RotateRelic.setPower(0);
        RelicClaw = hwMap.servo.get("RC");
        RelicClaw.setPosition(0.5);
        moveFlick = hwMap.servo.get("Jewel");
        moveFlick.setPosition(0.45);
        RightClaw = hwMap.servo.get("RightClaw");
        LeftClaw = hwMap.servo.get("LeftClaw");
        //RightClaw.setPosition(1);
        //LeftClaw.setPosition(0.25);
        MMS = hwMap.servo.get("MMS");
        MMS.setPosition(0.3 );//up, 0 is down
        MoveSensor = hwMap.servo.get("MoveSensor");
        MoveSensor.setPosition(1);
        CloseClaw();
        final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // TETRIX MOTORS = 1440, andymark = 1120
        final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
        final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
        final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);
        if(auto){
            modernRoboticsI2cGyro.calibrate();
            timer.reset();
            while (modernRoboticsI2cGyro.isCalibrating()) {

            }
        }

    }

    public double scaleController(double in){
        //return ( java.lang.Math.signum(in)*in*in);
        return (in*in*in);
    }
    public double halfPwr (double in){
        return (in/2);
    }

    //TEAM METHODS BELOW

    //Claw A_Main

    public void CloseClaw (){
        RightClaw.setPosition(0.50);
        LeftClaw.setPosition(0.50);
    }
    public void OpenClaw (){
        RightClaw.setPosition(0.9);
        LeftClaw.setPosition(0.0);
    }
    public void FlatClaw(){
        RightClaw.setPosition(0.3);
        LeftClaw.setPosition(0);
    }
    public void CloseRelic() {
    RelicClaw.setPosition(0);
    }
    public void OpenRelic() {
        RelicClaw.setPosition(0.5);
    }

    //Jewel Arm A_Main

    public void moveArm (double Degrees, double Power){
        int newLeftTarget;
        final double COUNTS_PER_MOTOR_REV = 1120;    // TETRIX MOTORS = 1440, andymark = 1120
        final double DEGREES = (Degrees*(COUNTS_PER_MOTOR_REV/360));//3.111111111111111111111111111 ticks per degree
        if (A) {
            newLeftTarget = arm.getCurrentPosition() + (int) (DEGREES);
            arm.setTargetPosition(newLeftTarget);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(Math.abs(Power));
            while (A && (arm.isBusy())) {

            }
            arm.setPower(0);
        }
    }
    public void SeeOurColor (){ moveFlick.setPosition(0); }
    public void DontSeeOurColor (){ moveFlick.setPosition(1); }//I just changed them when we changed the arm.
    public void JewelServoReturn (double KNOW_THAT_YOU_HAVE_TO_SLEEP_BEFORE_THIS){
        moveFlick.setPosition(0.45);
    }


    //Drive A_Main

    public void DriveNoCorrection (double DesiredDistance, double Power){
        if (DesiredDistance < 10){
            Power = Math.min(Power, 0.4);
        }
        int newLeftTarget;
        int newRightTarget;
        final double COUNTS_PER_MOTOR_REV = 1440;    // TETRIX MOTORS = 1440, andymark = 1120
        final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared DOWN
        final double WHEEL_DIAMETER_INCHES = 3.95;     // used to be 3.8125
        final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
        if (A) {
            //float CurrentOrientation = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            //float OrigionalOrientation = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            newLeftTarget = leftDrive.getCurrentPosition() + (int) (DesiredDistance * COUNTS_PER_INCH);
            newRightTarget = rightDrive.getCurrentPosition() + (int) (DesiredDistance * COUNTS_PER_INCH);
            leftDrive.setTargetPosition(newLeftTarget);
            rightDrive.setTargetPosition(newRightTarget);
            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftDrive.setPower(Math.abs(Power));
            rightDrive.setPower(Math.abs(Power));
            while ((leftDrive.isBusy() || rightDrive.isBusy())){

                leftDrive.setPower(Math.abs(Power));
                rightDrive.setPower(Math.abs(Power));
            //while ((leftDrive.isBusy() || rightDrive.isBusy()) && (Math.abs(CurrentOrientation-OrigionalOrientation)<10.0) ) {
                //CurrentOrientation = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
                //if (rightDrive.getCurrentPosition()> newRightTarget){
                //    rightDrive.setPower(0);
                //}
                //if (leftDrive.getCurrentPosition() > newLeftTarget){
                //    leftDrive.setPower(0);
                //}
            }//&&
            leftDrive.setPower(0);
            rightDrive.setPower(0);
            rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public void DriveGyroCorrection(double DesiredDistance, double RightPower, double LeftPower){
        double dg10incrs = 0.01;
        double dg22incrs = 0.05;
        double dg45incrs = 0.1;
        float DesiredAngle = 0;
        int newLeftTarget;
        int newRightTarget;
        float displacement;
        final double COUNTS_PER_MOTOR_REV = 1440;    // TETRIX MOTORS = 1440, andymark = 1120
        final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
        final double WHEEL_DIAMETER_INCHES = 3.8125;     // For figuring circumference
        final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
        if (A) {
            newLeftTarget = leftDrive.getCurrentPosition() + (int) (DesiredDistance * COUNTS_PER_INCH);
            newRightTarget = rightDrive.getCurrentPosition() + (int) (DesiredDistance * COUNTS_PER_INCH);
            leftDrive.setTargetPosition(newLeftTarget);
            rightDrive.setTargetPosition(newRightTarget);
            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftDrive.setPower(Math.abs(LeftPower));
            rightDrive.setPower(Math.abs(RightPower));
            while (A && (leftDrive.isBusy() && rightDrive.isBusy())) {
                float zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
                if (zAngle > DesiredAngle){
                    LeftPower = LeftPower + ((zAngle - DesiredAngle)/90) * 0.05;
                    RightPower = RightPower - ((zAngle - DesiredAngle)/90) * 0.05;
                } else {
                    LeftPower = LeftPower - (-1*(zAngle - DesiredAngle)/90) * 0.05;
                    RightPower = RightPower + (-1*(zAngle - DesiredAngle)/90) * 0.05;
                }
                leftDrive.setPower(Math.abs(LeftPower));
                rightDrive.setPower(Math.abs(RightPower));
            }
            float zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            if (zAngle != DesiredAngle){
                leftDrive.setPower(-0.05);
                leftDrive.setPower(0.05);
            }
        }
    }
    //TURN METHODS
//Floats
    public float AngularSeparation (float a, float b){
        float rv;
        rv = Math.abs(a-b);
        if (rv >= 180){
            rv = 360 - rv;
        }
        return rv;
    }
    public void SmartTurnRight(float Angle, double Power){//turn right is clockwise
        //code to turn untill an angle ex 0, 90, -90
        float zAngle;
        float targetAngle;
        zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC,
                AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        //Set target direction in range -180 - 180;
        targetAngle = (zAngle - Angle + 180);
        while (targetAngle > 360){ targetAngle = targetAngle - 360; }
        while (targetAngle < 0){ targetAngle = targetAngle + 360; }
        targetAngle = targetAngle - 180;
        float as;
        as = AngularSeparation(zAngle, targetAngle);
        while (as > 1.0){//1.0

            if(as < 15) {
                leftDrive.setPower(Math.max(0.2 * Power, 0.1));
                rightDrive.setPower(-Math.max(0.2 * Power, 0.1));

            } else if (as < 45){
                leftDrive.setPower(Math.max(0.5 * Power, 0.1));
                rightDrive.setPower(-Math.max(0.5 * Power, 0.1));
            }else {
                leftDrive.setPower(Math.max(Power, 0.1));
                rightDrive.setPower(-Math.max(Power, 0.1));
            }
            zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC,
                    AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            as = AngularSeparation(zAngle, targetAngle);
        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);

    }

    public void SmartTurnLeft (float Angle, double Power){
        float zAngle;
        float targetAngle;
        zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        //Set target direction in range -180 - 180;
        targetAngle = (zAngle + Angle + 180);
        while (targetAngle > 360){ targetAngle = targetAngle - 360; }
        while (targetAngle < 0){ targetAngle = targetAngle + 360; }
        targetAngle = targetAngle - 180;
        float as;
        as = AngularSeparation(zAngle, targetAngle);
        while (as > 1.0){

            if(as < 15) {
                leftDrive.setPower(-Math.max(0.2 * Power, 0.1));
                rightDrive.setPower(Math.max(0.2 * Power, 0.1));
            } else if (as < 45){
                leftDrive.setPower(-Math.max(0.5 * Power, 0.1));
                rightDrive.setPower(Math.max(0.5 * Power, 0.1));
            }else {
                leftDrive.setPower(-Math.max(Power, 0.1));
                rightDrive.setPower(Math.max(Power, 0.1));
            }
            zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC,
                    AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            as = AngularSeparation(zAngle, targetAngle);
        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);

    }

//doubles
    public double AngularSeparationD (double a, double b){
        double rv;
        rv = Math.abs(a-b);
        if (rv >= 180){
            rv = 360 - rv;
        }
        return rv;
    }
    public void SmartTurnRightD(double Angle, double Power){//turn right is clockwise
        //code to turn untill an angle ex 0, 90, -90
        double zAngle;
        double targetAngle;
        zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        //Set target direction in range -180 - 180;
        targetAngle = (zAngle - Angle + 180);
        while (targetAngle > 360){ targetAngle = targetAngle - 360; }
        while (targetAngle < 0){ targetAngle = targetAngle + 360; }
        targetAngle = targetAngle - 180;

        while (AngularSeparationD(zAngle, targetAngle)> 2.0){
            leftDrive.setPower(Power);
            rightDrive.setPower(-Power);
            zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);

    }

    public void SmartTurnLeftD (double Angle, double Power){
        double zAngle;
        double targetAngle;
        zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        //Set target direction in range -180 - 180;
        targetAngle = (zAngle - Angle + 180);
        while (targetAngle > 360){ targetAngle = targetAngle - 360; }
        while (targetAngle < 0){ targetAngle = targetAngle + 360; }
        targetAngle = targetAngle - 180;

        while (AngularSeparationD(zAngle, targetAngle)> 2.0){
            leftDrive.setPower(-Power);
            rightDrive.setPower(Power);
            zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);

    }
 }

