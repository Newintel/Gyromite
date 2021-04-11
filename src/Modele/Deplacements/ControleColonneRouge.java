package Modele.Deplacements;

public class ControleColonneRouge extends ControleColonne {
    private static ControleColonneBleue cc;

    public static ControleColonneBleue getInstance(){
        if (cc == null)
            cc = new ControleColonneBleue();
        
        return cc;
    }
}
