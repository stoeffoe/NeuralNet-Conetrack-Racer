package CarSimulator;

class Controls{
    private double steeringAngle;
    private double targetVelocity;

    Controls(){

    }

    public void setSteeringAngle(double steeringAngle){
        this.steeringAngle = steeringAngle;
    }

    public double getSteeringAngle(){
        return steeringAngle;
    }

    public void setTargetVelocity(double targetVelocity){
        this.targetVelocity = targetVelocity;
    }

    public double getTargetVelocity(){
        return targetVelocity;
    }
}
