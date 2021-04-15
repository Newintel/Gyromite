package Modele.Deplacements;

import java.util.ArrayList;

import Modele.Plateau.Personnage;

public class IA extends RealisateurDeDeplacements<Personnage> {
    public boolean realiserDeplacement(){
        boolean ret = false;

        for (Personnage perso : listEntitesDynamiques){

        }

        return ret;
    }

    private Direction calculerMeilleureDirection(Personnage perso){
        Direction d = null;



        return d;
    }

    private ArrayList<Direction> getPossibleDirections(Personnage perso){
        ArrayList<Direction> ret = new ArrayList<Direction>();

        return ret;
    }
}
