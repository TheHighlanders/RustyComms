
package org.usfirst.frc.team6201.robot;

import org.usfirst.frc.team6201.robot.subsystems.DataLoggerFetcher;
import org.usfirst.frc.team6201.robot.subsystems.DriveTrain;
import org.usfirst.frc.team6201.robot.subsystems.GearVisionAuto;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This class is automatically run when the robot first boots up.
 * Each of the methods are called at their appropriate parts of the game.
 * 
 * @author David Matthews
 * 
 */
public class Robot extends IterativeRobot {

	
	/**
	 * Creates a DriveTrain subsystem object which enables moving the robot around.
	 */
	public static final DriveTrain dt = new DriveTrain();

	/**
	 * Creates a DataLoggerFetcher subsystem object which starts the logging publisher thread for the Data Logger.
	 */
	public static DataLoggerFetcher dlf = new DataLoggerFetcher();
	
	/**
	 * Creates a GearVisionAuto subsystem which prepares for receiving of target information,
	 * and informs the DS when the robot is able to complete the gear delivery routine. 
	 */
	public static GearVisionAuto gva = new GearVisionAuto();
	
	/**
	 * Declare the Operator Interface object.
	 * DO NOT initialize it here that would cause No Robot Code to occur.
	 */
	public static OI oi;
	
	// provides a place to store which command we want to use in the auto section of the match.
	// See robotInit() for how this is set.
	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 * TODO: look up how calibration works if we have multiple gyro objects. Maybe chief delphi
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		dt.calibrateGyro();
		
		//chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		//TODO: implement this for our different auto routines.
		SmartDashboard.putData("Auto mode", chooser);
	}

	/**
	 * This method is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	/**
	 * This method is called periodically when the robot is in Disabled mode.
	 * put anything here you want to run periodically when the robot is disabled
	 */
	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This method is called when the robot is entering autonomous. Put any setup for autonomous here.
	 * Currently, we grab the selected autonomous command from the smart dashboard(a screen on the driverstation), and schedule it to run here.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		
//		  String autoSelected = SmartDashboard.getString("Auto Selector", "Default"); switch(autoSelected) {
//		  	case "My Auto": autonomousCommand = new MyAutoCommand(); break;
//		  	case "Default Auto": default: autonomousCommand = new ExampleCommand(); break;
//		  	}
		 

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This method is called just before the tele operated period of the begins. Add any setup here.
	 * 
	 */
	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
