package com.example;

import java.util.Vector;

public class Winds {
    private enum SortMethod { ALTITUDE, SPEED, HEADING, NONE };
    private SortMethod currentSortMethod;
    private Vector<Double> windAltitude;
    private Vector<Double> windSpeed;
    private Vector<Double> windHeading;

    private Vector<Double> interpolatedAltitude;
    private Vector<Double> interpolatedSpeed;
    private Vector<Double> interpolatedHeading;

    public Winds() {
        this.init();
    }

    /**
     * Creates a new Winds object. The altitudes and speeds arrays must be of equal length.
     * @param altitudes The altitudes of the Winds. Each index must match with a windspeed.
     * @param speeds The windspeed. Each index must match with an altitude.
     * @param headings The heading of the wind in degrees.
     */
    public Winds(double altitudes[], double speeds[], double headings[]) {
        this.init();
        int initialWindCount = altitudes.length;
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
    private void init() {
        windAltitude            = new Vector<>();
        windSpeed               = new Vector<>();
        windHeading             = new Vector<>();
        interpolatedAltitude    = new Vector<>();
        interpolatedSpeed       = new Vector<>();
        interpolatedHeading     = new Vector<>();
        currentSortMethod = SortMethod.NONE;

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
        currentSortMethod = SortMethod.NONE;
    }

    /**
     * Add a windspeed at a certain altitude.
     * @param newAltitude The altitude of the wind.
     * @param newSpeed The velocity of the wind.
     * @param newHeading The heading of the wind.
     */
    public void addInterpolatedWind(double newAltitude, double newSpeed, double newHeading) {
        // If a windspeed is already recorded at a particular altitude
        if (interpolatedAltitude.contains(newAltitude)) {
            // Remove the old value
            int indexToRemove = interpolatedAltitude.indexOf(newAltitude);
            new IllegalArgumentException("Interpolated windspeed already recorded at altitude " +
                    String.valueOf(newAltitude) + ". Windspeed of " + String.valueOf(newSpeed) +
                    " will be used.").printStackTrace();
            interpolatedAltitude.remove(indexToRemove);
            interpolatedSpeed.remove(   indexToRemove);
            interpolatedHeading.remove( indexToRemove);
        }
        interpolatedAltitude.add(newAltitude);
        interpolatedSpeed.add(newSpeed);
        interpolatedHeading.add(newHeading);
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
        return pointToHeadingInDegrees(this.averageY(),this.averageX());
    }

    /**
     * Convert an x,y pair into a heading in radians. Note that since we're using compass
     * headings instead of the unit circle x and y are flipped.
     * @param x
     * @param y
     * @return An angle in radians corresponding to the heading of point x,y.
     */
    private double pointToHeadingInRadians(double x, double y) {
        double heading = y/x;

        heading = Math.atan(heading);
        // If x is negative on the unit circle add Pi to the value
        if (x < 0) {
            heading += Math.PI;
        }
        // If x is positive but y is negative on the unit circle
        else if (0 < x && y < 0)
        {
            // When x is positive but y is negative arctan returns values which are off by 2*Pi
            heading += 2*Math.PI;
        }
        return heading;
    }
    private double pointToHeadingInDegrees(double x, double y) {
        return Math.toDegrees(this.pointToHeadingInRadians(x,y));
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
    private void sortWindsByAltitudeAscending() {
        if (currentSortMethod != SortMethod.ALTITUDE) {
            quickSortByAltitudeAscending(0, windAltitude.size() - 1);
            currentSortMethod = SortMethod.ALTITUDE;
        }
    }
    private void quickSortByAltitudeAscending(int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        double pivot = windAltitude.get(lowerIndex+(higherIndex-lowerIndex)/2);
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (windAltitude.get(i) < pivot) {
                i++;
            }
            while (windAltitude.get(j) > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSortByAltitudeAscending(lowerIndex, j);
        if (i < higherIndex)
            quickSortByAltitudeAscending(i, higherIndex);
    }

    private void exchangeNumbers(int i, int j) {
        double tempAltitude = windAltitude.get(i);
        double tempSpeed = windSpeed.get(i);
        double tempHeading = windHeading.get(i);
        windAltitude.set(i, windAltitude.get(j));
        windAltitude.set(j, tempAltitude);
        windSpeed.set(i, windSpeed.get(j));
        windSpeed.set(j, tempSpeed);
        windHeading.set(i, windHeading.get(j));
        windHeading.set(j, tempHeading);
    }

    /**
     * Interpolates the windspeeds. The interpolated windspeeds will serve as approximations to
     * the windspeeds at altitudes between the ones directly measured.
     * @param steps The number of steps that you want to generate via interpolation. E.g.
     *              If you have a maximum recorded altitude of 3000ft and minimum recorded altitude
     *              of 2000ft you can estimate the windspeed at every foot in between by setting the
     *              step size to 3000-2000=1000. Otherwise if you want to estimate the windspeed at
     *              every other foot you would set the step size to be 500.
     */
    public void interpolateWindspeedLinearly(double steps) {
        // Sort the winds
        sortWindsByAltitudeAscending();

        double feetPerStep = (getMaxAltitude() - getMinAltitude()) / steps;
        // For each pair of altitudes
        for (int i = 0; i < windAltitude.size()-1; i++) {
            // Get the number of steps between the altitude pair
            double feetBetweenAltitudePair = windAltitude.get(i + 1) - windAltitude.get(i);
            double stepAltitude = windAltitude.get(i);
            double stepsBetweenPair = feetBetweenAltitudePair / feetPerStep;
            // Get the distance between the two velocity points
            double lowX  = windSpeed.get(i) *   Math.cos(Math.toRadians(windHeading.get(i)));
            double lowY  = windSpeed.get(i) *   Math.sin(Math.toRadians(windHeading.get(i)));
            double highX = windSpeed.get(i+1) * Math.cos(Math.toRadians(windHeading.get(i+1)));
            double highY = windSpeed.get(i+1) * Math.sin(Math.toRadians(windHeading.get(i+1)));

            // Add the lower of the altitude pair, one of the recorded windspeeds that
            // was set by the user.
            addInterpolatedWind(windAltitude.get(i), windSpeed.get(i), windHeading.get(i));
            // For each interpolated altitude
            for (int j = 0; j < stepsBetweenPair; j++) {
                // Generate the interpolated x and y values.
                double x = ((stepsBetweenPair - 1 - j)/stepsBetweenPair) * lowX + ((j+1)/stepsBetweenPair) * highX;
                double y = ((stepsBetweenPair - 1 - j)/stepsBetweenPair) * lowY + ((j+1)/stepsBetweenPair) * highY;
                // Set the new heading
                double heading = pointToHeadingInDegrees(x,y);
                // Set the new Speed
                double speed = Math.sqrt(x*x +y*y);
                // If there isn't already one recorded at that altitude
                if (!windAltitude.contains(stepAltitude)) {
                    // Add the interpolated wind value
                    addInterpolatedWind(stepAltitude, speed, heading);
                }
                // Increment the altitude of the next step
                stepAltitude += feetPerStep;
            }
        }
        addInterpolatedWind(windAltitude.get(windAltitude.size()-1),
                windSpeed.get(windAltitude.size()-1),
                windHeading.get(windAltitude.size()-1));
        if (interpolatedAltitude.size() != interpolatedSpeed.size() || interpolatedAltitude.size() != interpolatedHeading.size()) {
            System.out.println("Warning! Unequal number of altitudes,speeds, and sizes.");
        }
    }
    public double getMaxAltitude() {
        if (currentSortMethod != SortMethod.ALTITUDE) {
            this.sortWindsByAltitudeAscending();
        }
        return windAltitude.get(windAltitude.size()-1);
    }
    public double getMinAltitude() {
        if (currentSortMethod != SortMethod.ALTITUDE) {
            this.sortWindsByAltitudeAscending();
        }
        return windAltitude.get(0);
    }
}
