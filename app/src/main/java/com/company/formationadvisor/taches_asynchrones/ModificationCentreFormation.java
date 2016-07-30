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

public class ModificationCentreFormation extends AsyncTask<String, String, String>{

    private IModificationCentreFormation callback;
    private String ip;

    public ModificationCentreFormation (IModificationCentreFormation callback, IPAddress ipAddress) {
        this.callback = callback;
        this.ip = ipAddress.getIpAddress();
    }


    @Override
    protected String doInBackground(String... params) {

        String etablissement = params[0];
        String rue = params[1];
        String codePostal = params[2];
        String telephone = params[3];
        String email = params[4];
        String siteInternet = params[5];
        String idCentreFormation = params[6];
        String localite = params[7];
        String token = params[8];

        URL url;
        try {

            String encodedString1 = URLEncoder.encode(etablissement);
            String encodedString2 = URLEncoder.encode(rue);

            url = new URL("http://"+ip+"/webService_Android/modifier_centre_formation.php?nom_etablissement="+encodedString1+
                    "&adresse_etablissement="+encodedString2+
                    "&code_postal="+codePostal+
                    "&localite="+localite+
                    "&telephone="+telephone+
                    "&email="+email+
                    "&site_internet="+siteInternet+
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String string) {
        super.onPostExecute(string);
        callback.confirmationModificationCentreFormation(string);
    }

    public interface IModificationCentreFormation{
        void confirmationModificationCentreFormation(String string);
    }
}
