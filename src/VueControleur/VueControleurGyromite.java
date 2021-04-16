package VueControleur;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

import Modele.Deplacements.Controle4Directions;
import Modele.Deplacements.Direction;
import Modele.Plateau.*;


public class VueControleurGyromite extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private ImageIcon[] icoHero;
    private ImageIcon[] icoBot;
    private ImageIcon[] icoMur;
    private ImageIcon[] icoColonne;
    private ImageIcon icoCorde;
    private ImageIcon[] icoHolder;
    private ImageIcon icoRadis;
    private ImageIcon icoDynamite;
    private ImageIcon[] death;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)
    private JLabel[] tabJPanneau;

    public VueControleurGyromite(Jeu _jeu) {
        sizeX = jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite");
        setResizable(false);
        setSize(18 * sizeX, 19 * (sizeY + 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent panneau = new JPanel(new GridLayout(1, 2));
        tabJPanneau = new JLabel[2];

        JLabel score = new JLabel("", 0);
        score.setOpaque(true);
        score.setForeground(Color.WHITE);
        score.setBackground(Color.BLACK);
        panneau.add(score);
        tabJPanneau[0] = score;

        JLabel time = new JLabel("", 0);
        time.setOpaque(true);
        time.setForeground(Color.WHITE);
        time.setBackground(Color.BLACK);
        panneau.add(time);
        tabJPanneau[1] = time;

        add(panneau, BorderLayout.NORTH);
        
        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                jlab.setOpaque(true);
                jlab.setBackground(Color.BLUE);
                jlab.setSize(18, 19);
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels, BorderLayout.SOUTH);
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : Controle4Directions.getInstance().setDirectionCourante(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : Controle4Directions.getInstance().setDirectionCourante(Direction.droite); break;
                    case KeyEvent.VK_DOWN : Controle4Directions.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_UP : Controle4Directions.getInstance().setDirectionCourante(Direction.haut); break;
                    case KeyEvent.VK_C : jeu.getHector().attraperPoserRadis(); break; // Attraper radis
                    case KeyEvent.VK_D : jeu.getControlleur(true).ouvrirFermer(); break;
                    case KeyEvent.VK_F : jeu.getControlleur(false).ouvrirFermer(); break; // Colonnes rouges
                }
            }
        });
    }

    private void mettreAJourAffichage(){
        if (jeu.getTime() == 0){
            dispose();
        }
        tabJPanneau[0].setText("<html><b>Score : " + jeu.getHector().getScore() + "</b></html>");
        tabJPanneau[1].setText("<html><b>Time : " + jeu.getTime() + "</b></html>");

        for (int x = 0; x < sizeX; x++){
            for (int y = 0; y < sizeY; y++){
                if (!jeu.estFini()){
                    int d;
                    if (jeu.getGrille()[x][y] instanceof Personnage){
                        d = 0;
    
                        if (!((Personnage) jeu.getGrille()[x][y]).monteOuDescend()){
                            d = ((Personnage) jeu.getGrille()[x][y]).vaADroite() ? 2 : 1;
    
                            if (((Personnage) jeu.getGrille()[x][y]).estDevantLaCorde()) d += 2;
    
                            if (((Personnage) jeu.getGrille()[x][y]).aUnRadis()) d += 4;
                        }
                        
    
                        ImageIcon[] icones = jeu.getGrille()[x][y] instanceof Heros ? icoHero : icoBot;
    
                        tabJLabel[x][y].setIcon(icones[d]);
                    } else if (jeu.getGrille()[x][y] instanceof Colonne){
                        d = ((Colonne) jeu.getGrille()[x][y]).estBleue() ? 0 : 1;
                        tabJLabel[x][y].setIcon(icoColonne[d]);
                    } else if (jeu.getGrille()[x][y] instanceof Mur){
                        if (((Mur) jeu.getGrille()[x][y]).estUneBrique()) d = 2;
                        else d = ((Mur) jeu.getGrille()[x][y]).estVertical() ? 1 : 0;
                        tabJLabel[x][y].setIcon(icoMur[d]);
                    } else if (jeu.getGrille()[x][y] instanceof Corde){
                        tabJLabel[x][y].setIcon(icoCorde);
                    } else if (jeu.getGrille()[x][y] instanceof Holder){
                        d = ((Holder) jeu.getGrille()[x][y]).droite() ? 1 : 0;
                        tabJLabel[x][y].setIcon(icoHolder[d]);
                    } else if (jeu.getGrille()[x][y] instanceof Dynamite){
                        tabJLabel[x][y].setIcon(icoDynamite);
                    } else if (jeu.getGrille()[x][y] instanceof Radis){
                        tabJLabel[x][y].setIcon(icoRadis);
                    } else {
                        tabJLabel[x][y].setIcon(null);
                    }
                } else if (!jeu.won()){
                    tabJLabel[x][y].setBackground(Color.BLACK);
                    if (x == 10 && y == 10){
                        tabJLabel[x][y].setIcon(death[jeu.getDeath() == null ? 2 : (jeu.getDeath() == Direction.droite ? 0 : 1)]);
                    } else tabJLabel[x][y].setIcon(null);
                }
            }
        }
    }

    protected static ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new ImageIcon(image);
    }

    public void chargerLesIcones(){
        icoHero = new ImageIcon[]{
            chargerIcone("Images/Entites/Corde/player_monte_dc.png"),
            chargerIcone("Images/Entites/player_left.png"),
            chargerIcone("Images/Entites/player_right.png"),
            chargerIcone("Images/Entites/Corde/player_left_dc.png"),
            chargerIcone("Images/Entites/Corde/player_right_dc.png"),
            chargerIcone("Images/Entites/player_left_radis.png"), 
            chargerIcone("Images/Entites/player_right_radis.png"),
            chargerIcone("Images/Entites/Corde/player_left_radis_dc.png"), 
            chargerIcone("Images/Entites/Corde/player_right_radis_dc.png")
        };
        icoColonne = new ImageIcon[]{
            chargerIcone("Images/Evt/pilier_bleu.png"), 
            chargerIcone("Images/Evt/pilier_rouge.png")
        };
        icoMur = new ImageIcon[]{
            chargerIcone("Images/Evt/mur_horizontal.png"), 
            chargerIcone("Images/Evt/mur_vertical.png"),
            chargerIcone("Images/Evt/mur_briques.png")
        };
        icoCorde = chargerIcone("Images/Evt/corde.png");
        icoHolder = new ImageIcon[]{
            chargerIcone("Images/Evt/pilier_holder_gauche.png"),
            chargerIcone("Images/Evt/pilier_holder_droite.png")
        };
        icoDynamite = chargerIcone("Images/Entites/bombe.png");
        icoBot = new ImageIcon[]{
            chargerIcone("Images/Entites/Corde/ennemi_monte_dc.png"),
            chargerIcone("Images/Entites/ennemi_left.png"),
            chargerIcone("Images/Entites/ennemi_right.png"),
            chargerIcone("Images/Entites/Corde/ennemi_left_dc.png"),
            chargerIcone("Images/Entites/Corde/ennemi_right_dc.png"),
            chargerIcone("Images/Entites/radis_ennemis/ennemi_radis_left.png"),
            chargerIcone("Images/Entites/radis_ennemis/ennemi_radis_right.png")
        };
        icoRadis = chargerIcone("Images/Entites/radis.png");
        death = new ImageIcon[]{
            chargerIcone("Images/Entites/collisions/collision_ennemi_left.png"),
            chargerIcone("Images/Entites/collisions/collision_ennemi_right.png"),
            chargerIcone("Images/Entites/collisions/dead.png")
        };
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }
}
