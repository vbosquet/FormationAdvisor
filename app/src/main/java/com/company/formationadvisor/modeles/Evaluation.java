package com.company.formationadvisor.modeles;


public class Evaluation {

    private String titre;
    private String commentaire;
    private String auteur;

    public Evaluation(String titre, String commentaire, String auteur) {
        this.titre = titre;
        this.commentaire = commentaire;
        this.auteur = auteur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
}
