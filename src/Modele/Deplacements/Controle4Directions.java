package Modele.Deplacements;
import Modele.Plateau.*;

public class Controle4Directions extends RealisateurDeDeplacements<EntiteDynamique> {
    private Direction directionCourante;
    // Design pattern singleton
    private static Controle4Directions c3d;

    public static Controle4Directions getInstance() {
        if (c3d == null) {
            c3d = new Controle4Directions();
        }
        return c3d;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : listEntitesDynamiques) {
            if (directionCourante != null){
                if (e.avancerDirectionChoisie(directionCourante))
                    ret = true;
            }
        }

        return ret;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }

    public void resetDirection() {
        directionCourante = null;
    }
}
