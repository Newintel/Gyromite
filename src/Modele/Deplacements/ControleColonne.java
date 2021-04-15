
package Modele.Deplacements;

import java.util.ArrayList;
import Modele.Plateau.Colonne;
import Modele.Plateau.Holder;
import Modele.Plateau.EntiteDynamique;

public class ControleColonne extends RealisateurDeDeplacements<ArrayList<Colonne>>{
    private boolean haut = false;
    
    public boolean realiserDeplacement(){
        boolean ret = false;

        Direction d = haut ? Direction.haut : Direction.bas;

        for (ArrayList<Colonne> lc : listEntitesDynamiques){
            int[] indexes;

            if (!haut || lc.get(0).regarderDansLaDirection(d) instanceof EntiteDynamique || haut && lc.get(0).regarderDansLaDirection(d) == null){

                if (haut) indexes = new int[]{0, lc.size() - 1, 1};
                else indexes = new int[]{lc.size() - 1, 0, -1};
                
                for (int i = indexes[0]; i != indexes[1] + indexes[2]; i += indexes[2]){
                    Colonne topBottom = lc.get(indexes[1]);
                    if (!(topBottom.regarderDansLaDirection(Direction.gauche) instanceof Holder || topBottom.regarderDansLaDirection(Direction.droite) instanceof Holder)){
                        if (lc.get(i).avancerDirectionChoisie(d)){
                            ret = true;
                        }
                    }
                }
            }

        }

        return ret;
    }

    public void ouvrirFermer(){ haut = !haut; }
}       