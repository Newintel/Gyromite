package Modele.Deplacements;

import Modele.Plateau.EntiteDynamique;

import java.util.ArrayList;

/**
Tous les déplacement sont déclenchés par cette classe (gravité, controle clavier, IA, etc.)
 */
public abstract class RealisateurDeDeplacement {
    protected ArrayList<EntiteDynamique> listEntitesDynamiques = new ArrayList<EntiteDynamique>();
    protected abstract boolean realiserDeplacement();

    public void addEntiteDynamique(EntiteDynamique ed) {listEntitesDynamiques.add(ed);}
}
