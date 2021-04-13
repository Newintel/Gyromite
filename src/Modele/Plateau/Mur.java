package Modele.Plateau;

public class Mur extends EntiteStatique{
    private final boolean vertical;
    private final boolean brique;

    public Mur(Jeu _jeu, boolean _v){
        super(_jeu);
        vertical = _v;
        brique = false;
    }

    public Mur(Jeu _jeu){
        super(_jeu);
        brique = true;
        vertical = false;
    }

    public boolean estVertical(){ return vertical; }
    public boolean estUneBrique(){ return brique; }
}
