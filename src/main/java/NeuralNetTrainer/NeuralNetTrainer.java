package NeuralNetTrainer;

public class NeuralNetTrainer extends Thread{
    public void train( double weightChange) {
        int[] bestEdgeIndex = new int[3];
        double bestEdgeWeightChange =0;
        double lowestAvgError = calculateAverageError(dataSet);

        for (int layer = 0; layer < edges.length; layer++) {
            for (int row = 0; row < edges[layer].length; row++) {
                for (int col = 0; col < edges[layer][row].length; col++) {

                        int[] currentIndex = {layer, row, col};

                        double avgErrorPlus = calculateErrorEdgeChange(dataSet, currentIndex, weightChange);
                        double avgErrorMinus = calculateErrorEdgeChange(dataSet, currentIndex, -weightChange);
                
                        if (lowestAvgError < avgErrorPlus && lowestAvgError < avgErrorMinus ){
                            continue;
                        }

                        if (avgErrorMinus < avgErrorPlus){
                            lowestAvgError = avgErrorMinus ;  
                            bestEdgeWeightChange = -weightChange;
                        } else {
                            lowestAvgError = avgErrorPlus;
                            bestEdgeWeightChange = weightChange;
                        }

                        bestEdgeIndex = currentIndex;
                    }
                }
            }

        edges[bestEdgeIndex[0]][bestEdgeIndex[1]][bestEdgeIndex[2]] += bestEdgeWeightChange;
    }

    private double calculateError(Data data) {
        double[][] target = data.getDesiredValue();
        double[][] output = predict(data.getMatrix());

        return MatMath.sumOfSquares(MatMath.sub(target, output));
    }

    public double calculateAverageError(Data[] dataSet) {
        double errorSum = 0;
        for (Data data : dataSet) {
            errorSum += calculateError(data);
        }

        return errorSum / dataSet.length;
    }

    private double calculateErrorEdgeChange(Data[] dataSet, int[] edgeIndex, double weightChange) {
        edges[edgeIndex[0]][edgeIndex[1]][edgeIndex[2]] += weightChange;
        double avgError = calculateAverageError(dataSet);
        edges[edgeIndex[0]][edgeIndex[1]][edgeIndex[2]] -= weightChange;
        
        return avgError;
    }

    public void fit(Data[] dataSet, double weightChange, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            train(dataSet, weightChange);
        }
    }
    
}
