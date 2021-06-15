// Author: Stefan Beenen

package NeuralNet;

import java.util.Arrays;

import NeuralNet.ActivationFunction.ActivationFunction;

import java.lang.IllegalArgumentException;


public final class MatMath {

    private MatMath(){} // private constructor; no initialization, only static methods


    public static double[][] pow(double[][] mA, double n) {
        return map(mA, a -> Math.pow(a,n));
    }

        double[][] resultMatrix = new double[rows][cols];

    public static double[][] sub(double[][] mA, double[][] mB) {
        int rows = mA.length;
        int cols = mA[0].length;

        double[][] resultMatrix = new double[cols][rows];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                resultMatrix[c][r] = mA[r][c] - mB[r][c];
            }
        }

        return resultMatrix;
    }

                double sum = 0;
                for (int i=0; i<numOfCalcs; i++){
                    sum += mA[r][i] * mB[i][c];
                }
                resultMatrix[r][c] = af.calculateActivation(sum);
            }
        }

    public static double sumSquaredErrors(double[][] mA, double[][] mB) {
        int rows = mA.length;
        int cols = mA[0].length;

        double sse = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double error = mA[r][c] - mB[r][c];
                sse += error*error;
            }
        }

        return sse;
    }
    

    public static double[][] fromList(double[] list){
        double[][] newVector = new double[list.length][1];
        for (int i=0; i<list.length; i++) {
            newVector[i][0] = list[i];
        }
        return newVector;
    }


    public static double[][] map(double[][] mA, double[][] mB, BiFunction<Double, Double, Double> fn) throws IllegalArgumentException { 
        
        if (!matricesEqualSize(mA, mB)) throw new IllegalArgumentException("Matrices must be equal in size");

        int rows = mA.length;
        int cols = mA[0].length;

        double[][] resultMatrix = new double[rows][cols];

        for (int r=0; r<rows; r++){
            for (int c=0; c<cols; c++){
                resultMatrix[r][c] = fn.apply(mA[r][c], mB[r][c]);
            }
        }

        return resultMatrix;
    }


    public static double[][] map(double[][] mA, Function<Double, Double> fn) { 
        int rows = mA.length;
        int cols = mA[0].length;

        double[][] resultMatrix = new double[rows][cols];

        for (int r=0; r<rows; r++){
            for (int c=0; c<cols; c++){
                resultMatrix[r][c] = fn.apply(mA[r][c]);
            }
        }

        return resultMatrix;
    }


    public static String toString(double[][] mat) {
        return Arrays.deepToString(mat);
    }


    public static void print(double[][] mA) {
        System.out.println(toString(mA));
    }
    
    
    public static double[][] multiply(double[][] mA, double[][] mB) throws IllegalArgumentException {
        
        if (!areMultiplyable(mA, mB)) throw new IllegalArgumentException("Matrix sizes are not compatible");

        int rows = mA.length;
        int cols = mB[0].length;
        int numOfCalcs = mA[0].length;

        double[][] resultMatrix = new double[rows][cols];

        for (int r=0; r<rows; r++){
            for (int c=0; c<cols; c++){

                for (int i=0; i<numOfCalcs; i++){
                    resultMatrix[r][c] += mA[r][i] * mB[i][c];
                }

            }
        }

        return resultMatrix;
    }


    public static boolean areMultiplyable(double mA[][], double mB[][]) {
        return mA[0].length == mB.length; 
    }


    public static boolean isRowVector(double vA[][]) {
        return vA.length == 1;
    }


    public static boolean isColVector(double vA[][]) {
        return vA[0].length == 1;
    }

    
    public static boolean matricesEqualSize(double mA[][], double mB[][]) {
        return mA.length == mB.length && mA[0].length == mB[0].length;
    }
}