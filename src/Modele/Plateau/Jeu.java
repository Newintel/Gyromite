package Modele.Plateau;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Point;
import Modele.Deplacements.*;

public class Jeu {
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 20;
    private boolean fin;
    private int time = 120;
    private int nbDyn;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private ControleColonne cr = new ControleColonne();
    private ControleColonne cb = new ControleColonne();

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

    private void addCorde(int x, int yd, int ye){
        for (int i = yd; i <= ye; i++){
            addEntite(new Corde(this), x, i);
        }
    }

    private void addContour(){
        for(int x = 0; x < SIZE_X; x++){
            addEntite(new Mur(this), x, 0);
            addEntite(new Mur(this), x, SIZE_Y - 1);
        }

        for (int y = 1; y < SIZE_Y - 1; y++){
            addEntite(new Mur(this), 0, y);
            addEntite(new Mur(this), SIZE_X - 1, y);
        }
    }

    private void addColonne(int x, int yd, int ye, boolean estBleue){
        ArrayList<Colonne> c = new ArrayList<Colonne>();
        for (int i = yd; i <= ye; i++){
            Colonne toAdd = new Colonne(this, estBleue, i == yd || i == ye);
            c.add(toAdd);
            addEntite(toAdd, x, i);
        }
        ControleColonne a = estBleue ? cb : cr;
        a.addEntiteDynamique(c);
    }

    private void initialisationDesEntites() {
        
        hector = new Heros(this);
        addEntite(hector, 15, 15);
        Gravite.getInstance().addEntiteDynamique(hector);
        Controle4Directions.getInstance().addEntiteDynamique(hector);

        addContour();
        
        addColonne(10, 4, 12, false);
        addEntite(new Mur(this), 10, 1);
        
        addEntite(new Holder(this, false), 9, 10);

        // addColonne(15, 3, 11, true);

        addEntite(new Holder(this, true), 16, 10);
        
        addCorde(6, 2, 18);
        addCorde(7, 2, 18);
        
        addEntite(new Mur(this, false), 5, 6);
        
        Radis r = new Radis(this);
        Gravite.getInstance().addEntiteDynamique(r);
        addEntite(r, 15, 2);
        
        addEntite(new Dynamite(this), 2, 15);
        nbDyn++;

        ordonnanceur.add(Gravite.getInstance());
        ordonnanceur.add(cr);
        ordonnanceur.add(cb);
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
            case haut: ret = new Point(pCourant.x, pCourant.y - 1); break;
            case bas: ret = new Point(pCourant.x, pCourant.y + 1); break;
            case droite: ret = new Point(pCourant.x + 1, pCourant.y); break;
            case gauche: ret = new Point(pCourant.x - 1, pCourant.y); break;
        }

        return ret;
    }

    private void collision(EntiteDynamique perso, Entite objet){
        if (objet instanceof Colonne){
            Gravite.getInstance().removeEntiteDynamique(perso);
            if (perso instanceof Bot){
                hector.addPts(500);
            } else if (perso instanceof Radis){
                hector.addPts(-50);
            } else if (perso instanceof Heros){
                // TODO: fin du jeu
                fin = true;
                System.out.println("Ded");
            }
        } else if (perso instanceof Bot){
            if (objet instanceof Heros){
                // TODO: fin du jeu
                fin = true;
                System.out.println("ded");
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
                            if (e instanceof Radis){
                                if (eCible instanceof Personnage){
                                    ((Personnage) eCible).setRadisSurLeChemin((Radis) e);
                                    grilleEntites[pCourant.x][pCourant.y] = null;
                                    map.put(null, pCourant);
                                } else ret = true;
                            } else if (e instanceof Personnage){
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
                                        } else if (eCible != null && eCible.peutEtreEcrase() && cmptDeplV.get(eCible) == null){
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
                nbDyn--;
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
                Gravite.getInstance().addEntiteDynamique(r);
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

    public ControleColonne getControlleur(boolean bleu){
        return bleu ? cb : cr;
    }

    public Heros getHector(){
        return hector;
    }

    public void start(long _pause){
        ordonnanceur.start(_pause);
    }
    
    public void updateTime(){
        if (!fin && nbDyn != 0 && time > 0){
            if (ordonnanceur.getTurn() == 0) time--;
        } else {
            fin = true;
            if (nbDyn != 0) time = 0;
        }
    }

    public int getTime(){
        return time;
    }

    public boolean estFini(){
        return fin;
    }

    public void updateScoreFinal(){
        if (time > 10){
            time -= 10;
            hector.addPts(1000);
        } else if (time > 0){
            time--;
            hector.addPts(100);
        }
    }


}
