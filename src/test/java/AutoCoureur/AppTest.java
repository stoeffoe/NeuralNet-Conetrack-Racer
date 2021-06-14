package AutoCoureur;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AppTest{
    @Test
    public void noArgumentsTest(){
        App.main(new String[]{});
    }

    @Test
    public void getDataTest(){
        App.main(new String[]{"data"});
        assertEquals("data", App.currentFunction);
    }

    @Test
    public void trainNeuralNetTest(){
        App.main(new String[]{"train"});
        assertEquals("train", App.currentFunction);
    }

    @Test
    public void testNeuralNetTest(){
        App.main(new String[]{"test"});
        assertEquals("test", App.currentFunction);
    }
}
