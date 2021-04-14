package Modele.Plateau;

public class Colonne extends EntiteDynamique{
    private final boolean bleu;
    private Radis radisSurLeChemin;
    private final boolean x;

    public Colonne(Jeu _jeu, boolean estBleu, boolean extremite){
        super(_jeu);
        bleu = estBleu;
        x = extremite;
        //TODO: gestion des parties de colonnes + colonne holder
    }

    public boolean peutServirDeSupport() { return true; }
    public boolean peutEtreEcrase() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; }

    public boolean estBleue() {return bleu;}
    public boolean estExtremite() { return x; }

    public boolean aUnRadisSurLeChemin(){ return radisSurLeChemin != null; }
    public Radis getRadisSurLeChemin(){ return radisSurLeChemin; }
    public void setRadisSurLeChemin(Radis radis){ radisSurLeChemin = radis; }
}