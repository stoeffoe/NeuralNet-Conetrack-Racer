package AutoCoureur;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;

import CarSimulator.Car;

public class KeyboardController implements NativeKeyListener, NativeMouseMotionListener {

    private double steeringAngle;
    private double targetVelocity;

    public KeyboardController() {

        LogManager.getLogManager().reset();
        Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);

        GlobalScreen.addNativeMouseMotionListener(this);
        
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent ev) {
        switch(ev.getKeyCode()){
            case NativeKeyEvent.VC_W:
                this.targetVelocity = 0.9;
                break;

            case NativeKeyEvent.VC_S:
                this.targetVelocity = 0;
                break;

        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent arg0) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent arg0) {}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
		this.steeringAngle = Math.round(e.getX() - 960)*-0.1;
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
    }
    
    public double getSteeringAngle() {
        return steeringAngle;
    }
    
    public double getTargetVelocity() {
        return targetVelocity;
    }
}

