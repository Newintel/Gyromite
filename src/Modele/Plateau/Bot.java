package Modele.Plateau;

public class Bot extends Personnage {
    public Bot(Jeu _jeu){
        super(_jeu);
    }
    public void attraperRadis(){
        if (aUnRadisSurLeChemin())
            radis = true;
    }

    public void taperHeros(){
        if (!radis){

        }
    }
}
