package CarSimulator;

import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

public class ControlsTest {
    static Controls controls;
    
    @Before
    public void initializeProperties(){
        controls = new Controls();
    }

    @Test
    public void controlsConstructorTest(){
        double steeringAngle = 45.0;
        double targetVelocity = 1.0;
        controls = new Controls(steeringAngle, targetVelocity);
        assertEquals(steeringAngle, controls.getSteeringAngle(), 0.001);
        assertEquals(targetVelocity, controls.getTargetVelocity(), 0.001);
    }

    @Test
    public void toJsonTest(){
        double steeringAngle = 45.0;
        double targetVelocity = 1.0;
        controls = new Controls(steeringAngle, targetVelocity);
        Gson gson = new Gson();
        String expected = "{\"steeringAngle\":45.0,\"targetVelocity\":1.0}";
        String actual = gson.toJson(controls);
        assertEquals(expected, actual);
    }

    @Test
    public void setSteeringAngleTest(){
        controls.setSteeringAngle(45.0);
        double expected = 45.0;
        double actual = controls.getSteeringAngle();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void setSteeringAngleTooHighTest(){
        controls.setSteeringAngle(Controls.maxSteeringAngle + 10.0);
        double expected = Controls.maxSteeringAngle;
        double actual = controls.getSteeringAngle();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void setSteeringAngleTooLowTest(){
        controls.setSteeringAngle(-Controls.maxSteeringAngle - 10.0);
        double expected = -Controls.maxSteeringAngle;
        double actual = controls.getSteeringAngle();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void setTargetVelocityTest(){
        controls.setTargetVelocity(1.0);
        double expected = 1.0;
        double actual = controls.getTargetVelocity();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void setTargetVelocityTooHighTest(){
        controls.setTargetVelocity(Controls.maxTargetVelocity + 10.0);
        double expected = Controls.maxTargetVelocity;
        double actual = controls.getTargetVelocity();
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void setTargetVelocityTooLowTest(){
        controls.setSteeringAngle(Controls.minTargetVelocity - 10.0);
        double expected = Controls.minTargetVelocity;
        double actual = controls.getTargetVelocity();
        assertEquals(expected, actual, 0.001);
    }
}
