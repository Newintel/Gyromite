package Modele.Deplacements;

import java.util.ArrayList;
import java.awt.Point;

import Modele.Plateau.Personnage;
import Modele.Plateau.Jeu;

public class IA extends RealisateurDeDeplacements<Personnage> {
    private Jeu jeu;

    public IA(Jeu _jeu){
        jeu = _jeu;
    }

    public boolean realiserDeplacement(){
        boolean ret = false;

        for (Personnage perso : listEntitesDynamiques){
            ArrayList<Direction> p = getDirectionsPossibles(perso);
        }

        return ret;
    }

    private Direction calculerMeilleureDirection(Personnage perso, ArrayList<Direction> posDir){
        Direction d = null;

        

        return d;
    }

    private ArrayList<Direction> getDirectionsPossibles(Personnage perso){
        ArrayList<Direction> ret = new ArrayList<Direction>();
        ret.add(Direction.gauche);
        ret.add(Direction.droite);

        for (Direction d : ret){
            if (perso.regarderDansLaDirection(d) != null && perso.regarderDansLaDirection(Direction.gauche).peutServirDeSupport()){
                ret.remove(d);
            }
        }

        if (perso.monteOuDescend() && perso.regarderDansLaDirection(Direction.bas) != null && perso.regarderDansLaDirection(Direction.bas).peutPermettreDeMonterDescendre()){
            ret.add(Direction.bas);
        }

        if (perso.regarderDansLaDirection(Direction.haut) != null && perso.regarderDansLaDirection(Direction.haut).peutPermettreDeMonterDescendre() && (perso.estDevantLaCorde() || perso.monteOuDescend())){
            ret.add(Direction.haut);
        }

        return ret;
    }
}
