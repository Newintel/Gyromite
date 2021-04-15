package Modele.Deplacements;

import java.util.ArrayList;

/**
Tous les déplacement sont déclenchés par cette classe (gravité, controle clavier, IA, etc.)
 */
public abstract class RealisateurDeDeplacements<T> {
    protected ArrayList<T> listEntitesDynamiques = new ArrayList<T>();
    protected ArrayList<T> listToRemove = new ArrayList<T>();
    protected abstract boolean realiserDeplacement();

    public void addEntiteDynamique(T ed) {listEntitesDynamiques.add(ed);}
    public void removeEntiteDynamique(T ed){listToRemove.add(ed);}
    public void resetControlleur(){ listEntitesDynamiques.clear(); }

    public void updateControlleur(){for (T ed : listToRemove) listEntitesDynamiques.remove(ed);}
}
