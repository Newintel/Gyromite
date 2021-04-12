package Modele.Plateau;

public class Bot extends Personnage {
    private boolean bouffeUnRadis;

    public Bot(Jeu _jeu){
        super(_jeu);
    }

    public boolean bouffeUnRadis(){ return bouffeUnRadis; }
    public void trouveUnRadis(){ bouffeUnRadis = !bouffeUnRadis; }

    public void taperHeros(){
        if (!bouffeUnRadis){

        }
    }
}
