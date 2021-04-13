package Modele.Deplacements;

import Modele.Plateau.Entite;
import Modele.Plateau.EntiteDynamique;
import Modele.Plateau.Personnage;

public class Gravite extends RealisateurDeDeplacements<EntiteDynamique> {

    // private Direction sens;

    // public Gravite(boolean inv){
    //     sens = inv ? Direction.haut : Direction.bas;
    // }

    @Override
    public boolean realiserDeplacement(){
        boolean ret = false;

        for (EntiteDynamique e : listEntitesDynamiques){
            if (!(e instanceof Personnage && ((Personnage) e).monteOuDescend())){
                Entite b = e.regarderDansLaDirection(Direction.bas);
                if (b == null || (b != null && !b.peutServirDeSupport()))
                    if (e.avancerDirectionChoisie(Direction.bas))
                        ret = true;
            }
        }

        return ret;
    }
}
