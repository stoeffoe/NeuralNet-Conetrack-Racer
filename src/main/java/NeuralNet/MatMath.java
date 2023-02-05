package NeuralNet;

import java.util.Arrays;

import NeuralNet.ActivationFunction.ActivationFunction;

import java.lang.IllegalArgumentException;


public final class MatMath {

    private MatMath(){} // private constructor; no initialization, only static methods


    
    /**
     * Multiplies two matrices (sum of all weighted neurons) and applies an activation function on the result
     * both in a single matrix iteration to speed up the neural network
     * @param mA First matrix operand (weights of the edges)
     * @param mB Second matrix operand (input neurons)
     * @param af ActivationFunction (i.e. sigmoid)
     * @return Matrix with the size: num-cols-mB x num-rows-mA (output neurons)
     * @throws IllegalArgumentException when matrices are not multipliable
     */
    public static double[][] multiplyAndActivate(double[][] mA, double[][] mB, ActivationFunction af) throws IllegalArgumentException {
        
        if (!areMultiplyable(mA, mB)) throw new IllegalArgumentException("Matrix sizes are not compatible");

        int rows = mA.length;
        int cols = mB[0].length;
        int numOfCalcs = mA[0].length;

        double[][] resultMatrix = new double[rows][cols];

        for (int r=0; r<rows; r++){
            for (int c=0; c<cols; c++){

                double sum = 0;
                for (int i=0; i<numOfCalcs; i++){
                    sum += mA[r][i] * mB[i][c];
                }
                resultMatrix[r][c] = af.calculateActivation(sum);
            }
        }

        return resultMatrix;
    }

    /**
     * Calculates the sum of the difference squared. Used to determine the square distance between the dataset and the predicted output 
     * @param mA First matrix operand
     * @param mB Second matrix operand
     * @return The sum of the difference squared
     */
    public static double sumSquaredErrors(double[][] mA, double[][] mB) {

        if (!matricesEqualSize(mA, mB)) throw new IllegalArgumentException("Matrix sizes are not compatible");

        int rows = mA.length;
        int cols = mA[0].length;

        double sumSquaredError = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double error = mA[r][c] - mB[r][c];
                sumSquaredError += error*error;
            }
        }

        return sumSquaredError;
    }

    /**
     * Creates a deep copy of a double matrix with 3 dimensions
     */
    public static double[][][] copyOf3Dim(double[][][] array) {
   
        int xlen = array.length;
        double[][][] copy = new double[xlen][][];
    
        for (int x = 0; x < xlen; x++) {
            int ylen = array[x].length;  
            copy[x] = new double[ylen][];
    
            for (int y = 0; y < ylen; y++) {  
                copy[x][y] = Arrays.copyOf(array[x][y], array[x][y].length);
            }  
        } 
        return copy;
    }

    /**
     * Maps all the values in the list from the given range on to a range of 0 to 1
     * @param list
     * @param min
     * @param max
     * @return A list with values between 0 and 1
     */
    public static double[] normalize(double[] list, double min, double max){
        double[] normalizedList = new double[list.length];
        for (int i = 0; i < list.length; i++){
            normalizedList[i] = normalize(list[i], min, max);
        }
        return normalizedList;
    }

    /**
     * Maps the value from the given range on to a range of 0 to 1 
     * @param value
     * @param min
     * @param max
     * @return A value between 0 and 1
     */
    public static double normalize(double value, double min, double max){
        if (value > max){
            return 1.0;
        } else if (value < min){
            return 0.0;
        } else {
            return (value - min) / (max - min);
        }
    }

    /**
     * Maps the value from a range of 0 to 1 to it's absolute value within the given range
     * @param normalizedValue The value between 0 and 1
     * @param min
     * @param max
     * @return A value between min and max
     */
    public static double denormalize(double normalizedValue, double min, double max){
        return (normalizedValue * (max - min)) + min;
    }

    /**
     * Turns a list of values into a column vector
     * @param list Values in the vector
     * @return A 2d array with the values stacked vertically
     */
    public static double[][] fromList(double[] list){
        double[][] newVector = new double[list.length][1];
        for (int i=0; i<list.length; i++) {
            newVector[i][0] = list[i];
        }
        return newVector;
    }

    /**
     * Prints the values of the matrix on the command line
     * @param mA The matrix to be printed
     */
    public static void print(double[][] mA) {
        System.out.println(Arrays.deepToString(mA));
    }

    public static boolean areMultiplyable(double mA[][], double mB[][]) {
        return mA[0].length == mB.length; 
    }

    public static boolean matricesEqualSize(double mA[][], double mB[][]) {
        return mA.length == mB.length && mA[0].length == mB[0].length;
    }
}
