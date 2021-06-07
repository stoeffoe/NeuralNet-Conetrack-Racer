package CarSimulator;

class Properties{
    private double[] lidarDistances;
    private long lidarHalfApertureAngle;

    Properties(){}

    /**
     * 
     * @return Array of lidardistances
     */
    public double[] getLidarDistances(){
        return lidarDistances;
    }

    /**
     * 
     * @return Lidar half aperture angle
     */
    public long getLidarHalfApertureAngle(){
        return lidarHalfApertureAngle;
    }
}
