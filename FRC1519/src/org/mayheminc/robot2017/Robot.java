package org.mayheminc.robot2017;

import org.mayheminc.robot2017.commands.RunAutonomous;
import org.mayheminc.robot2017.subsystems.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends MayhemIterativeRobot {
	static NetworkTable table;

	public static Drive drive= new Drive();
	public static Shooter shooter = new Shooter(); 	
	public static Climber climber = new Climber();
	public static ShooterLoader shooterLoader = new ShooterLoader();
	public static Autonomous autonomous = new Autonomous();
	public static GearManipulator gearManipulator = new GearManipulator();	
	public static GearFloorPickerUpper gearFloorPickerUpper = new GearFloorPickerUpper();	
	
	public static OI oi;
	private Command autonomousCommand;

	public static final PowerDistributionPanel pdp = new PowerDistributionPanel();


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		oi = new OI();
		table = NetworkTable.getTable("datatable");
		autonomousCommand = new RunAutonomous();
		initSmartDashboard();
	}

	int disabledLoopCount;
	public void disabledPeriodic(){
		updateSmartDashboard();
		disabledLoopCount++;

		drive.periodic();
		updateImgResults();
		postAllianceColor();

	}


	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {	
		postAllianceColor();
		if (autonomousCommand != null) {
			SmartDashboard.putString("robot trace", "auto init");
			autonomousCommand.start();
		}		
	}

	int autoLoopCount;
	
	@Override
	public void autonomousPeriodic() {
		SmartDashboard.putNumber("Auto loop count",  autoLoopCount++);
		drive.periodic();
		shooter.periodic();
		gearFloorPickerUpper.periodic();
		updateImgResults();

		updateSmartDashboard();
	}

	public void teleopInit(){		
		Scheduler.getInstance().removeAll();		
	}

	/**
	 * This function is called periodically during operator control
	 */
	int teleOpLoopCount;
	public void teleopPeriodic() {
		updateSmartDashboard();
		drive.periodic();
		climber.periodic();
		gearFloorPickerUpper.periodic();
		shooter.periodic();
		shooterLoader.periodic();
		
		SmartDashboard.putNumber("Tele loop count",  teleOpLoopCount++);
		updateImgResults();

		if(!oi.getAutoTargetLift()){
			drive.stoppedAligningToLift();
		}

		// choose the type of driving.
		if(oi.getAutoTargetBoiler()) {
			drive.autoTargetToBoiler();
		} else if (oi.getStrafeForWidthOfGearSpokeButtons()) {
			//do nothing as commands will take over
		} else if( oi.getAutoTargetLift()) {
			drive.autoTargetToLift();
			// KBS: TEMPORARY CODE FOR TESTING FLOOR PICKUP, don't have alignment take over drive
			//drive.hDrive(Robot.oi.getDriveThrottle(), Robot.oi.getSteeringX(), Robot.oi.getStrafeThrottle(), Robot.oi.getQuickTurn(), Robot.oi.getSlowModifier());
		} else if ( oi.getGoToHeadingButtons()) {
			//	do nothing as commands will cause robot to go to heading
		} else if (oi.getPlaceGearButton()){
			drive.placeGear();
		}else{
			drive.hDrive(Robot.oi.getDriveThrottle(), Robot.oi.getSteeringX(), Robot.oi.getStrafeThrottle(), Robot.oi.getQuickTurn(), Robot.oi.getSlowModifier());
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	//	@Override
	public void testPeriodic() {
		updateSmartDashboard();
		updateImgResults();
	}

	private long SMART_DASHBOARD_UPDATE_INTERVAL = 250; // update 4 times per second.
	private long nextSmartDashboardUpdate = System.currentTimeMillis();

	public void updateSmartDashboard()	{
		try{

			if (System.currentTimeMillis() > nextSmartDashboardUpdate) {
				SmartDashboard.putBoolean("FMS Checked In", MayhemIterativeRobot.isCheckedIn());
				SmartDashboard.putNumber("Disabled loop count",  disabledLoopCount);

				autonomous.updateSmartDashboard();
				shooter.updateSmartDashboard();
				drive.updateSmartDashboard();
				climber.updateSmartDashboard();
				gearFloorPickerUpper.updateSmartDashboard();
				
				//DriverStation.reportError("BoilerCenterX" + getBoilerCenterX(), false);

				//				SmartDashboard.putBoolean("SDB-AC", smartDashboardPercievedColor());
				//				SmartDashboard.putBoolean("SDB-MUIM", smartDashboardMadeItsMindUp());
				//				SmartDashboard.putNumber("SmartDashboardPercievedColor", fetchAllianceColorResponse());

				nextSmartDashboardUpdate += SMART_DASHBOARD_UPDATE_INTERVAL;
				drive.constantUpdateSmartDashboard();

				SmartDashboard.putNumber("Boiler Distance Measurement", getBoilerTop());

				SmartDashboard.putBoolean("Alliance Color", autonomous.isRed());
			}
		} 
		catch (Exception e)	{
			return;
		}
	}


	public void initSmartDashboard(){
		//placeholder values to create SDB "regions" - SSM
		SmartDashboard.putBoolean("1", true);
		SmartDashboard.putBoolean("2", true);
		SmartDashboard.putBoolean("3", true);
		SmartDashboard.putBoolean("4", true);
		SmartDashboard.putBoolean("5", true);
		SmartDashboard.putBoolean("6", true);
		SmartDashboard.putBoolean("7", true);
		SmartDashboard.putBoolean("8", true);		
	}

	/* 
	 * Functions for camera image processing via network tables
	 */

	// NOTE:  By convention, data in the "ImgResults" array is as follows:
	//       imgResults[0] is frameNumber
	//       imgResults[1] is centerX
	//    (1000.0 is a "magic number" meaning no target found by image processing)
	//    (1001.0 is a "magic number" meaning no info in network tables

	//Boiler: 10.15.19.11
	//Gear:   10.15.19.12

	private static final double[] DEFAULT_IMG_RESULTS = {0.0, 1001.0};
	private static double[] boilerRedCameraLatestImgResults = {0.0, 1001.0};
	private static int boilerRedCameraLatestFrameNum = 0;
	private static int boilerRedCameraLatestCenterX = 1001;
	private static int boilerRedCameraLatestTop = 1000;
	private static double boilerRedCameraLatestImageHeading = 0.0;

	private static double[] boilerBlueCameraLatestImgResults = {0.0, 1001.0};
	private static int boilerBlueCameraLatestFrameNum = 0;
	private static int boilerBlueCameraLatestCenterX = 1001;
	private static int boilerBlueCameraLatestTop = 1000;
	private static double boilerBlueCameraLatestImageHeading = 0.0;

	//gearImgResults: {frameNum, centerX, numTargets, centerY}
	//TODO: What's the best default numTargets? -@SSM
	private static final double[] DEFAULT_GEAR_IMG_RESULTS = {0.0, 1001.0, 1001.0, 1001.0};
	private static double[] gearLatestImgResults = {0.0,  1001.0, 1001.0, 1001.0};
	private static int gearLatestFrameNum = 0;
	private static int gearLatestCenterX = 1001;
	private static int gearLatestCenterY = 1001;
	private static int gearLatestNumTargets = 1001;
	private static double gearLatestImageHeading = 0.0;

	private static void updateImgResults() {
		updateBoilerRedCameraImageResults();
		updateBoilerBlueCameraImageResults();
		updateGearImageResults();

	}
	private static void updateBoilerRedCameraImageResults(){
		boilerRedCameraLatestImgResults = table.getNumberArray("BoilerImgResultsRed", DEFAULT_IMG_RESULTS);
		// check to see if these are new results
		if ( (int) boilerRedCameraLatestImgResults[0] != boilerRedCameraLatestFrameNum) 
		{
			boilerRedCameraLatestFrameNum = (int) boilerRedCameraLatestImgResults[0];
			boilerRedCameraLatestCenterX = (int) boilerRedCameraLatestImgResults[1];
			boilerRedCameraLatestTop = (int)boilerRedCameraLatestImgResults[2];
			boilerRedCameraLatestImageHeading = drive.getHeadingForCapturedImage();
			SmartDashboard.putNumber("AutoAlign Error", boilerRedCameraLatestCenterX);
		}
	}

	private static void updateBoilerBlueCameraImageResults(){
		boilerBlueCameraLatestImgResults = table.getNumberArray("BoilerImgResultsBlue", DEFAULT_IMG_RESULTS);
		// check to see if these are new results
		if ( (int) boilerBlueCameraLatestImgResults[0] != boilerBlueCameraLatestFrameNum) 
		{
			boilerBlueCameraLatestFrameNum = (int) boilerBlueCameraLatestImgResults[0];
			boilerBlueCameraLatestCenterX = (int) boilerBlueCameraLatestImgResults[1];
			boilerBlueCameraLatestTop = (int)boilerBlueCameraLatestImgResults[2];
			boilerBlueCameraLatestImageHeading = drive.getHeadingForCapturedImage();
			SmartDashboard.putNumber("AutoAlign Error", boilerBlueCameraLatestCenterX);
		}
	}

	private static void updateGearImageResults(){
		gearLatestImgResults = table.getNumberArray("GearImgResults", DEFAULT_IMG_RESULTS);
		// check to see if these are new results
		if ( (int) gearLatestImgResults[0] != gearLatestFrameNum) 
		{
			gearLatestFrameNum = (int) gearLatestImgResults[0];
			gearLatestCenterX = (int) gearLatestImgResults[1];
			gearLatestNumTargets = (int) gearLatestImgResults[2];
			gearLatestCenterY = (int) gearLatestImgResults[3];
			gearLatestImageHeading = drive.getHeadingForCapturedImage();
			SmartDashboard.putNumber("AutoAlign Error", gearLatestCenterX);
		}
	}

	public static int getBoilerFrameNum() {
		if(autonomous.isRed()){
			return boilerRedCameraLatestFrameNum;
		}else{
			return boilerBlueCameraLatestFrameNum;
		}	
	}

	public static int getBoilerCenterX() {
		if (autonomous.isRed()) {
			return boilerRedCameraLatestCenterX;
		} else {
			return boilerBlueCameraLatestCenterX;
		}
	}

	public static int getGearFrameNum() {
		return gearLatestFrameNum;	
	}

	public static int getGearCenterX() {
		return gearLatestCenterX;
	}

	public static int getGearCenterY(){
		return gearLatestCenterY;
	}

	public static int getGearNumberOfTargets(){
		return gearLatestNumTargets;
	}

	public static double getBoilerImageHeading() {
		if(autonomous.isRed()){
			return boilerRedCameraLatestImageHeading;
		}else{
			return boilerBlueCameraLatestImageHeading;
		}
	}

	public static double getGearImageHeading() {
		return gearLatestImageHeading;
	}

	public static int getBoilerTop(){
		if (autonomous.isRed()) {
			return boilerRedCameraLatestTop;
		} else {
			return boilerBlueCameraLatestTop;
		}
	}

	public static boolean isBoilerVisible() {
		return (getBoilerCenterX() < 1000);
	}

	//TODO: @SSM - should this also look for the Y-component of the gear target
	public static boolean isGearVisible() {
		return (getGearCenterX() < 1000);
	}

	public void postAllianceColor(){
		//0 = unknown
		//1 = RED
		//2 = BLUE
		int val = 1;
		if (!autonomous.isRed()) {
			val = 2;
		}

		table.putNumber("alliance", val);
	}

	//	final int UNDECIDED = 0;
	//	final int RED = 1;
	//	final int BLUE = 2;
	//
	//	public boolean smartDashboardMadeItsMindUp(){
	//		return (fetchAllianceColorResponse() != UNDECIDED);
	//	}
	//	public boolean smartDashboardPercievedColor(){
	//		SmartDashboard.putNumber("getCrosshairX()", table.getNumber("crosshair", -1.0));
	//		return (fetchAllianceColorResponse() != BLUE);
	//
	//	}
	//	public int fetchAllianceColorResponse(){
	//		//0 = unknown
	//		//1 = RED
	//		//2 = BLUE
	//		return (int)table.getNumber("respones", UNDECIDED);
	//	}
}