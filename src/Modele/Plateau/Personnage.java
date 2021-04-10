package Modele.Plateau;

public abstract class Personnage extends EntiteDynamique {
    private boolean monteOuDescend = false;
    private boolean droite = true;

    public Personnage(Jeu _jeu){
        super(_jeu);
    }

    public boolean peutServirDeSupport() { return true; }
    public boolean peutEtreEcrase() { return true; }
    public boolean peutPermettreDeMonterDescendre() { return false; }

    public boolean vaADroite() { return droite; }
    public void seTourner() { droite = !droite; }

    public boolean monteOuDescend(){ return monteOuDescend; }
    public void sePoseOuMonte(){ monteOuDescend = !monteOuDescend; }
}
