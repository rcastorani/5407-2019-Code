/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANError;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;
import java.lang.AutoCloseable;

public class RobotMap {

//Left Side Speed Controlers
int leftMotorID_1 = 1;
int leftMotorID_2 = 1;
int leftMotorID_3 = 1;

//Right Side Speed Controlers
int rightMotorID_1 = 1;
int rightMotorID_2 = 1;
int rightMotorID_3 = 1;

public CANSparkMax leftMotor_1;
public CANSparkMax leftMotor_2;

public CANSparkMax rightMotor_1;
public CANSparkMax rightMotor_2;

public DifferentialDrive drive;

    public RobotMap() {

        leftMotor_1 = new CANSparkMax(leftMotorID_1, MotorType.kBrushless);
        leftMotor_2 = new CANSparkMax(leftMotorID_2, MotorType.kBrushless);

        rightMotor_1 = new CANSparkMax(rightMotorID_1, MotorType.kBrushless);
        rightMotor_2 = new CANSparkMax(rightMotorID_2, MotorType.kBrushless);

        leftMotor_2.follow(leftMotor_1);
        rightMotor_2.follow(rightMotor_1);

        drive = new DifferentialDrive(leftMotor_1, rightMotor_1);

    }

    public void motorSafetyCheck() {
        if (leftMotor_1.getMotorType() == MotorType.kBrushed || leftMotor_2.getMotorType() == MotorType.kBrushed ||
        rightMotor_1.getMotorType() == MotorType.kBrushed || rightMotor_2.getMotorType() == MotorType.kBrushed) {
            System.out.println("Brushed motor selected");
            System.exit(0);
        }
      }

}
