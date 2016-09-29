package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Wivi on 26-09-16.
 */
public class GetCoordinatesFromAddress extends AsyncTask<String, String, String> {

    private IGetCoordinatesFromAddress callback;

    public GetCoordinatesFromAddress(IGetCoordinatesFromAddress callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String address = params[0];

        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+
                    encodedAddress+"&key=AIzaSyCjhRHOckZVNVVItaM6gmkxegMOWL6UEao");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStream.close();

            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        callback.getCoordinates(string);
    }

    public interface IGetCoordinatesFromAddress {
        void getCoordinates(String string);
    }
}
