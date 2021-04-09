package Modele.Plateau;

import java.util.HashMap;
import java.awt.Point;
import Modele.Deplacements.*;
import Modele.Plateau.Personnages.*;

public class Jeu {
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu(){
        initialisationDesEntites();
    }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    private void addEntite(Entite e, int x, int y){
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }

    private void initialisationDesEntites() {
        hector = new Heros(this);
        addEntite(hector, 2, 1);

        Gravite g = new Gravite(false);
        g.addEntiteDynamique(hector);

        ordonnanceur.add(g);

        Controle4Directions.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Controle4Directions.getInstance());
    }

    private boolean contenuDansGrille(Point p){
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Entite ObjetALaPosition(Point p){
        Entite ret = null;

        if (contenuDansGrille(p)){
            ret = grilleEntites[p.x][p.y];
        }

        return ret;
    }

    private Point calculerPointCible(Point pCourant, Direction d){
        Point ret = null;

        switch (d){
            case haut: ret = new Point(pCourant.x, pCourant.y + 1); break;
            case bas: ret = new Point(pCourant.x, pCourant.y - 1); break;
            case droite: ret = new Point(pCourant.x + 1, pCourant.y); break;
            case gauche: ret = new Point(pCourant.x - 1, pCourant.y); break;
        }

        return ret;
    }

    public boolean deplacerEntite(Entite e, Direction d){
        boolean ret = false;

        Point pCourant = map.get(e);
        Point pCible = calculerPointCible(pCourant, d);

        if (contenuDansGrille(pCible))
            if (ObjetALaPosition(pCible) == null){ // TODO: penser aux collisions
                switch (d){
                    case haut:
                    case bas:
                        cmptDeplV.put(e, 1);
                        ret = true;
                        break;
                    case gauche:
                    case droite:
                        cmptDeplH.put(e, 1);
                        ret = true;
                        break;
                }
            }

        return ret;
    }

    public Entite regarderDansLaDirection(Entite e, Direction d){
        Point pCourant = map.get(e);
        return ObjetALaPosition(calculerPointCible(pCourant, d));
    }

    public Ordonnanceur getOrdonnanceur(){
        return ordonnanceur;
    }

    public Entite[][] getGrille(){
        return grilleEntites;
    }

    public Heros getHector(){
        return hector;
    }
}
