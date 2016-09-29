package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

public class RechercherAdressesCentreFormation extends AsyncTask<String, String, String>{

    private IRechercheAdressesCentreFormation callback;
    private String ip;

    public RechercherAdressesCentreFormation(IRechercheAdressesCentreFormation callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL("http://"+ip+"/webService_Android/rechercher_adresse_centre_formation.php");
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

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        callback.afficherAdressesCentreFormation(string);
    }

    public interface IRechercheAdressesCentreFormation {
        void afficherAdressesCentreFormation(String string);
    }
}
