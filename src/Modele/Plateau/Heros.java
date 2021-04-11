package Modele.Plateau;

public class Heros extends Personnage{
    private int hp = 3;
    private int nombreDeDynamites = 0;
    private boolean radis = false;

    public Heros(Jeu _jeu){
        super(_jeu);
    }

    public void attraperDynamite() { nombreDeDynamites++; }
    public int getNombreDeDynamites() { return nombreDeDynamites; }

    public void attraperPoserRadis(){ radis = !radis; }
    public boolean aUnRadis(){ return radis; }

    public void seBlesser(){ hp--; }
    public int getHp(){ return hp; }
}
