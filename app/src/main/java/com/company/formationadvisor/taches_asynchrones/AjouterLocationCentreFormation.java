package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Wivi on 27-09-16.
 */
public class AjouterLocationCentreFormation extends AsyncTask<String, String, String> {

    private IAjouterLocationCentreFormation callback;
    private String ip;

    public AjouterLocationCentreFormation(IAjouterLocationCentreFormation callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {
        String nom = params[0];
        String latitude = params[1];
        String longitude = params[2];
        String idCentreFormation = params[3];
        String token = params[4];

        try {

            String encodedname = URLEncoder.encode(nom, "UTF-8");

            URL url = new URL("http://"+ip+"/webService_Android/ajouter_localite_centre_formation.php?nom_etablissement="+encodedname+
                    "&latitude="+latitude+
                    "&longitude="+longitude+
                    "&id_centre_formation="+idCentreFormation+
                    "&token="+token);

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
        callback.confirmationEnregistrementLocation(string);
    }

    public interface IAjouterLocationCentreFormation {
        void confirmationEnregistrementLocation(String string);
    }
}
