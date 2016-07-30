package com.company.formationadvisor.taches_asynchrones;

import android.os.AsyncTask;
import android.util.Log;

import com.company.formationadvisor.modeles.IPAddress;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class CreerNouveauCentreFormation extends AsyncTask<String, String, String> {

    private ICreationCentreFormation callback;
    private String ip;

    public CreerNouveauCentreFormation (ICreationCentreFormation callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }


    @Override
    protected String doInBackground(String... params) {
        String etablissement = params[0];
        String rue = params[1];
        String codePostal = params[2];
        String localite = params[3];
        String telephone = params[4];
        String email = params[5];
        String siteInternet = params[6];
        String token = params[7];

        String encodedString1, encodedString2;

        try {

            encodedString1 = URLEncoder.encode(etablissement, "UTF-8");
            encodedString2 = URLEncoder.encode(rue, "UTF-8");


            URL url = new URL("http://"+ip+"/webService_Android/ajouter_centre_formation.php?nom_etablissement="+encodedString1+
                    "&adresse_etablissement="+encodedString2+"&code_postal="+codePostal+
                    "&localite="+localite+
                    "&numero_de_telephone="+telephone+
                    "&email="+email+
                    "&site_internet="+siteInternet+
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
        return  null;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        Log.e("STRING", string);
        try {
            callback.recuperationIdCentreFormation(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface ICreationCentreFormation {
        void recuperationIdCentreFormation(String string) throws JSONException;
    }
}
