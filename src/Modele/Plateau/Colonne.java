package Modele.Plateau;

public class Colonne extends EntiteDynamique{
    private boolean bleu;
    private Radis radisSurLeChemin;

    public Colonne(Jeu _jeu, boolean estBleu){
        super(_jeu);
        bleu = estBleu;

        //TODO: gestion des parties de colonnes + colonne holder
    }

    public boolean peutServirDeSupport() { return true; }
    public boolean peutEtreEcrase() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; }

    public boolean estBleue() {return bleu;}

    public boolean aUnRadisSurLeChemin(){ return radisSurLeChemin != null; }
    public Radis getRadisSurLeChemin(){ return radisSurLeChemin; }
    public void setRadisSurLeChemin(Radis radis){ radisSurLeChemin = radis; }
}