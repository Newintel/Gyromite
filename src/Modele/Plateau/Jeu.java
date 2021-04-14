package Modele.Plateau;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Point;
import Modele.Deplacements.*;

public class Jeu {
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 20;
    private int time = 90;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Gravite gravite = new Gravite();

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu(){
        initialisationDesEntites();
        ordonnanceur.add(gravite);
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

        ordonnanceur.add(ControleColonneRouge.getInstance());
        ordonnanceur.add(ControleColonneBleue.getInstance());

        gravite.addEntiteDynamique(hector);
        
        Controle4Directions.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Controle4Directions.getInstance());
        
        for(int x = 0; x < SIZE_X; x++){
            addEntite(new Mur(this), x, 0);
            addEntite(new Mur(this), x, SIZE_Y - 1);
        }

        for (int y = 1; y < SIZE_Y - 1; y++){
            addEntite(new Mur(this), 0, y);
            addEntite(new Mur(this), SIZE_X - 1, y);
        }

        ArrayList<Colonne> cb1 = new ArrayList<Colonne>(); 
        for (int i = 4; i < 13; i++){
            Colonne toAdd;
            if (i == 4 || i == 12) toAdd = new Colonne(this, false, true);
            else toAdd = new Colonne(this, false, false);
            cb1.add(toAdd);
            addEntite(toAdd, 10, i);
        }
        addEntite(new Mur(this), 10, 1);

        addEntite(new Holder(this, false), 9, 10);

        ControleColonneRouge.getInstance().addEntiteDynamique(cb1);

        ArrayList<Colonne> cb2 = new ArrayList<Colonne>(); 
        for (int i = 3; i < 12; i++){
            Colonne toAdd;
            if (i == 3 || i == 11) toAdd = new Colonne(this, true, true);
            else toAdd = new Colonne(this, true, false);
            cb2.add(toAdd);
            addEntite(toAdd, 15, i);
        }

        addEntite(new Holder(this, true), 16, 10);

        ControleColonneBleue.getInstance().addEntiteDynamique(cb2);

        for (int i = 2; i < 19; i++){
            addEntite(new Corde(this), 6, i);
            addEntite(new Corde(this), 7, i);
        }

        addEntite(new Mur(this, false), 5, 6);
        
        Radis r = new Radis(this);
        gravite.addEntiteDynamique(r);
        addEntite(r, 15, 2);
        
        Radis r2 = new Radis(this);
        gravite.addEntiteDynamique(r2);
        addEntite(r2, 15, 18);
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

    private void collision(EntiteDynamique perso, Entite objet){
        if (objet instanceof Colonne){
            gravite.removeEntiteDynamique(perso);
            if (perso instanceof Bot){
                // TODO: ajouter points au heros
            } else if (perso instanceof Radis){
                // TODO: enlever points au héros
            } else if (perso instanceof Heros){
                // TODO: fin du jeu
            }
        }
    }

