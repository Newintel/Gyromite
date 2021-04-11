package Modele.Plateau;

public class Radis extends EntiteDynamique{
    public Radis(Jeu _jeu){
        super(_jeu);
    }

    public boolean peutEtreEcrase() { return false; }
    public boolean peutServirDeSupport() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
}
