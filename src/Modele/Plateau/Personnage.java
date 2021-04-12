package Modele.Plateau;

public abstract class Personnage extends EntiteDynamique {
    private boolean droite = true;
    private boolean monteOuDescend = false;
    private boolean corde = false;

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

    public boolean estDevantLaCorde(){ return corde; }
    public void passeDevantLaCorde(){ corde = !corde; }
}
