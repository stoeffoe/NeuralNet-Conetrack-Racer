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
    public void multipyDifferentSizedMatrices(){
        double[][] expected = {{30.14, 71.24, 3.91},
                               {97.7, 303.2, 106.3}};
        
        double[][] actual = MatMath.multiply(mat_3x2, mat_3x3);

        assertArrayEquals(expected, actual);
    }


    @Test
    public void magnitude(){
        double expected = 5.0;
        double actual = MatMath.magnitude(mat_2x1);
        double delta = 0.00000001;
        assertEquals(expected, actual, delta);
    }


    @Test
    public void sum(){
        double expected = 7.0;
        double actual = MatMath.sum(mat_2x1);
        double delta = 0.00000001;
        assertEquals(expected, actual, delta);
    }


    @Test
    public void normaliseVector(){
        double[][] expected = {{3.0/5.0, 4.0/5.0}};
        double[][] actual = MatMath.norm(mat_2x1);

        assertArrayEquals(expected, actual);
    }


    @Test
    public void mapSingleMatrix(){
        double[][] expected = {{13.1, 15.3, 12},
                               {  13,   14, 19}};;
        
        double[][] actual = MatMath.map(mat_3x2, (a) -> a + 10);

        assertArrayEquals(expected, actual);
    }


    @Test
    public void raiseMatrixToPower() {
        double[][] expected = {{3.0*3*3, 4.0*4*4}};
        double[][] actual = MatMath.pow(mat_2x1, 3);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void transposeMatrix() {
        double[][] expected =   {{3.1, 3},
                                 {5.3, 4},
                                 {  2, 9},};
        double[][] actual = MatMath.transpose(mat_3x2);

        assertArrayEquals(expected, actual);
    }

}

