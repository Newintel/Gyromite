package Modele.Deplacements;

public class ControleColonneBleue extends ControleColonne{
    private static ControleColonneBleue cc;

    public static ControleColonneBleue getInstance(){
        if (cc == null)
            cc = new ControleColonneBleue();
        
        return cc;
    }
}
