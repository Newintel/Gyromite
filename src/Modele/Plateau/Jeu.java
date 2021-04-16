package Modele.Plateau;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import Modele.Deplacements.*;

public class Jeu {
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 20;
    private boolean fin;
    private int time = 120;
    private int nbDyn;
    private Direction death = null;
    private boolean won;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private HashMap<String, ArrayList<ArrayList<Integer>>> gameMap;

    private Heros hector;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private ControleColonne cr = new ControleColonne();
    private ControleColonne cb = new ControleColonne();

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu(HashMap<String, ArrayList<ArrayList<Integer>>> gMap){
        gameMap = gMap;
        initialisationDesEntites();
    }
    // public Jeu(){
    //     initialisationDesEntites();
    // }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    private void addEntite(Entite e, int x, int y){
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }

    private void addPlayer(ArrayList<Integer> l){
        int x = l.get(0);
        int y = l.get(1);
        hector = new Heros(this);
        addEntite(hector, x, y);
        Gravite.getInstance().addEntiteDynamique(hector);
        Controle4Directions.getInstance().addEntiteDynamique(hector);
        System.out.println("yaaas");
    }

    private void addCorde(ArrayList<Integer> l){
        int x = l.get(0);
        int yd = l.get(1);
        int ye = l.get(2);
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

    private void addColonne(ArrayList<Integer> l){
        int x = l.get(0);
        int yd = l.get(1);
        int ye = l.get(2);
        boolean estBleue = l.get(3) == 1;
        ArrayList<Colonne> c = new ArrayList<Colonne>();
        for (int i = yd; i <= ye; i++){
            Colonne toAdd = new Colonne(this, estBleue, i == yd || i == ye);
            c.add(toAdd);
            addEntite(toAdd, x, i);
        }
        ControleColonne a = estBleue ? cb : cr;
        a.addEntiteDynamique(c);
    }

    private void addMur(ArrayList<Integer> l){
        int c = l.get(0);
        int start = l.get(1);
        int end = l.get(2);
        boolean v = l.get(3) == 1;
        for (int i = start; i <= end; i++){
            addEntite(new Mur(this, v), v ? c : i, v ? i : c);
        }
    }

    private void addBot(ArrayList<Integer> l){
        int x = l.get(0);
        int y = l.get(1);
        Bot b = new Bot(this);
        Gravite.getInstance().addEntiteDynamique(b);
        addEntite(b, x, y);
    }

    private void addRadis(ArrayList<Integer> l){
        int x = l.get(0);
        int y = l.get(1);
        Radis r = new Radis(this);
        Gravite.getInstance().addEntiteDynamique(r);
        addEntite(r, x, y);
    }

    private void addHolder(ArrayList<Integer> l){
        int x = l.get(0);
        int y = l.get(1);
        boolean droite = l.get(3) == 1;
        Holder h = new Holder(this, droite);
        addEntite(h, x, y);
    }

    private void addDynamite(ArrayList<Integer> l){
        int x = l.get(0);
        int y = l.get(1);
        addEntite(new Dynamite(this), x, y);
        nbDyn++;
    }

    private void addEntity(String _name, ArrayList<Integer> l){
        String[] f = _name.split("");
        f[0] = f[0].toUpperCase();
        String name = "add";
        for (String s : f) name += s;
        try{
            Method method = getClass().getDeclaredMethod(name, ArrayList.class);
            method.invoke(this, l);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void initialisationDesEntites() {
        addContour();

        for (String key : gameMap.keySet()){
            for (ArrayList<Integer> l : gameMap.get(key)){
                addEntity(key, l);
            }
        }

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
            map.remove(perso);
            if (perso instanceof Bot){
                hector.addPts(500);
            } else if (perso instanceof Radis){
                hector.addPts(-50);
            } else if (perso instanceof Heros){
                fin = true;
            }
        } else if (perso instanceof Bot && objet instanceof Heros || perso instanceof Heros && objet instanceof Bot){
            death = ((Bot) (perso instanceof Bot ? perso : objet)).vaADroite() ? Direction.droite : Direction.gauche;
            fin = true;
            System.out.println("ded");
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

    public Point getPositionHector(){
        return map.get(hector);
    }

    public void start(long _pause){
        ordonnanceur.start(_pause);
    }
    
    public void updateTime(){
        if (!fin && nbDyn != 0 && time > 0){
            if (ordonnanceur.getTurn() == 0) time--;
        } else {
            fin = true;
            if (nbDyn != 0){
                won = false;
                time = 0;
            } else won = true;
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
        } else if (time == 0){
            time = -10;
        }
    }

    public Direction getDeath(){
        return death;
    }

    public boolean won(){
        return won;
    }
}
