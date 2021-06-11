/*
- Ruben Hiemstra 0924010
- Stefan Beenen 0963586
- Jordy Weijgertse 0974347
*/

package Machine_Learning;

import java.util.Arrays;

public class Data {
    private double[][] desiredValue;
    private double[] matrix;

    public double[] lidar ;
    public double[] a  ;
    

    public Data(double[] _matrix, double[] _desiredValue) {
        this.matrix =  getRay(_matrix);
        double[] _desiredValueA = new double[1];  

        _desiredValueA[0] = normalize(_desiredValue[0],-50,50);
        this.desiredValue = MatMath.fromList(_desiredValueA);

        this.lidar = _matrix.clone();
        this.a = _desiredValue.clone(); 
        
    }

    public double[] getMatrix() {
        return matrix;
    }

    public double[][] getDesiredValue() {
        return desiredValue;
    }

    public double[] getRay(double[] lidarDistances ){
        double[][] rays = new double[8][15]; 

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 15; j++) {
                rays[i][j] = lidarDistances[(i*15)+j];
            }
        }

        for (double[] ds : rays) {
            Arrays.sort(ds);
        }

        double[] tempRays = new double[8];
        for (int k = 0; k < rays.length; k++) {
            tempRays[k] = normalize(rays[k][0],0.5,5);
        }

        return tempRays;
    }

    double normalize(double value, double min, double max){
        if (value > max){
            return 1.0;
        } else if (value < min){
            return 0.0;
        } else {
            return (value - min) / (max - min);
        }
    }
}
