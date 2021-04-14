package Modele.Plateau;

public class Heros extends Personnage{
    private int nombreDeDynamites = 0;
    private Radis radisDuHeros;
    private long score;
    private boolean intouchable;
    private int momentIntouchable;

    public Heros(Jeu _jeu){
        super(_jeu);
    }

    public void attraperDynamite() { nombreDeDynamites++; } // TODO: ajouter points au heros
    public int getNombreDeDynamites() { return nombreDeDynamites; }

    public boolean estIntouchable(){ return intouchable; }

    public void attraperPoserRadis(){
        if (!radis && aUnRadisSurLeChemin()){
            radisDuHeros = getRadisSurLeChemin();
            radis = true;
            setRadisSurLeChemin(null);
        } else if (radis && !estDevantLaCorde()){
            radis = false;
            setRadisSurLeChemin(radisDuHeros);
            radisDuHeros = null;
        }
    }
    
    public Radis getRadis(){ return radisDuHeros; }

    public long getScore(){ return score; }
    public void addPts(long toAdd){ score += toAdd; }
}
