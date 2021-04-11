package Modele.Deplacements;

import java.util.ArrayList;

/**
Tous les déplacement sont déclenchés par cette classe (gravité, controle clavier, IA, etc.)
 */
public abstract class RealisateurDeDeplacement<T> {
    protected ArrayList<T> listEntitesDynamiques = new ArrayList<T>();
    protected abstract boolean realiserDeplacement();

    public void addEntiteDynamique(T ed) {listEntitesDynamiques.add(ed);}
}
