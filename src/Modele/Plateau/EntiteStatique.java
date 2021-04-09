package Modele.Plateau;

public abstract class EntiteStatique extends Entite {
    public EntiteStatique(Jeu _jeu){
        super(_jeu);
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
}
