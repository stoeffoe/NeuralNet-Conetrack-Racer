package NeuralNet;

public class NNdata implements Comparable<NNdata>  {
    public double[][][] nn;
    public double error;

    public NNdata(double[][][] neuralNet, double error){
        this.nn = neuralNet;
        this.error = error;
    }

    @Override
    public int compareTo(NNdata other) {
        return (this.error < other.error) ? -1 : (this.error > other.error) ? 1 : 0;
    }
}