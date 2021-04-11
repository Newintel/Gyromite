package Modele.Plateau;

public class Dynamite extends Entite{
    public Dynamite(Jeu _jeu){
        super(_jeu);
    }

    public boolean peutEtreEcrase(){ return false; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
}
