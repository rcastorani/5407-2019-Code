/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Add your docs here.
 */
public class OI {

	private Joystick driveStick;
	private double throttle;
	private double turn;

	private Joystick opStick;
	private boolean op_yButton;
	private boolean op_bButton;
	private boolean op_aButton;
	private boolean op_xButton;
	private double op_leftStick;
	private double op_rightStick;
	
    public OI() {
		driveStick = new Joystick(0);
		opStick = new Joystick(1);
    }

    public void readValues() {
		if (driveStick.getY() < -0.1 || driveStick.getY() > 0.1) {
			throttle = driveStick.getY();
		} 
		else {
			throttle = 0;
		}
		
		if (driveStick.getX() < -0.1  || driveStick.getX() > 0.1) {
			turn = driveStick.getX();
		} else {
			turn = 0;
		}
		op_yButton = opStick.getRawButton(4);
		op_bButton = opStick.getRawButton(2);
		op_aButton = opStick.getRawButton(1);
		op_xButton = opStick.getRawButton(3);
		op_rightStick = opStick.getRawAxis(5);
	}
	
	public double getThrottle() {
		return throttle;
	}
	
	public double getTurn() {
		return turn;
	}

}
