
package org.usfirst.frc.team6201.robot;

import org.usfirst.frc.team6201.robot.commands.DoNothingAuto;
import org.usfirst.frc.team6201.robot.commands.gears.BoilerStationAutoCmdGroup;
import org.usfirst.frc.team6201.robot.commands.gears.CenterStationAutoCmdGroup;
import org.usfirst.frc.team6201.robot.commands.gears.LoaderStationAutoCmdGroup;
//import org.usfirst.frc.team6201.robot.commands.rgb.AutoRGB;
//import org.usfirst.frc.team6201.robot.commands.rgb.Final30RGB;
//import org.usfirst.frc.team6201.robot.commands.rgb.TeleOpRGB;
import org.usfirst.frc.team6201.robot.dataLogger.DataCollator;
import org.usfirst.frc.team6201.robot.subsystems.DataLoggerFetcher;
import org.usfirst.frc.team6201.robot.subsystems.DriveTrain;
import org.usfirst.frc.team6201.robot.subsystems.GearVisionAuto;
import org.usfirst.frc.team6201.robot.subsystems.Pneumatics;
//import org.usfirst.frc.team6201.robot.subsystems.RobotRGB;
import org.usfirst.frc.team6201.robot.subsystems.RopeClimber;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is automatically run when the robot first boots up. Each of the
 * methods are called at their appropriate parts of the game.
 * 
 * @author David Matthews
 * 
 */
public class Robot extends IterativeRobot {

	/**
	 * Creates a RobotRGB subsystem object which enables colour changing for robot LEDs.
	 */
	//public static final RobotRGB rgb = new RobotRGB();

	/**
	 * Boolean for enabling and disabling CERTAIN parts of the robot. Does not completely disable the robot.
	 * 
	 * @param False Robot will be "disabled." Functions such as driving, rope climbing, pneumatics, etc. will not be available.
	 * @param True Robot is "enabled." Everything will function properly and the robot can be driven.
	 */
	public static boolean buttonDisable = false;
	
	/**
	 * Creates a DriveTrain subsystem object which enables moving the robot
	 * around.
	 */
	public static final DriveTrain dt = new DriveTrain();

	
	public static final Pneumatics pn = new Pneumatics();
	/**
	 * Creates a DataLoggerFetcher subsystem object which starts the logging
	 * publisher thread for the Data Logger.
	 */
	public static DataLoggerFetcher dlf = new DataLoggerFetcher();

	/**
	 * Creates a GearVisionAuto subsystem which prepares for receiving of target
	 * information, and informs the DS when the robot is able to complete the
	 * gear delivery routine.
	 */
	public static GearVisionAuto gva = new GearVisionAuto();

	/**
	 * Creates a RopeClimber subsystem object which enables rope-climbing
	 * capabilities.
	 */
	public static RopeClimber rc = new RopeClimber();

	/**
	 * Declare the Operator Interface object. DO NOT initialize it here; that
	 * would cause No Robot Code to occur.
	 */
	public static OI oi;

	// provides a place to store which command we want to use in the auto
	// section of the match.
	// See robotInit() for how this is set.
	Command autonomousCommand;
	
	//private Command rgbCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code. 
	 */
	@Override
	public void robotInit() {
		
		oi = new OI();
		dt.calibrateGyro();
		DataCollator.state.setVal("RobotInit");

		SmartDashboard.putString("Auto", "");
		SmartDashboard.putNumber("TurboSpeed", 0.95);
		
	}

	/**
	 * This method is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit() {
		DataCollator.state.setVal("RobotDisabledInit");
		SmartDashboard.putNumber("Turning Tuning", Robot.gva.getTuning());
		SmartDashboard.putNumber("LoaderAutoTargetLowThresh",  0.05);
		SmartDashboard.putNumber("LoaderAutoTargetHighThresh", 0.95);
	}

	/**
	 * This method is called periodically when the robot is in Disabled mode.
	 * put anything here you want to run periodically when the robot is disabled
	 */
	@Override
	public void disabledPeriodic() {

		Robot.gva.setTuning(SmartDashboard.getNumber("Turning Tuning", 0));
		DataCollator.state.setVal("RobotDisabledPeriodic");
		Scheduler.getInstance().run();
		if (dlf.getStopOnNextDisable()) {
			DriverStation.reportError("Shutting Jetson Down", false);
			dlf.setStopOnNextDisable(false);
			dlf.stopLoggingRecorder();
		}
	}

	/**
	 * This method is called when the robot is entering autonomous. Put any
	 * setup for autonomous here. Currently, we grab the selected autonomous
	 * command from the smart dashboard(a screen on the driverstation), and
	 * schedule it to run here.
	 */
	@Override
	public void autonomousInit() {
		
		DataCollator.state.setVal("RobotAutonomousInit");
		switch (SmartDashboard.getString("Auto", "D")){
		case "B" :
			autonomousCommand = new BoilerStationAutoCmdGroup();
			break;
		case "L" :
			autonomousCommand = new LoaderStationAutoCmdGroup();
			break;
		case "C" :
			autonomousCommand = new CenterStationAutoCmdGroup();
			break;
		default :
			autonomousCommand = new DoNothingAuto();
			break;
		}

		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
		
		//rgbCommand = new AutoRGB();
		//rgbCommand.start();
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		DataCollator.state.setVal("RobotAutonomousPeriodic");
		Scheduler.getInstance().run();
	}

	/**
	 * This method is called just before the tele-operated period of the begins.
	 * Add any setup here.
	 * 
	 */
	@Override
	public void teleopInit() {
		DataCollator.state.setVal("RobotTeleopInit");
		if (DriverStation.getInstance().isFMSAttached()) {
			dlf.setStopOnNextDisable(true);
		}

		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		
		//rgbCommand.cancel();
		//rgbCommand = new TeleOpRGB();
		//rgbCommand.start();
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		DataCollator.state.setVal("RobotTeleopPeriodic");
		Scheduler.getInstance().run();
		
		if(Timer.getMatchTime() > 120) {
			
			//rgbCommand.cancel();
			//rgbCommand = new Final30RGB();
			//rgbCommand.start();
			
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
