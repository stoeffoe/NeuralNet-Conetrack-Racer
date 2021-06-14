package AutoCoureur;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;

public final class UserInputControls implements NativeKeyListener, NativeMouseMotionListener {

    private static UserInputControls singletonInstance;

    private double steeringAngle;
    private double targetVelocity;

    private static int screenWidth = 1920;
    private static double steeringFactor = -0.1;

    private static double speedIncrement = 0.45;

    /**
     * Private constructor, 
     * initializes the system wide controls to control the car
     */
    private UserInputControls() {
        try {
            LogManager.getLogManager().reset();
            Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.OFF);

            GlobalScreen.addNativeMouseMotionListener(this);
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @return Singleton instance
     */
    public static UserInputControls getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new UserInputControls();
        }

        return singletonInstance;
    }

    /**
     * @return Desired steering angle from user input
     */
    public double getSteeringAngle() {
        return steeringAngle;
    }

    /**
     * @return Desired target velocity from user input
     */
    public double getTargetVelocity() {
        return targetVelocity;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent ev) {
        switch(ev.getKeyCode()){
            case NativeKeyEvent.VC_W:
                this.targetVelocity += speedIncrement;
                break;

            case NativeKeyEvent.VC_S:
                this.targetVelocity -= speedIncrement;
                break;
        }
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent mouse) {
	this.steeringAngle = (mouse.getX() - (screenWidth/2)) * steeringFactor;
    }

    // Unused mandatory methods
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}

    @Override
    public void nativeMouseDragged(NativeMouseEvent e) {}
}
