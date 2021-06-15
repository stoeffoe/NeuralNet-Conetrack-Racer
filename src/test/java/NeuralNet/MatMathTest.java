// Author: Stefan Beenen

package NeuralNet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatMathTest {
    
    // Matrix samples for testing

    private double[][] mat_3x3 = {{  -5.6, 3.4, -2.3},
                                  {     5,  -1, -3.2},
                                  {  10.5,  33,   14}};

    private double[][] mat_3x2 = {{3.1, 5.3, 2},
                                  {  3,   4, 9}};

    private double[][] mat_2x1 = {{3.0, 4.0}};

    @Test
    public void mapSingleMatrix(){
        double[][] expected = {{13.1, 15.3, 12},
                               {  13,   14, 19}};;
        
        double[][] actual = MatMath.map(mat_3x2, (a) -> a + 10);

        assertArrayEquals(expected, actual);
    }
}

