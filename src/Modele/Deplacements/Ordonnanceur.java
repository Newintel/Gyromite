package Modele.Deplacements;

import Modele.Plateau.Jeu;

import java.util.ArrayList;
import java.util.Observable;

import static java.lang.Thread.*;

public class Ordonnanceur extends Observable implements Runnable {
    private Jeu jeu;
    private int turn;
    private ArrayList<RealisateurDeDeplacements> listDeplacements = new ArrayList<RealisateurDeDeplacements>();
    private long pause;
    public void add(RealisateurDeDeplacements deplacement) {
        listDeplacements.add(deplacement);
    }

    public Ordonnanceur(Jeu _jeu) {
        jeu = _jeu;
    }

    public void start(long _pause) {
        pause = _pause;
        new Thread(this).start();
    }

    public int getTurn(){ return turn; }

    @Override
    public void run() {
        boolean update = false;

        while(true) {
            jeu.resetCmptDepl();
            turn = (turn + 1) % (int)(1000 / pause);
            jeu.updateTime();
            if (jeu.getTime() == 0){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            if (jeu.estFini()){
                jeu.updateScoreFinal();
            } else{
                for (RealisateurDeDeplacements d : listDeplacements) {
                    d.updateControlleur();
                    if (d.realiserDeplacement())
                        update = true;
                }
            }

            Controle4Directions.getInstance().resetDirection();

            if (update) {
                setChanged();
                notifyObservers();
            }

            try {
                sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
