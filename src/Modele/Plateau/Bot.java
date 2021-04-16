package Modele.Plateau;

import Modele.Deplacements.Gravite;

public class Bot extends Personnage {
    private EntiteDynamique target;

    public Bot(Jeu _jeu){
        super(_jeu);
    }

    @Override
    public void setRadisSurLeChemin(Radis r){
        super.setRadisSurLeChemin(r);
        Gravite.getInstance().removeEntiteDynamique(r);
        radis = true;
    }

    public void setTarget(EntiteDynamique t){
        target = t;
    }

    public EntiteDynamique getTarget(){
        return target;
    }
}
