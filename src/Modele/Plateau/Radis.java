package Modele.Plateau;

public class Radis extends EntiteDynamique{
    public Radis(Jeu _jeu){
        super(_jeu);
    }

    public boolean peutEtreEcrase() { return true; }
    public boolean peutServirDeSupport() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; }
}
