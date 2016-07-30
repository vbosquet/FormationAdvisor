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

public class InscriptionFragment extends Fragment {

    IInscription mInscription;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inscription_fragment, container, false);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mInscription = (IInscription) activity;
    }

    public interface IInscription {
        void onRegisterInscription(View view) throws NoSuchAlgorithmException, NoSuchProviderException;
        boolean isValidPassword(String password);
        boolean isValidEmail(String email);
    }
}
