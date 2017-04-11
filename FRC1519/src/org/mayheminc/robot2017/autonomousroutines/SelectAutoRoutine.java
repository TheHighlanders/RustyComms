package org.mayheminc.robot2017.autonomousroutines;

import org.mayheminc.robot2017.commands.Print;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class SelectAutoRoutine extends CommandGroup {

    public SelectAutoRoutine() {
        //This is the default auto routine
    	//This routine does NOT actively select anything.
    	addSequential(new Print("No auto routine selected, doing nothing"));
    }
}
