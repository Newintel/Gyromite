package Modele.Plateau;

public class Heros extends Personnage{
    private int hp = 3;
    private int nombreDeDynamites = 0;
    private Radis radisDuHeros;

    public Heros(Jeu _jeu){
        super(_jeu);
    }

    public void attraperDynamite() { nombreDeDynamites++; }
    public int getNombreDeDynamites() { return nombreDeDynamites; }

    public void seBlesser(){ hp--; }
    public int getHp(){ return hp; }

    public void attraperRadis(){
        if (!radis){
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
}
