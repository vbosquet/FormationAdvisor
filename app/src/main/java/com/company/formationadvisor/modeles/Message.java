package com.company.formationadvisor.modeles;

public class Message {

    private String titre;
    private String texte;
    private String expediteur;
    private String dateEnvoi;

    public Message(String texte, String expediteur, String dateEnvoi) {
        this.texte = texte;
        this.expediteur = expediteur;
        this.dateEnvoi = dateEnvoi;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
}
