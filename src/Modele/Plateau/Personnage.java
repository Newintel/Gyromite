package Modele.Plateau;

import Modele.Deplacements.Direction;

public abstract class Personnage extends EntiteDynamique {
    private boolean droite = true;
    private boolean monteOuDescend;

    public Personnage(Jeu _jeu){
        super(_jeu);
    }

    public boolean peutServirDeSupport() { return true; }
    public boolean peutEtreEcrase() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }

    public boolean vaADroite(){ return droite; }
    public void seTourne(){ droite = !droite; }

    public boolean monteOuDescend(){ return monteOuDescend; }
    public void sePoseOuMonte(){ monteOuDescend = !monteOuDescend; }
}
