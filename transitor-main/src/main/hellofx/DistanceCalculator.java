package main.hellofx;

public class DistanceCalculator {

    //Earth's radius in kilometers
    private final double EARTH_RADIUS_KM = 6371.0;

    /*
     
Calculates the distance between two points on the Earth's surface.*
@param lat1 Latitude of the first point in degrees
@param lon1 Longitude of the first point in degrees
@param lat2 Latitude of the second point in degrees
@param lon2 Longitude of the second point in degrees
@return Distance between the two points in kilometers
*/

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // onvert degrees to radians for latitude and longitude
        double lat1Radians = Math.toRadians(lat1);
        double lon1Radians = Math.toRadians(lon1);
        double lat2Radians = Math.toRadians(lat2);
        double lon2Radians = Math.toRadians(lon2);

        //Difference in coordinates
        double deltaLat = lat2Radians - lat1Radians;
        double deltaLon = lon2Radians - lon1Radians;

        //Haversine formula
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Radians) * Math.cos(lat2Radians) *
                Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        //Distance calculation
        double distance = EARTH_RADIUS_KM * c;

        return distance;
    }

    private final double WALKING_SPEED_KM_H = 5.0; // Average walking speed in km/h
    private final double CYCLING_SPEED_KM_H = 15.0; // Average cycling speed in km/h

    public double calculateWalkingTime(double distanceKm) {
        return (distanceKm / WALKING_SPEED_KM_H) * 60; //Time in minutes
    }

    public double calculateCyclingTime(double distanceKm) {
        return (distanceKm / CYCLING_SPEED_KM_H) * 60; //Time in minutes
    }
}
