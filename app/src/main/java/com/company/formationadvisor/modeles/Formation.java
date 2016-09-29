package com.company.formationadvisor.modeles;


public class Formation {

    private String nom;
    private String dateDebut;
    private String dateFin;
    private String description;
    private String dureeFormation;

    public Formation(String nom, String dateDebut, String dateFin, String description ) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.dureeFormation = "Du " + dateDebut + " au " + dateFin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDureeFormation() {
        return dureeFormation;
    }

    public void setDureeFormation(String duréeFormation) {
        this.dureeFormation = duréeFormation;
    }
}
