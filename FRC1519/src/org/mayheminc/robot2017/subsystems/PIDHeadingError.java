package org.mayheminc.robot2017.subsystems;

import edu.wpi.first.wpilibj.PIDSource;

import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * 
 * @author user
 * This is a class to hold the Heading error of the drive.
 */
public class PIDHeadingError implements PIDSource
{

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		return m_Error;
	}
	public double m_Error;
}

