package Modele.Plateau;

public abstract class Personnage extends EntiteDynamique {
    public boolean monteOuDescend = false;

    public Personnage(Jeu _jeu){
        super(_jeu);
    }

    public boolean peutServirDeSupport() { return true; }
    public boolean peutEtreEcrase() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
}
