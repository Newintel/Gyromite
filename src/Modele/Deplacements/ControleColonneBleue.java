package Modele.Deplacements;

import java.util.ArrayList;
import Modele.Plateau.Colonne;

public class ControleColonneBleue extends RealisateurDeDeplacement<ArrayList<Colonne>>{
    private static ControleColonneBleue cc;
    private static boolean haut = false; 

    public static ControleColonneBleue getInstance(){
        if (cc == null)
            cc = new ControleColonneBleue();
        
        return cc;
    }

    public boolean realiserDeplacement(){
        boolean ret = false;
        Direction d = haut ? Direction.haut : Direction.bas;

        for (ArrayList<Colonne> lc : listEntitesDynamiques){
            int[] indexes;

            if (haut)
                indexes = new int[]{0, lc.size() - 1, 1};
            else
                indexes = new int[]{lc.size() - 1, 0, -1};
            
            int i = indexes[0];

            while (i != indexes[1] && !ret){
                // if (lc.get(i).avancerDirectionChoisie()) ret &= true;

                i += indexes[2];
            }
        }

        return ret;
    }
}
