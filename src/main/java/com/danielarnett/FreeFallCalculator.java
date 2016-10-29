package com.danielarnett;

/**
 * FreeFallCalculator is used to calculate average windspeed, wind heading, freefall time
 * and horizontal displacement in freefall.
 */
public class FreeFallCalculator {
    public  final double MIN_EXIT_ALTITUDE = 2000;
    public  final double MIN_DEPLOYMENT_ALTITUDE = 1000;
    private final double TERMINAL_VELOCITY_IN_MPH = 120;
    public  final double MPH_TO_FPS = 66.0/45.0;
    public  final double FPS_TO_MPH = 1.0 / MPH_TO_FPS;
    private double exitAltitude;
    private double deploymentAltitude;
    private double firstOneThousandFeetInSeconds = 12;
    private double terminalOneThousandFeetTimeInSeconds = 5.5;

    public  double roundTo = 0.01;
    public Winds winds;

    /**
     * Creates a new FreeFallCalculator. This can be used to calculate freefall time,
     * average windspeed, average wind heading, and horizontal displacement in freefall.
     * See main() function for example usage.
     * @param exitAltitude The altitude that the jumper exits the plane.
     * @param deploymentAltitude The altitude that the jumper deploys his or her parachute.
     */
    public FreeFallCalculator(double exitAltitude, double deploymentAltitude) {
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

    /**
     * Set the altitude at which the jumper plans to exit the aircraft.
     * @param exitAltitude The altitude at which the jumper will exit.
     * @throws IllegalArgumentException If the exit altitude < MIN_EXIT_ALTITUDE
     */
    public void setExitAltitude(double exitAltitude) {
        if (exitAltitude < MIN_EXIT_ALTITUDE) {
            new IllegalArgumentException("Exit altitude " + String.valueOf(exitAltitude) +
            " must be greater than minimum value of " + String.valueOf(MIN_EXIT_ALTITUDE));
        }
        else {
            this.exitAltitude = exitAltitude;
        }
    }

    /**
     * Get the deployment altitude.
     * @return The planned parachute deployment altitude.
     */
    public double getDeploymentAltitude() {
        return deploymentAltitude;
    }

    /**
     * Set the altitude at which the skydiver deploys their parachute.
     * @param deploymentAltitude The planned deployment altitude.
     */
    public void setDeploymentAltitude(double deploymentAltitude) {
        if (deploymentAltitude < MIN_DEPLOYMENT_ALTITUDE) {
            new IllegalArgumentException("Deployment altitude " + String.valueOf(deploymentAltitude) +
                    " must be greater than minimum value of " + String.valueOf(MIN_DEPLOYMENT_ALTITUDE));
        }
        else {
            this.deploymentAltitude = deploymentAltitude;
        }
    }

    /**
     * Get the distance traveled horizontally.
     * @return Distance traveled horizontally during freefall in feet.
     */
    public double getHorizontalDistanceTraveled() {
        double speed = this.winds.getAverageWindspeedInRange(this.deploymentAltitude, this.exitAltitude);
        speed *= this.MPH_TO_FPS;
        double seconds = this.getFreefallTimeInSeconds();
        return speed * seconds;
    }
    public static void main(String[] args) {
        double exitAltitude = 12000;
        double deploymentAltitude = 4000;
        FreeFallCalculator freeFallCalculator = new FreeFallCalculator(12000.0, 3500.0);
        freeFallCalculator.winds.addWind(12000, 25,   0);
        freeFallCalculator.winds.addWind( 9000, 25,  90);
        freeFallCalculator.winds.addWind( 6000, 25, 180);
        freeFallCalculator.winds.addWind( 3000, 25, 270);
        System.out.println("Average Wind Heading in Degrees:        " +
            String.valueOf(freeFallCalculator.winds.getAverageHeading()));

        System.out.println("Average Wind Speed in Miles/Hour:       " +
            String.valueOf(freeFallCalculator.winds.getAverageWindspeedInRange(
            freeFallCalculator.deploymentAltitude, freeFallCalculator.exitAltitude)));

        System.out.println("Approximate Time in Freefall:           " +
            String.valueOf(freeFallCalculator.getFreefallTimeInSeconds()));

        System.out.println("Distance Traveled Horizontally in Feet: " +
            String.valueOf(freeFallCalculator.getHorizontalDistanceTraveled()));

        // Display all interpolated wind values
//        for (int i = 0; i < freeFallCalculator.winds.interpolatedAltitude.size(); i++) {
//            System.out.println(String.valueOf(freeFallCalculator.winds.interpolatedAltitude.get(i)) + "," +
//                    String.valueOf(freeFallCalculator.winds.interpolatedSpeed.get(i)) + "," +
//                    String.valueOf(freeFallCalculator.winds.interpolatedHeading.get(i)));
//        }
    }
}
