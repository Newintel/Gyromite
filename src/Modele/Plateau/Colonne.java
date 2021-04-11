package Modele.Plateau;

public class Colonne extends EntiteDynamique{
    private boolean bleu;
    private boolean enBas;

    public Colonne(Jeu _jeu, boolean estBleu, boolean estEnBas){
        super(_jeu);
        bleu = estBleu;
        enBas = estEnBas;

        //TODO: gestion des parties de colonnes + colonne holder
    }

    public boolean peutServirDeSupport() { return true; }
    public boolean peutEtreEcrase() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; }

    public boolean estBleue() {return bleu;}

    public boolean estEnBas() { return enBas; }
    public void monterDescendre(){ enBas = !enBas; }
}