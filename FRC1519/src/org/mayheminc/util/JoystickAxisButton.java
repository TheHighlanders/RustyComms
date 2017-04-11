/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mayheminc.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 *
 * @author Team1519
 */
public class JoystickAxisButton extends Button {
    public static final int BOTH_WAYS = 1;
    public static final int POSITIVE_ONLY = 2;
    public static final int NEGATIVE_ONLY = 3;
    
    private static final double AXIS_THRESHOLD = 0.2;
    
    private Joystick joystick;
    private Joystick.AxisType axis;
    private int axisInt;
    private int direction;
    
    public JoystickAxisButton(Joystick stick, Joystick.AxisType axis) {
        this(stick, axis, BOTH_WAYS);
    }
    
    public JoystickAxisButton(Joystick stick, Joystick.AxisType axis, int direction) {
        joystick = stick;
        this.axis = axis;
        this.direction = direction;
    }
    
    public JoystickAxisButton(Joystick stick, int axis) {
        this(stick, axis, BOTH_WAYS);
    }
    
    public JoystickAxisButton(Joystick stick, int axis, int direction) {
        joystick = stick;
        this.axis = null;
        axisInt = axis;
        this.direction = direction;
    }
    
    public boolean get() {
        switch (direction) {
            case BOTH_WAYS:
                if (axis != null) {
                    return Math.abs(joystick.getAxis(axis)) > AXIS_THRESHOLD;
                }
                else {
                    return Math.abs(joystick.getRawAxis(axisInt)) > AXIS_THRESHOLD;
                }
            
            case POSITIVE_ONLY:
                if (axis != null) {
                    return joystick.getAxis(axis) > AXIS_THRESHOLD;
                }
                else {
                    return joystick.getRawAxis(axisInt) > AXIS_THRESHOLD;
                }
                
            case NEGATIVE_ONLY:
                if (axis != null) {
                    return joystick.getAxis(axis) < -AXIS_THRESHOLD;
                }
                else {
                    return joystick.getRawAxis(axisInt) < -AXIS_THRESHOLD;
                }
        }
        
        return false;
    }
}
