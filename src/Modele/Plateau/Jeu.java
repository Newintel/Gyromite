package Modele.Plateau;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Point;
import Modele.Deplacements.*;

public class Jeu {
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 20;

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
        addEntite(hector, 5, 4);

        Gravite g = new Gravite();
        g.addEntiteDynamique(hector);
        
        Controle4Directions.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Controle4Directions.getInstance());
        
        for(int x = 0; x < SIZE_X; x++){
            addEntite(new Mur(this, false), x, 0);
            addEntite(new Mur(this, false), x, SIZE_Y - 1);
        }

        for (int y = 1; y < SIZE_Y - 1; y++){
            addEntite(new Mur(this, true), 0, y);
            addEntite(new Mur(this, true), SIZE_X - 1, y);
        }

        ArrayList<Colonne> cb1 = new ArrayList<Colonne>(); 
        for (int i = 8; i < 13; i++){
            Colonne toAdd = new Colonne(this, false);
            cb1.add(toAdd);
            addEntite(toAdd, 10, i);
        }

        addEntite(new Holder(this, false), 9, 10);

        ControleColonneRouge.getInstance().addEntiteDynamique(cb1);

        Radis r = new Radis(this);
        g.addEntiteDynamique(r);
        addEntite(r, 10, 7);

        ArrayList<Colonne> cb2 = new ArrayList<Colonne>(); 
        for (int i = 5; i < 11; i++){
            Colonne toAdd = new Colonne(this, true);
            cb2.add(toAdd);
            addEntite(toAdd, 15, i);
        }

        addEntite(new Holder(this, true), 16, 10);

        ControleColonneBleue.getInstance().addEntiteDynamique(cb2);
        ordonnanceur.add(ControleColonneRouge.getInstance());
        ordonnanceur.add(ControleColonneBleue.getInstance());
        
        ordonnanceur.add(g);

        addEntite(new Mur(this, false), 5, 6);
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
            case haut: ret = new Point(pCourant.x, pCourant.y - 1); break;
            case bas: ret = new Point(pCourant.x, pCourant.y + 1); break;
            case droite: ret = new Point(pCourant.x + 1, pCourant.y); break;
            case gauche: ret = new Point(pCourant.x - 1, pCourant.y); break;
        }

        return ret;
    }

    public boolean deplacerEntite(Entite e, Direction d){
        boolean ret = false;

        Point pCourant = map.get(e);
        Point pCible = calculerPointCible(pCourant, d);

        Entite eCible = ObjetALaPosition(pCible);
        Entite eBas = regarderDansLaDirection(e, Direction.bas);

        if (contenuDansGrille(pCible)){
            boolean bougerED = false;
            if (eBas instanceof EntiteDynamique)
                bougerED = e instanceof EntiteDynamique && eBas instanceof Colonne;

            if (eCible == null || !eCible.peutServirDeSupport() || bougerED){ // TODO: penser aux collisions
                switch (d){
                    case haut:
                    case bas:
                        if (cmptDeplV.get(e) == null){
                            cmptDeplV.put(e, 1);
                            if (e instanceof Personnage){
                                if (eCible != null && eCible.peutPermettreDeMonterDescendre())
                                    ((Personnage) e).sePoseOuMonte();
                                
                                if (eCible instanceof Corde && regarderDansLaDirection(eCible, Direction.bas).peutServirDeSupport()){
                                    ((Personnage) e).passeDevantLaCorde();
                                    ((Personnage) e).sePoseOuMonte();
                                }
                            }
                            
                            // Faire monter les entités dynamiques posées sur les colonnes avec les colonnes
                            if (e instanceof Colonne && d == Direction.haut){
                                if (eCible instanceof EntiteDynamique && !(eCible instanceof Colonne)){
                                    if (deplacerEntite(eCible, d))
                                        ret = true;
                                } else ret = true;
                            } else ret = true;
                        }
                        break;
                    case gauche:
                    case droite:
                        if (cmptDeplH.get(e) == null){
                            cmptDeplH.put(e, 1);
                            if (e instanceof Personnage && ObjetALaPosition(pCible) != null && !ObjetALaPosition(pCible).peutPermettreDeMonterDescendre() && ((Personnage) e).monteOuDescend())
                                ((Personnage) e).sePoseOuMonte();
                            ret = true;
                        }
                        break;
                }
            }
        }

            if (ret){
                boolean remettreCorde = false;
                if (e instanceof Personnage){
                    if (ObjetALaPosition(pCible) != null && ObjetALaPosition(pCible).peutPermettreDeMonterDescendre() && d.getValue() > 1)
                        ((Personnage) e).sePoseOuMonte();
                    else if (((Personnage) e).vaADroite() && d == Direction.gauche || !((Personnage) e).vaADroite() && d == Direction.droite)
                        ((Personnage) e).seTourne();
                    else if (((Personnage) e).estDevantLaCorde() || ((Personnage) e).monteOuDescend()) remettreCorde = true;
                    
                    if (e instanceof Heros)
                        if (ObjetALaPosition(pCible) instanceof Dynamite)
                            ((Heros) e).attraperDynamite();
                }

                deplacerEntite(pCourant, pCible, e);

                if (remettreCorde) addEntite(new Corde(this), pCourant.x, pCourant.y);
            }

        return ret;
    }

    private void deplacerEntite(Point pCourant, Point pCible, Entite e){
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
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

    public void start(long _pause){
        ordonnanceur.start(_pause);
    }
}
