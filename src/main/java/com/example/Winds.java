package com.example;

import java.util.Vector;

/**
 * Created by Daniel on 10/25/2016.
 */
public class Winds {
    private Vector<Double> windAltitude;
    private Vector<Double> windSpeed;
    private Vector<Double> windHeading;

    public Winds() {
        windAltitude = new Vector<>();
        windSpeed = new Vector<>();
        windHeading = new Vector<>();
    }

    /**
     * Creates a new Winds object. The altitudes and speeds arrays must be of equal length.
     * @param altitudes The altitudes of the Winds. Each index must match with a windspeed.
     * @param speeds The windspeed. Each index must match with an altitude.
     * @param headings The heading of the wind in degrees.
     */
    public Winds(double altitudes[], double speeds[], double headings[]) {
        int initialWindCount = altitudes.length;
        windAltitude = new Vector<>();
        windSpeed = new Vector<>();
        windHeading = new Vector<>();
        try
        {
            if (altitudes.length != speeds.length) {
                initialWindCount = altitudes.length < speeds.length ? altitudes.length : speeds.length;
                throw new IllegalArgumentException("The number of Wind altitudes must equal the number of speeds." +
                        "\nNumber of Altitudes: " + altitudes.length +
                        "\nNumber of Speeds:    " + speeds.length + "\n");
            }
            if (initialWindCount != headings.length) {
                initialWindCount = initialWindCount < headings.length ? initialWindCount : headings.length;
                throw new IllegalArgumentException("The number of Wind altitudes must equal the number of headings." +
                        "\nNumber of Winds:    " + String.valueOf(initialWindCount) +
                        "\nNumber of Headings: " + String.valueOf(headings.length) + "\n");
            }
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        for (int i = 0; i < initialWindCount; i++)
        {
            addWind(altitudes[i], speeds[i], headings[i]);
        }
    }

    /**
     * Add a windspeed at a certain altitude.
     * @param newAltitude The altitude of the wind.
     * @param newSpeed The velocity of the wind.
     * @param newHeading The heading of the wind.
     */
    public void addWind(double newAltitude, double newSpeed, double newHeading) {
        // If a windspeed is already recorded at a particular altitude
        if (windAltitude.contains(newAltitude)) {
            // Remove the old value
            int indexToRemove = windAltitude.indexOf(newAltitude);
            new IllegalArgumentException("Windspeed already recorded at altitude " +
                    String.valueOf(newAltitude) + ". Windspeed of " + String.valueOf(newSpeed) +
                    " will be used.").printStackTrace();
            windAltitude.remove(indexToRemove);
            windSpeed.remove(   indexToRemove);
            windHeading.remove( indexToRemove);
        }
        windAltitude.add(newAltitude);
        windSpeed.add(newSpeed);
        windHeading.add(newHeading);
    }

    /**
     * @return The average x value of the wind headings.
     */
    private double averageX() {
        double average = 0;
        for (int i = 0; i < windAltitude.size(); i++) {
            average += windSpeed.get(i) * Math.sin(Math.toRadians(windHeading.get(i)));
        }
        average /= windSpeed.size();
        return average;
    }

    /**
     * @return The average y value of the wind headings.
     */
    private double averageY() {
        double average = 0;
        for (int i = 0; i < windAltitude.size(); i++) {
            average += windSpeed.get(i) * Math.cos(Math.toRadians(windHeading.get(i)));
        }
        average /= windSpeed.size();
        return average;
    }

    /**
     *
     * @return The average heading of the wind in degrees
     */
    public double getAverageHeading() {
        double average = this.averageX()/this.averageY();

        average = Math.atan(average);
        // If x is negative on the unit circle add Pi to the value
        if (this.averageY() < 0) {
            average += Math.PI;
        }
        // If x is positive but y is negative on the unit circle
        else if (0 < this.averageY() && this.averageX() < 0)
        {
            // When x is positive but y is negative arctan returns values which are off by 2*Pi
            average += 2*Math.PI;
        }
//        average += 2*Math.PI;
        return Math.toDegrees(average);
    }

    /**
     *
     * @return The windspeed averaged over the different altitudes.
     */
    public double getAverageWindSpeed() {
        double average = 0;
        average = Math.pow(averageX(),2) + Math.pow(averageY(),2);
        average = Math.sqrt(average);
        return average;
    }
}
