package CarSimulator;

class Properties{
    private double[] lidarDistances;
    private long lidarHalfApertureAngle;
    private boolean isOnTrack;
    private double progress;
    private double lapTime;
    private boolean collided;

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

    /**
     * 
     * @return whether the car is on the track or not
     */
    public boolean getIsOnTrack(){
        return isOnTrack;
    }

    /**
     * 
     * @return the progress of the car on the track in percentage
     */
    public double getProgress() {
        return progress;
    }

    /**
     * 
     * @return elasped simulation time
     */
    public double getLapTime() {
        return lapTime;
    }

    /**
     * 
     * @return if the car is colliding
     */
    public boolean getCollided() {
        return collided;
    }
}
