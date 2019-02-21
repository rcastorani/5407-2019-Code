/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Limelight.LightMode;

public class Robot extends TimedRobot {
  Actions actions;
  Air air;
  Limelight limelight10;
  Limelight limelight11;
  OI oi;
  RobotMap robotmap;
  Sensors sensors;

  public String hostNameTen = "limelight-ten";
  public String hostNameEleven = "limelight-eleven";

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private static final String kPipeline0 = "Pipeline 0";
  private static final String kPipeline1 = "Pipeline 1";
  private static final String kPipeline2 = "Pipeline 2";
  private String m_pipelineChoice;
  private final SendableChooser<String> m_pipeline = new SendableChooser<>();

  private static final String kHighHatch = "High Hatch";
  private static final String kMidHatch = "Mid Hatch";
  private static final String kLowHatch = "Low Hatch";
  private String m_armControl;

  // Talk to Limelight Network Tables
  // http://docs.limelightvision.io/en/latest/getting_started.html
  private double cameraTargetXAxis;
  private double cameraTargetYAxis;
  private double cameraTargetArea;

  // For Range
  double distance;
  double heading_error;
  double hard_mounting_angle;
  double soft_mounting_angle;

  // For AimAndRange
  double steeringAdjustBack;
  double drivingAdjustBack;

  // For Aim and Range Back
  double drivingAdjustFront;
  double steeringAdjustFront;

  double armkp = 0;
  double armkd = 0;
  double armError = 0;
  double output;
  double armDistance = 0;
  double actualdistance;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    air = new Air();
    limelight10 = new Limelight(hostNameTen);
    limelight11 = new Limelight(hostNameEleven);
    oi = new OI(limelight11);
    robotmap = new RobotMap();
    sensors = new Sensors();
    actions = new Actions(air, oi, robotmap);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_pipeline.setDefaultOption("Front-Tape", kPipeline2);
    m_pipeline.addOption("Back-Tape", kPipeline0);
    m_pipeline.addOption("Ball", kPipeline1);
    SmartDashboard.putData("Pipeline", m_pipeline);

    // limelight10.setLedMode(LightMode.eOff);
    // limelight11.setLedMode(LightMode.eOff);

    hard_mounting_angle = Calculations.getHardMountingAngle();
    final int threeFeet = 36; // Assume this distance from camera lens to target
    soft_mounting_angle = Calculations.getSoftMountingAngle(cameraTargetYAxis, threeFeet);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Gyro-NAVX", sensors.getPresentAngleNAVX());
    SmartDashboard.updateValues();

    // Limelight read values periodically
    cameraTargetXAxis = limelight11.getTx();
    cameraTargetYAxis = limelight11.getTy();
    cameraTargetArea = limelight11.getTa();

    // Limelight post to smart dashboard periodically
    SmartDashboard.putNumber("limelightX", cameraTargetXAxis);
    SmartDashboard.putNumber("limelightY", cameraTargetYAxis);
    SmartDashboard.putNumber("limelightArea", cameraTargetArea);
    SmartDashboard.putNumber("Distance", distance);
    SmartDashboard.putNumber("hardMA", hard_mounting_angle);
    SmartDashboard.putNumber("softMA", soft_mounting_angle);
    SmartDashboard.putBoolean("ledStatus", oi.ledStatus);

    m_pipelineChoice = m_pipeline.getSelected();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    // Zero the NAVX before auton
    sensors.zeroNAVX();

    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    case kCustomAuto:
      // Put custom auto code here
      break;
    case kDefaultAuto:
    default:
      // Put default auto code here
      sensors.setFollowAngleNAVX(0);
      robotmap.drive.arcadeDrive(0, (sensors.getFollowAngleNAVX() - sensors.getPresentAngleNAVX()) * 0.015);
      break;
    }
  }

  @Override
  public void teleopInit() {
    // Zero the NAVX before teleop
    sensors.zeroNAVX();

    // Turn off Limelight LEDs before teleop
    // limelight10.setLedMode(LightMode.eOff);
    // limelight11.setLedMode(LightMode.eOff);
  }

  @Override
  public void teleopPeriodic() {
    oi.readValues();
    distance = Calculations.getRange(cameraTargetYAxis);

    if (oi.getOPControlButton()) {
      actions.endGameOp();
    } else if (oi.getOpLeftBumper()) {
      getAimAndRangeFront();
      heading_error = Calculations.getHeadingError(cameraTargetXAxis);
      robotmap.drive.arcadeDrive(drivingAdjustFront, steeringAdjustFront);
    } else if (oi.getOpRightBumper()) {
      getAimAndRangeBack();
      robotmap.drive.arcadeDrive(drivingAdjustBack, steeringAdjustBack);
    } else {
      actions.gameOp();
      //robotmap.drive.arcadeDrive(-oi.getThrottle(), oi.getTurn());
      robotmap.drive.arcadeDrive(-oi.getXThrottle(), oi.getXTurn());
    }

    if (!robotmap.getFlowKcap()) {
      robotmap.motorSafetyCheck();
    }

    // System.out.println(sensors.getArmPotValue());

  }

  @Override
  public void testPeriodic() {}

  public void getAimAndRangeFront() {
    AimAndRange aimAndRange = Calculations.getAimAndRangeFront(cameraTargetXAxis, cameraTargetYAxis);
    drivingAdjustFront = aimAndRange.getDrivingAdjust();
    steeringAdjustFront = aimAndRange.getSteeringAdjust();
  }

  public void getAimAndRangeBack() {
    AimAndRange aimAndRange = Calculations.getAimAndRangeBack(cameraTargetXAxis, cameraTargetYAxis);
    drivingAdjustBack = aimAndRange.getDrivingAdjust();
    steeringAdjustBack = aimAndRange.getSteeringAdjust();
  }

  public void updatePipelineChoice() {
    switch (m_pipelineChoice) {
      case kPipeline0:
        limelight10.setPipeline(0);
        break;
      case kPipeline1:
        limelight10.setPipeline(1);
        break;
      case kPipeline2:
        limelight10.setPipeline(2);
        break;
    }
  }

  public void armControl() {
 
    switch (m_armControl) {
      case kHighHatch:
        //Set Distace
        break;
      case kMidHatch:
        //Set Distance
        break;
      case kLowHatch:
        //Set Distance
        break;
      }

      armError = armDistance - actualdistance;

    output = armkp * armError + armkd;
    robotmap.arm.set(output);
  }

  // public void driveStraight() {
  // double turn = (sensors.kAngleSetpoint - sensors.gyro.getAngle()) *
  // sensors.kP;
  // turn = Math.copySign(turn, direction);
  // robotmap.drive.arcadeDrive(-oi.getThrottle(), turn);
  // }
}
