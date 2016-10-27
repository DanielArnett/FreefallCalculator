package com.example;

public class OpeningPointCalculator {
    private double MIN_EXIT_ALTITUDE = 2000;
    private double MIN_DEPLOYMENT_ALTITUDE = 1000;
    private double exitAltitude;
    private double deploymentAltitude;
    private double firstOneThousandFeetInSeconds = 12;
    private double terminalOneThousandFeetTimeInSeconds = 5.5;
    private double freefallTimeInSeconds;

    public OpeningPointCalculator(double exitAltitude, double deploymentAltitude) {
        this.setExitAltitude(exitAltitude);
        this.setDeploymentAltitude(deploymentAltitude);
    }

    public double getFreefallTimeInSeconds() {
        double feetAtTerminalVelocity = (exitAltitude - deploymentAltitude - 1000);
        if (1000 < (exitAltitude - deploymentAltitude)) {
            freefallTimeInSeconds += firstOneThousandFeetInSeconds;
            freefallTimeInSeconds += terminalOneThousandFeetTimeInSeconds * feetAtTerminalVelocity;
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

    public static void main(String[] args) {
        double exitAltitude = 13500;
        double deploymentAltitude = 5000;
        OpeningPointCalculator OPCalculator = new OpeningPointCalculator(exitAltitude, deploymentAltitude);
        Winds winds = new Winds();
        winds.addWind(12000, 63, 310);
        winds.addWind( 9000, 53, 305);
        winds.addWind( 6000, 43, 290);
        winds.addWind( 3000, 32, 300);
        winds.interpolateWindspeedLinearly(12000-3000);
        System.out.println("Average Heading:    " + String.valueOf(winds.getAverageHeading()));
        System.out.println("Average Wind Speed: " + String.valueOf(winds.getAverageWindSpeed()));
    }
}
