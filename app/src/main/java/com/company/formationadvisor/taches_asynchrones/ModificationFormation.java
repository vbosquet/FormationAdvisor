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
import java.net.URLEncoder;

public class ModificationFormation extends AsyncTask<String, String, String>{

    private IModificationFormation callback;
    private String ip;

    public ModificationFormation(IModificationFormation callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }

    @Override
    protected String doInBackground(String... params) {
        String nom = params[0];
        String description = params[1];
        String dateDebut = params[2];
        String dateFin = params[3];
        String id = params[4];
        String token = params[5];

        URL url;
        try {

            String encodedString1 = URLEncoder.encode(nom);
            String encodedString2 = URLEncoder.encode(description);

            url = new URL("http://"+ip+"/webService_Android/modifier_formation.php?nom_formation="+encodedString1+
                    "&description="+encodedString2+
                    "&date_debut="+dateDebut+
                    "&date_fin="+dateFin+
                    "&id_formation="+id+
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        callback.confirmationModificationFormation(string);
    }

    public interface IModificationFormation{
        void confirmationModificationFormation(String string);
    }
}
