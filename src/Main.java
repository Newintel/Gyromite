import VueControleur.VueControleurGyromite;
import Modele.Plateau.Jeu;

public class Main{
    public static void main(String[] args) {
        Jeu jeu = new Jeu();
        
        VueControleurGyromite vc = new VueControleurGyromite(jeu);

        jeu.getOrdonnanceur().addObserver(vc);
        
        vc.setVisible(true);
        jeu.start(500);
    }
}