    public boolean deplacerEntite(Entite e, Direction d){
        boolean ret = false;

        Point pCourant = map.get(e);
        Point pCible = calculerPointCible(pCourant, d);

        Entite eCible = ObjetALaPosition(pCible);
        Entite eBas = regarderDansLaDirection(e, Direction.bas);

        boolean remettreCorde = false;
        boolean poserRadis = false;

        if (contenuDansGrille(pCible)){

            if (eCible instanceof Personnage && e instanceof Colonne && regarderDansLaDirection(eCible, d) != null && regarderDansLaDirection(eCible, d).peutServirDeSupport()){
                collision((Personnage) eCible, e);
            }

            if (eCible == null || !eCible.peutServirDeSupport() || eCible.peutEtreEcrase() || eCible.peutPermettreDeMonterDescendre()){ // TODO: penser aux collisions
                switch (d){
                    case haut:
                    case bas:
                        if (cmptDeplV.get(e) == null){
                            cmptDeplV.put(e, 1);

                            if (e instanceof Personnage){
                                Personnage perso = (Personnage) e;

                                if (eCible != null && eCible.peutPermettreDeMonterDescendre()){
                                    if (!perso.monteOuDescend()){
                                        perso.sePoseOuMonte();
                                    }
                                    remettreCorde = true;
                                    ret = true;
                                } else if (eCible == null || !eCible.peutServirDeSupport()){
                                    ret = d == Direction.bas || eBas instanceof Colonne && cmptDeplV.get(eBas) != null;
                                }
                                if (eCible instanceof Radis){
                                    perso.setRadisSurLeChemin((Radis) eCible);
                                }
                            } else if (e instanceof Colonne){
                                if (d == Direction.haut){// Faire monter les entités dynamiques posées sur les colonnes avec les colonnes
                                    if (eCible instanceof EntiteDynamique && !(eCible instanceof Colonne)){
                                        if (deplacerEntite(eCible, d)){
                                            ret = true;
                                        } else if (eCible != null && eCible.peutEtreEcrase()){
                                            collision((EntiteDynamique) eCible, e);
                                            ret = true;
                                        }
                                    } else ret = true;
                                } else ret = true;
                            } else ret = true;
                        }
                        break;
                    case gauche:
                    case droite:
                        if (cmptDeplH.get(e) == null){
                            cmptDeplH.put(e, 1);
                            ret = true;

                            if (e instanceof Personnage){
                                Personnage perso = (Personnage) e;

                                if (perso.vaADroite() ^ d == Direction.droite){
                                    perso.seTourne();
                                }
                                if (perso.estDevantLaCorde() || perso.monteOuDescend()){
                                    remettreCorde = true;
                                }
                                if (eCible instanceof Corde ^ perso.estDevantLaCorde()){
                                    Entite o = ObjetALaPosition(calculerPointCible(calculerPointCible(pCourant, d), Direction.bas));
                                    if (o != null && o.peutServirDeSupport())
                                        perso.passeDevantLaCorde();
                                }
                                if (eCible != null && eCible.peutPermettreDeMonterDescendre()){
                                    if (regarderDansLaDirection(eCible, Direction.bas) != null && !regarderDansLaDirection(eCible, Direction.bas).peutServirDeSupport() && !perso.monteOuDescend()){
                                        perso.sePoseOuMonte();
                                    }
                                }
                                if (eCible == null && perso.monteOuDescend()){
                                    perso.sePoseOuMonte();
                                }
                                if (perso.aUnRadisSurLeChemin()){
                                    poserRadis = true;
                                }
                                if (eCible instanceof Radis){
                                    perso.setRadisSurLeChemin((Radis) eCible);
                                }
                            }
                        }

                        break;
                }
            }
        }

        if (ret){
            
            if (e instanceof Heros && ObjetALaPosition(pCible) instanceof Dynamite){
                ((Heros) e).attraperDynamite();
            }

            deplacerEntite(pCourant, pCible, e);

            if (remettreCorde) addEntite(new Corde(this), pCourant.x, pCourant.y);

            if (poserRadis){
                Radis r;
                if (e instanceof Personnage){
                    r = ((Personnage) e).getRadisSurLeChemin();
                    ((Personnage) e).setRadisSurLeChemin(null);
                } else {
                    r = ((Colonne) e).getRadisSurLeChemin();
                    ((Colonne) e).setRadisSurLeChemin(null);
                }
                
                addEntite(r, pCourant.x, pCourant.y);
            }
        }

        if (e instanceof Personnage){
            Personnage perso = (Personnage) e;
            if (perso.monteOuDescend() && perso.estDevantLaCorde()){
                perso.passeDevantLaCorde();
            }
            if (e instanceof Heros) System.out.println(map.get(e).x + " " + map.get(e).y);
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
    
    public void updateTime(){
        if (ordonnanceur.getTurn() == 0){
            time--;
        }
    }

    public int getTime(){
        return time;
    }
}
