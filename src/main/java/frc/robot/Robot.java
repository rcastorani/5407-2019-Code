/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  Actions actions;
  Air air;
  static Limelight backLimelight;
  static Limelight frontLimelight;
  LimelightProvider limelightProvider;
  OI oi;
  RobotMap robotmap;
  Sensors sensors;

  // Shuffleboard
  private ShuffleboardTab driveFrontTab = Shuffleboard.getTab("Front");
  private ShuffleboardTab driveBackTab = Shuffleboard.getTab("Back");
  private ShuffleboardTab codeTab = Shuffleboard.getTab("Coding");
  private NetworkTableEntry defenseModeEntry;
  private NetworkTableEntry cameraTargetXAxisEntry;
  private NetworkTableEntry cameraTargetYAxisEntry;
  private NetworkTableEntry cameraTargetAreaEntry;
  private NetworkTableEntry cameraTargetSkewEntry;
  private NetworkTableEntry cameraTargetEntry;
  private NetworkTableEntry armPotValueEntry;
  private NetworkTableEntry winchPotValueEntry;
  private NetworkTableEntry tungOpenEntry;
  private NetworkTableEntry drivingAdjustEntry;
  private NetworkTableEntry steeringAdjustEntry;
  private NetworkTableEntry pixyOutputEntry;
  private NetworkTableEntry hardMountingAngleEntry;
  private NetworkTableEntry softMountingAngleEntry;
  private NetworkTableEntry distanceEntry;
  private NetworkTableEntry reverseDriveEntry;
  //
  private NetworkTableEntry FRONTdefenseModeEntry;
  private NetworkTableEntry FRONTcameraTargetXAxisEntry;
  private NetworkTableEntry FRONTcameraTargetYAxisEntry;
  private NetworkTableEntry FRONTcameraTargetAreaEntry;
  private NetworkTableEntry FRONTcameraTargetSkewEntry;
  private NetworkTableEntry FRONTcameraTargetEntry;
  private NetworkTableEntry FRONTarmPotValueEntry;
  private NetworkTableEntry FRONTwinchPotValueEntry;
  private NetworkTableEntry FRONTtungOpenEntry;
  private NetworkTableEntry FRONTdrivingAdjustEntry;
  private NetworkTableEntry FRONTsteeringAdjustEntry;
  private NetworkTableEntry FRONTpixyOutputEntry;
  private NetworkTableEntry FRONThardMountingAngleEntry;
  private NetworkTableEntry FRONTsoftMountingAngleEntry;
  private NetworkTableEntry FRONTdistanceEntry;
  private NetworkTableEntry FRONTreverseDriveEntry;
  //
  private NetworkTableEntry BACKdefenseModeEntry;
  private NetworkTableEntry BACKcameraTargetXAxisEntry;
  private NetworkTableEntry BACKcameraTargetYAxisEntry;
  private NetworkTableEntry BACKcameraTargetAreaEntry;
  private NetworkTableEntry BACKcameraTargetSkewEntry;
  private NetworkTableEntry BACKcameraTargetEntry;
  private NetworkTableEntry BACKarmPotValueEntry;
  private NetworkTableEntry BACKwinchPotValueEntry;
  private NetworkTableEntry BACKtungOpenEntry;
  private NetworkTableEntry BACKdrivingAdjustEntry;
  private NetworkTableEntry BACKsteeringAdjustEntry;
  private NetworkTableEntry BACKpixyOutputEntry;
  private NetworkTableEntry BACKhardMountingAngleEntry;
  private NetworkTableEntry BACKsoftMountingAngleEntry;
  private NetworkTableEntry BACKdistanceEntry;
  private NetworkTableEntry BACKreverseDriveEntry;

  // Create Limelight Variables for vision processing
  private double cameraTargetXAxis;
  private double cameraTargetYAxis;
  private double cameraTargetArea;
  private boolean cameraTarget;
  public double cameraTargetSkew;

  // For Range
  double distance;
  double hard_mounting_angle;
  double soft_mounting_angle;

  // Potentiometer
  private double armPotValue;
  private double winchPotValue;

  // Tung open or closed
  private boolean isTungOpen;

  // Reverse drive
  private boolean isReverseDrive = true;

  // Other values
  private boolean isDefenseModeEngaged;
  private double drivingAdjust;
  private double steeringAdjust;
  private boolean pixyOutput;

  private boolean selectCodingTab;

  // Motor voltages
  // private double LM0;
  // private double LM1;
  // private double LM2;
  // private double RM0;
  // private double RM1;
  // private double RM2;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    air = new Air();
    oi = new OI();
    robotmap = new RobotMap();
    sensors = new Sensors();
    actions = new Actions(air, oi, robotmap, sensors);

    // Set up shuffleboard
    // Driving Front tab
    // cameraServerFront = driveFrontTab.add("Front Limelight", ).getEntry();
    FRONTdistanceEntry = driveFrontTab.add("Distance", distance).withPosition(14, 2).withSize(1, 1).getEntry();
    FRONTreverseDriveEntry = driveFrontTab.add("BACK", isReverseDrive).withPosition(15, 0).withSize(2, 1).getEntry();
    FRONTpixyOutputEntry = driveFrontTab.add("Pixy", pixyOutput).withPosition(15, 5).withSize(2, 2).getEntry();
    FRONTarmPotValueEntry = driveFrontTab.add("ArmPot", armPotValue).withPosition(14, 1).withSize(1, 1).getEntry();
    FRONTwinchPotValueEntry = driveFrontTab.add("WinchPot", winchPotValue).withPosition(14, 0).withSize(1, 1).getEntry();
    FRONTtungOpenEntry = driveFrontTab.add("Tung", isTungOpen).withPosition(15, 1).withSize(2, 2).getEntry();
    FRONTdefenseModeEntry = driveFrontTab.add("DEFENSE", isDefenseModeEngaged).withPosition(15, 3).withSize(2, 2).getEntry();
    FRONTcameraTargetSkewEntry = driveFrontTab.add("ts", cameraTargetSkew).withPosition(14, 3).withSize(1, 1).getEntry();
    FRONTcameraTargetAreaEntry = driveFrontTab.add("ta", cameraTargetArea).withPosition(14, 4).withSize(1, 1).getEntry();

    // Driving Back tab
    BACKdistanceEntry = driveBackTab.add("Distance", distance).withPosition(14, 2).withSize(1, 1).getEntry();
    BACKreverseDriveEntry = driveBackTab.add("BACK", isReverseDrive).withPosition(15, 0).withSize(2, 1).getEntry();
    BACKpixyOutputEntry = driveBackTab.add("Pixy", pixyOutput).withPosition(15, 5).withSize(2, 2).getEntry();
    BACKarmPotValueEntry = driveBackTab.add("ArmPot", armPotValue).withPosition(14, 1).withSize(1, 1).getEntry();
    BACKwinchPotValueEntry = driveBackTab.add("WinchPot", winchPotValue).withPosition(14, 0).withSize(1, 1).getEntry();
    BACKtungOpenEntry = driveBackTab.add("Tung", isTungOpen).withPosition(15, 1).withSize(2, 2).getEntry();
    BACKdefenseModeEntry = driveBackTab.add("DEFENSE", isDefenseModeEngaged).withPosition(15, 3).withSize(2, 2).getEntry();
    BACKcameraTargetSkewEntry = driveBackTab.add("tS", cameraTargetSkew).withPosition(14, 3).withSize(1, 1).getEntry();
    BACKcameraTargetAreaEntry = driveBackTab.add("ta", cameraTargetArea).withPosition(14, 4).withSize(1, 1).getEntry();

    // Coding tab
    // hardMountingAngleEntry = codeTab.add("HardMA", hard_mounting_angle).getEntry();
    // softMountingAngleEntry = codeTab.add("SoftMA", soft_mounting_angle).getEntry();
    // drivingAdjustEntry = codeTab.add("DA", drivingAdjust).getEntry();
    // steeringAdjustEntry = codeTab.add("SA", steeringAdjust).getEntry();
    // cameraTargetXAxisEntry = codeTab.add("LL X", cameraTargetXAxis).getEntry();
    // cameraTargetYAxisEntry = codeTab.add("LL Y", cameraTargetYAxis).getEntry();
    // cameraTargetAreaEntry = codeTab.add("LL A", cameraTargetArea).getEntry();
    // cameraTargetSkewEntry = codeTab.add("LL S", cameraTargetSkew).getEntry();
    // cameraTargetEntry = codeTab.add("LL T", cameraTarget).getEntry();

    // Instantiate limelights
    limelightProvider = LimelightProvider.getProvider();
    backLimelight = limelightProvider.getBackLimelight();
    frontLimelight = limelightProvider.getFrontLimelight();

    if (!robotmap.getIsFlow()) {
      air.airInit();
    }

    // Zero the NAVX
    sensors.zeroNAVX();

    // Turn off Limelight LEDs during init
    // actions.setLightsAndVision(backLimelight, true);
    // actions.setLightsAndVision(frontLimelight, true);

    hard_mounting_angle = Calculations.getHardMountingAngle();
    final int threeFeet = 36; // Assume this distance from camera lens to target
    soft_mounting_angle = Calculations.getSoftMountingAngle(cameraTargetYAxis, threeFeet);

    // Start USB Camera
    // CameraServer.getInstance().startAutomaticCapture();
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

    // Reverse drive when driver toggles the left bumper
    // This reverses driving motors, switches limelights, and switches shuffleboard tabs
    if (oi.getDriveLeftBumperPressed()) {
      isReverseDrive = !isReverseDrive;
    }
    Limelight currentLimelight = limelightProvider.getCurrentLimelight(isReverseDrive);

    // Select shuffleboard tab
    if (oi.getDriveBackButtonPressed()) {
      selectCodingTab = !selectCodingTab;
    }
    if (selectCodingTab) {
      Shuffleboard.selectTab("Coding");
    } else if (isReverseDrive) {
      Shuffleboard.selectTab("Back");
    } else if (!isReverseDrive) {
      Shuffleboard.selectTab("Front");
    } else {
      Shuffleboard.selectTab("SmartDashboard");
    }

    // Update limelight values
    cameraTargetXAxis = currentLimelight.getTx();
    cameraTargetYAxis = currentLimelight.getTy();
    cameraTargetArea = currentLimelight.getTa();
    cameraTarget = currentLimelight.isTarget();
    cameraTargetSkew = currentLimelight.getTs();

    // Update arm potentiometer value
    armPotValue = sensors.getArmHeight();
    winchPotValue = sensors.getSmallWinchPot();

    // Tung open or closed
    if (!robotmap.getIsFlow()) {
      isTungOpen = air.getSolenoid2();
      SmartDashboard.putBoolean("Tung", isTungOpen);
    }

    // Update values
    isDefenseModeEngaged = actions.defenseModeToggle.get();
    drivingAdjust = actions.drivingAdjust;
    steeringAdjust = actions.steeringAdjust;
    pixyOutput = sensors.getPixyOutput();
  
    // Limelight post to smart dashboard periodically
    // SmartDashboard.putNumber("limelightX", cameraTargetXAxis);
    // SmartDashboard.putNumber("limelightY", cameraTargetYAxis);
    // SmartDashboard.putNumber("limelightArea", cameraTargetArea);
    // SmartDashboard.putBoolean("limelightTarget", cameraTarget);
    // SmartDashboard.putNumber("Distance", distance);
    // SmartDashboard.putNumber("hardMA", hard_mounting_angle);
    // SmartDashboard.putNumber("softMA", soft_mounting_angle);
    // SmartDashboard.putBoolean("visionStatus", visionStatus);
    // SmartDashboard.putNumber("ArmPot", armPotValue);
    // SmartDashboard.putNumber("WinchPot", winchPotValue);
    // SmartDashboard.putBoolean("DEFENSE", isDefenseModeEngaged);
    // SmartDashboard.putBoolean("BACKWARDS", isReverseDrive);
    // SmartDashboard.putNumber("DA", drivingAdjust);
    // SmartDashboard.putNumber("SA", steeringAdjust);
    // SmartDashboard.putBoolean("PIXY", pixyOutput);
    // SmartDashboard.putNumber("TS", cameraTargetSkew);

    // Update shuffleboard
    // Driving tab
    FRONTdistanceEntry.setDouble(distance);
    FRONTreverseDriveEntry.setBoolean(isReverseDrive);
    FRONTpixyOutputEntry.setBoolean(pixyOutput);
    FRONTarmPotValueEntry.setDouble(armPotValue);
    FRONTwinchPotValueEntry.setDouble(winchPotValue);
    FRONTtungOpenEntry.setBoolean(isTungOpen);
    FRONTdefenseModeEntry.setBoolean(isDefenseModeEngaged);
    FRONTcameraTargetSkewEntry.setDouble(cameraTargetSkew);
    FRONTcameraTargetAreaEntry.setDouble(cameraTargetArea);
    //
    BACKdistanceEntry.setDouble(distance);
    BACKreverseDriveEntry.setBoolean(isReverseDrive);
    BACKpixyOutputEntry.setBoolean(pixyOutput);
    BACKarmPotValueEntry.setDouble(armPotValue);
    BACKwinchPotValueEntry.setDouble(winchPotValue);
    BACKtungOpenEntry.setBoolean(isTungOpen);
    BACKdefenseModeEntry.setBoolean(isDefenseModeEngaged);
    BACKcameraTargetSkewEntry.setDouble(cameraTargetSkew);
    BACKcameraTargetAreaEntry.setDouble(cameraTargetArea);

    // Coding tab
    // hardMountingAngleEntry.setDouble(hard_mounting_angle);
    // softMountingAngleEntry.setDouble(soft_mounting_angle);
    // drivingAdjustEntry.setDouble(drivingAdjust);
    // steeringAdjustEntry.setDouble(steeringAdjust);
    // cameraTargetXAxisEntry.setDouble(cameraTargetXAxis);
    // cameraTargetYAxisEntry.setDouble(cameraTargetYAxis);
    // cameraTargetAreaEntry.setDouble(cameraTargetArea);
    // cameraTargetSkewEntry.setDouble(cameraTargetSkew);
    // cameraTargetEntry.setBoolean(cameraTarget);

    // double[] cornX = currentLimelight.getTcornX();
    // double[] cornY = currentLimelight.getTcornY();
    // System.out.println("X Array: " + Arrays.toString(cornX) + " | Y Array: " + Arrays.toString(cornY));

    // Values go clockwise from bottom left of bounding box
    // double xValue0 = cornX[0];
    // double xValue1 = cornX[1];
    // double xValue2 = cornX[2];
    // double xValue3 = cornX[3];
    // double yValue0 = cornY[0];
    // double yValue1 = cornY[1];
    // double yValue2 = cornY[2];
    // double yValue3 = cornY[3];
    // SmartDashboard.putNumber("tcornx0", xValue0);
    // SmartDashboard.putNumber("tcornx1", xValue1);
    // SmartDashboard.putNumber("tcornx2", xValue2);
    // SmartDashboard.putNumber("tcornx3", xValue3);
    // SmartDashboard.putNumber("tcorny0", yValue0);
    // SmartDashboard.putNumber("tcorny1", yValue1);
    // SmartDashboard.putNumber("tcorny2", yValue2);
    // SmartDashboard.putNumber("tcorny3", yValue3);

    /*     
    // Get motor voltage values
    LM0 = robotmap.leftMotor_0.getOutputCurrent();
    LM1 = robotmap.leftMotor_1.getOutputCurrent();
    LM2 = robotmap.leftMotor_2.getOutputCurrent();
    RM0 = robotmap.rightMotor_0.getOutputCurrent();
    RM1 = robotmap.rightMotor_1.getOutputCurrent();
    RM2 = robotmap.rightMotor_2.getOutputCurrent();
    // Put motor voltage values on shuffleboard
    SmartDashboard.putNumber("LM0", LM0);
    SmartDashboard.putNumber("LM1", LM1);
    SmartDashboard.putNumber("LM2", LM2);
    SmartDashboard.putNumber("RM0", RM0);
    SmartDashboard.putNumber("RM1", RM1);
    SmartDashboard.putNumber("RM2", RM2);
    */
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
  
    // Set winch position
    actions.startGame();
  }

  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }

  @Override
  public void teleopInit() {
    // Zero the NAVX before teleop
    sensors.zeroNAVX();
  }

  @Override
  public void teleopPeriodic() {
    oi.readValues();
    distance = Calculations.getRange(cameraTargetYAxis);

    // Check if in defense mode and only run defense function
    if (actions.checkDefenseMode()) {
      actions.defenseMode();
      return;
    }
    // If not in defense mode run either Game Operations or End Game Operations
    if (oi.getJoystickEmulatorButtonSwitch2()) {
      actions.endGameOp();
    } else {
      actions.gameOp(cameraTargetXAxis, cameraTargetYAxis, cameraTargetArea, cameraTarget);
    }
  }
}
