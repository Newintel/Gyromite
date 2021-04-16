package VueControleur;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

import org.json.simple.parser.JSONParser;

import Modele.Plateau.Jeu;

public class GUI implements ActionListener, MouseInputListener
{
    MapFileReader mapFileReader = new MapFileReader();

    //MenuGUI
    JFrame frmMenu = new JFrame();
    JPanel pnlMenu = new JPanel();
    JLabel lblTitleMenu = new JLabel(VueControleurGyromite.chargerIcone("Images/title.png"));
    JButton btnPlay = new JButton("Jouer");
    JButton btnExit = new JButton("Quitter");

    //ChooseMapGUI
    JFrame frmChooseMap = new JFrame();
    JPanel pnlChooseMap = new JPanel();
    JLabel lblTitleChooseMap = new JLabel("Choisissez une carte : ");

    ArrayList<File> maps = new ArrayList<File>();

    public void MenuGUI()
    {
        frmMenu.setPreferredSize(new Dimension(300, 200));

        btnPlay.addActionListener(this);
        btnExit.addActionListener(this);

        pnlMenu.setBorder(BorderFactory.createEmptyBorder());
        pnlMenu.setLayout(new GridLayout(0, 1));

        pnlMenu.add(lblTitleMenu);
        pnlMenu.add(btnPlay);
        pnlMenu.add(btnExit);

        frmMenu.add(pnlMenu, BorderLayout.CENTER);
        frmMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMenu.setTitle("Gyromite");
        frmMenu.pack();
        frmMenu.setVisible(true);
    }

    public void chooseMapGUI()
    {
        frmChooseMap.setPreferredSize(new Dimension(300, 200));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder());
        pnlMenu.setLayout(new GridLayout(0, 1));
        pnlChooseMap.add(lblTitleChooseMap);

        for (File map : mapFileReader.getAllAvailableMapsFiles())
        {
            JLabel lblMap = new JLabel(map.getName());
            lblMap.addMouseListener(this);
            pnlChooseMap.add(lblMap);
        }

        frmChooseMap.add(pnlChooseMap);
        frmChooseMap.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmChooseMap.setTitle("Gyromite");
        frmChooseMap.pack();
        frmChooseMap.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if ((JButton)e.getSource() == btnPlay)
        {
            frmMenu.setVisible(false);
            chooseMapGUI();
        }
        if ((JButton)e.getSource() == btnExit)
            System.exit(0);
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {
        HashMap<String, ArrayList<ArrayList<Integer>>> gameMap = mapFileReader.loadGameMap(((JLabel) e.getSource()).getText());
        Jeu jeu = new Jeu(gameMap);
        
        VueControleurGyromite vc = new VueControleurGyromite(jeu);

        jeu.getOrdonnanceur().addObserver(vc);
        
        vc.setVisible(true);
        jeu.start(100);
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}
