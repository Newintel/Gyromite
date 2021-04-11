package Modele.Plateau;

public class Mur extends EntiteStatique{
    private final boolean vertical;

    public Mur(Jeu _jeu, boolean _v){
        super(_jeu);
        vertical = _v;
    }

    public boolean estVertical(){ return vertical; }
}
