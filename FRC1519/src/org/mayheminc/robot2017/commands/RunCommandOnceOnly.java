package org.mayheminc.robot2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.*;

/**
 *
 */
public class RunCommandOnceOnly extends CommandGroup {

    public RunCommandOnceOnly(Command comm) {
        addSequential(comm);
        addSequential(new Delay(1000000));
    }
}
