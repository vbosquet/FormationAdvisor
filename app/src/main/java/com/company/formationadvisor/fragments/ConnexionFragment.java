package com.company.formationadvisor.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.formationadvisor.R;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class ConnexionFragment extends Fragment{

    IConnexion mConnexion;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.connexion_fragment, container, false);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mConnexion = (IConnexion) activity;
    }

    public interface IConnexion {
        void connexion(View view) throws NoSuchProviderException, NoSuchAlgorithmException;
    }
}
