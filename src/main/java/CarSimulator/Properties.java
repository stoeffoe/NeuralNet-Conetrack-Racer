package CarSimulator;

class Properties{
    private double[] lidarDistances;
    private long lidarHalfApertureAngle;

    Properties(){

    }

    public void setLidarDistances(double[] lidarDistances){
        this.lidarDistances = lidarDistances;
    }

    public double[] getLidarDistances(){
        return lidarDistances;
    }

    public void setLidarHalfApertureAngle(long lidarHalfApertureAngle){
        this.lidarHalfApertureAngle = lidarHalfApertureAngle;
    }

    public long getLidarHalfApertureAngle(){
        return lidarHalfApertureAngle;
    }
}
