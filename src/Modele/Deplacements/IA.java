package Modele.Deplacements;

import java.util.ArrayList;
import java.util.Random;

import java.awt.Point;

import Modele.Plateau.Bot;
import Modele.Plateau.EntiteDynamique;
import Modele.Plateau.Jeu;
import Modele.Plateau.Radis;

public class IA extends RealisateurDeDeplacements<Bot> {
    private Jeu jeu;
    private Random r = new Random();

    public IA(Jeu _jeu){
        jeu = _jeu;
    }

    public boolean realiserDeplacement(){
        boolean ret = false;

        for (Bot b : listEntitesDynamiques){
            if (!b.aUnRadis()){
                ArrayList<Direction> p = calculerMeilleureDirection(b, getDirectionsPossibles(b), target(b));
                if (p.size() > 0){
                    Direction d = p.get(r.nextInt(p.size()));
                    if (jeu.deplacerEntite(b, d)){
                        ret = true;
                        if (b.aUnRadis()) removeEntiteDynamique(b);
                    }
                }
            }
        }

        return ret;
    }

    private ArrayList<Direction> calculerMeilleureDirection(Bot b, ArrayList<Direction> posDir, EntiteDynamique target){
        ArrayList<Direction> bestDir = new ArrayList<Direction>();
        Point posTarget = jeu.getPosition(target);
        Point pos = jeu.getPosition(b);

        if (posTarget.x >= pos.x){
            if (posDir.contains(Direction.droite)){
                bestDir.add(Direction.droite);
            }
        }
        if (posTarget.x <= pos.x){
            if (posDir.contains(Direction.gauche)){
                bestDir.add(Direction.gauche);
            }
        }
        if (posTarget.y > pos.y){
            if (posDir.contains(Direction.haut)){
                bestDir.add(Direction.haut);
            }
        } else if (posTarget.y < pos.y){
            if (posDir.contains(Direction.bas)){
                bestDir.add(Direction.bas);
            }
        }

        if (bestDir.size() == 0){
            return posDir;
        }

        return bestDir;
    }

    private ArrayList<Direction> getDirectionsPossibles(Bot b){
        ArrayList<Direction> ret = new ArrayList<Direction>();
        ArrayList<Direction> toRem = new ArrayList<Direction>();
        ret.add(Direction.gauche);
        ret.add(Direction.droite);

        for (Direction d : ret){
            if (b.regarderDansLaDirection(d) != null){
                if (b.regarderDansLaDirection(d).peutServirDeSupport()){
                    toRem.add(d);
                }
            }
        }
        for (Direction d : toRem){
            ret.remove(d);
        }

        if (b.monteOuDescend() && b.regarderDansLaDirection(Direction.bas) != null){
            if(b.regarderDansLaDirection(Direction.bas).peutPermettreDeMonterDescendre()){
                ret.add(Direction.bas);
            }
        }
        if (b.regarderDansLaDirection(Direction.haut) != null){
            if(b.regarderDansLaDirection(Direction.haut).peutPermettreDeMonterDescendre() && (b.estDevantLaCorde() || b.monteOuDescend())){
                ret.add(Direction.haut);
            }
        }
        return ret;
    }

    private EntiteDynamique target(Bot b){
        EntiteDynamique ret = jeu.getHector();
        int distCurrentTarget = distance(jeu.getPosition(ret), jeu.getPosition(b));
        for (Radis r : jeu.getRadis()){
            int distRadis = distance(jeu.getPosition(r), jeu.getPosition(b));
            if (distRadis <= distCurrentTarget){
                ret = r;
            }
        }
        return ret;
    }

    private int distance(Point a, Point b){
        return (int) (Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
    }
}
