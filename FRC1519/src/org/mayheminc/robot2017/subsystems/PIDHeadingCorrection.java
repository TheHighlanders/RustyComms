package org.mayheminc.robot2017.subsystems;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 * 
 * @author user
 * This class holds the correction that is calculated by the PID Controller.
 */
public class PIDHeadingCorrection implements PIDOutput{

	public double HeadingCorrection;
	@Override
	public void pidWrite(double output) {
		HeadingCorrection = output;
	}

}
