package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;

import com.company.formationadvisor.modeles.IPAddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RechercherFormationParIdCentreFormation extends AsyncTask<String, String, String>{

    private IRechercheFormationParIdCentreFormation callback;
    private String ip;

    public RechercherFormationParIdCentreFormation(IRechercheFormationParIdCentreFormation callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }


    @Override
    protected String doInBackground(String... params) {
        String id = params[0];
        String token = params[1];

        URL url;
        try {
            url = new URL("http://"+ip+"/webService_Android/rechercher_formation_par_id_centre_formation.php?id="+id+
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
        callback.afficherInfoFormation(string);
    }

    public interface IRechercheFormationParIdCentreFormation{
        void afficherInfoFormation(String string);
    }
}
