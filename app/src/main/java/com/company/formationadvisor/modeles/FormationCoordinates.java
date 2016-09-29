package com.company.formationadvisor.modeles;

/**
 * Created by Wivi on 26-09-16.
 */
public class FormationCoordinates {

        private double latitude;
        private double longitude;

        public FormationCoordinates(double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public FormationCoordinates() {}

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
}
