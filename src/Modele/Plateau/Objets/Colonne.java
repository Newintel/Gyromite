package Modele.Plateau.Objets;

import Modele.Plateau.EntiteDynamique;
import Modele.Plateau.Jeu;

public class Colonne extends EntiteDynamique{
    private boolean bleu;

    public Colonne(Jeu _jeu, boolean estBleu){
        super(_jeu);
        bleu = estBleu;
    }

    public boolean peutServirDeSupport() { return true; }
    public boolean peutEtreEcrase() { return false; }
    public boolean peutPermettreDeMonterDescendre() { return false; }

    public boolean estBleue() {return bleu;}
}