package com.company.formationadvisor.modeles;


public class Utilisateur {

    private int id;
    private String nom;
    private String prenom;
    private String pseudo;
    private String mot_de_passe;
    private String email;
    private String sel;

    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String pseudo,
                       String mot_de_passe, String email, String sel) {
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.mot_de_passe = mot_de_passe;
        this.email = email;
        this.sel = sel;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMot_de_passe() {
        return mot_de_passe;
    }

    public void setMot_de_passe(String mot_de_passe) {
        this.mot_de_passe = mot_de_passe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSel() {
        return sel;
    }

    public void setSel(String sel) {
            this.sel = sel;
        }

    public String toString() {
        String message = "nom="+this.getNom()+
                "prenom="+this.getPrenom()+
                "pseudo="+getPseudo();
        return message;
    }
}

