package Modele.Plateau;

public class Holder extends EntiteStatique {
    private final boolean droite;

    public Holder(Jeu _jeu, boolean d){
        super(_jeu);
        droite = d;
    }

    public boolean droite(){ return droite; }
}
