package com.company.formationadvisor.modeles;

import java.io.Serializable;

/**
 * Created by Wivi on 30-09-16.
 */
public class Organisme implements Serializable {
    String nomOrganisme = "";
    String rue = "";
    String codePostal = "";
    String localite = "";
    String telephone = "";
    String emailOrganisme = "";
    String siteInternet = "";

    public Organisme() {

    }

    public Organisme(String nomOrganisme, String rue, String codePostal, String localite, String telephone, String emailOrganisme, String siteInternet) {
        this.nomOrganisme = nomOrganisme;
        this.rue = rue;
        this.codePostal= codePostal;
        this.localite = localite;
        this.telephone = telephone;
        this.emailOrganisme = emailOrganisme;
        this.siteInternet = siteInternet;
    }

    public String getNomOrganisme() {
        return nomOrganisme;
    }

    public void setNomOrganisme(String nomOrganisme) {
        this.nomOrganisme = nomOrganisme;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmailOrganisme() {
        return emailOrganisme;
    }

    public void setEmailOrganisme(String emailOrganisme) {
        this.emailOrganisme = emailOrganisme;
    }

    public String getSiteInternet() {
        return siteInternet;
    }

    public void setSiteInternet(String siteInternet) {
        this.siteInternet = siteInternet;
    }
}
