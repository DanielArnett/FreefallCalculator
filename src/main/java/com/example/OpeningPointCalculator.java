package com.example;

public class OpeningPointCalculator {
    private final double MIN_EXIT_ALTITUDE = 2000;
    private final double MIN_DEPLOYMENT_ALTITUDE = 1000;
    private final double TERMINAL_VELOCITY_IN_MPH = 120;
    public  final double MPH_TO_FPS = 66.0/45.0;
    public  final double FPS_TO_MPH = 1.0 / MPH_TO_FPS;
    private double exitAltitude;
    private double deploymentAltitude;
    private double firstOneThousandFeetInSeconds = 12;
    private double terminalOneThousandFeetTimeInSeconds = 5.5;
    public Winds winds;

    public OpeningPointCalculator(double exitAltitude, double deploymentAltitude) {
        this.setExitAltitude(exitAltitude);
        this.setDeploymentAltitude(deploymentAltitude);
        this.winds = new Winds();
    }

    /**
     * Gets the freefall time in seconds including accelleration.
     * TODO: Make this accurate using actual acceleration and drag as a function of time.
     * @return The number of seconds in freefall
     */
    public double getFreefallTimeInSeconds() {
        double feetAtTerminalVelocity = (exitAltitude - deploymentAltitude - 1000);
        double freefallTimeInSeconds = 0;
        if (1000 < (exitAltitude - deploymentAltitude)) {
            freefallTimeInSeconds += firstOneThousandFeetInSeconds;
            freefallTimeInSeconds += feetAtTerminalVelocity / (TERMINAL_VELOCITY_IN_MPH * MPH_TO_FPS);
        }
        else {
            freefallTimeInSeconds += firstOneThousandFeetInSeconds * (exitAltitude - deploymentAltitude);
        }
        return freefallTimeInSeconds;
    }

    public double getExitAltitude() {
        return exitAltitude;
    }

    public void setExitAltitude(double exitAltitude) {
        if (exitAltitude < MIN_EXIT_ALTITUDE) {
            new IllegalArgumentException("Exit altitude " + String.valueOf(exitAltitude) +
            " must be greater than minimum value of " + String.valueOf(MIN_EXIT_ALTITUDE));
        }
        else {
            this.exitAltitude = exitAltitude;
        }
    }

    public double getDeploymentAltitude() {
        return deploymentAltitude;
    }

    public void setDeploymentAltitude(double deploymentAltitude) {
        if (deploymentAltitude < MIN_DEPLOYMENT_ALTITUDE) {
            new IllegalArgumentException("Deployment altitude " + String.valueOf(deploymentAltitude) +
                    " must be greater than minimum value of " + String.valueOf(MIN_DEPLOYMENT_ALTITUDE));
        }
        else {
            this.deploymentAltitude = deploymentAltitude;
        }
    }
    public double getHorizontalDistanceTraveled() {
        double speed = this.winds.getAverageWindspeedInRange(this.deploymentAltitude, this.exitAltitude);
        speed *= this.MPH_TO_FPS;
        double seconds = this.getFreefallTimeInSeconds();
        return speed * seconds;
    }
    public static void main(String[] args) {
        double exitAltitude = 12000;
        double deploymentAltitude = 6000;
        OpeningPointCalculator OPCalculator = new OpeningPointCalculator(exitAltitude, deploymentAltitude);
        OPCalculator.winds.addWind(12000, OPCalculator.FPS_TO_MPH, 360);
        OPCalculator.winds.addWind( 6000, OPCalculator.FPS_TO_MPH, 360);



        System.out.println("Average Wind Heading in Degrees:        " + String.valueOf(OPCalculator.winds.getAverageHeading()));
        System.out.println("Average Wind Speed in Miles/Hour:       " + String.valueOf(OPCalculator.winds.getAverageWindSpeed()));
        System.out.println("Approximate Time in Freefall:           " + String.valueOf(OPCalculator.getFreefallTimeInSeconds()));
        System.out.println("Distance Traveled Horizontally in Feet: " + String.valueOf(OPCalculator.getHorizontalDistanceTraveled()));

        // Display all interpolated wind values
//        for (int i = 0; i < OPCalculator.winds.interpolatedAltitude.size(); i++) {
//            System.out.println(String.valueOf(OPCalculator.winds.interpolatedAltitude.get(i)) + "," +
//                    String.valueOf(OPCalculator.winds.interpolatedSpeed.get(i)) + "," +
//                    String.valueOf(OPCalculator.winds.interpolatedHeading.get(i)));
//        }
    }
}
