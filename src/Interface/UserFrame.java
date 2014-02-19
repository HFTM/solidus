/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           08.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package Interface;


/**
 *
 * @author stecm1
 */
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class UserFrame implements ActionListener {
    
    int sizeX = 800;
    int sizeY = 500;
     
     JFrame applikation;
     Container container;
     
     // Menüleiste
     JMenuBar menueLeiste;
     
     // Menüleiste Elemente
     JMenu datei;
     JMenu optionen;
     
     // Datei
     
     JMenuItem beenden;
     
     // Hilfe
     JMenuItem refbox;
     JMenuItem robo;
     
     public RefboxFrame opt;
     
     public InterfacePanel panel;
     
     

    
     
     public UserFrame() {
          applikation = new JFrame("ServerSolidus");
          container = applikation.getContentPane();
         
          
          // Menüleiste erzeugen
          menueLeiste = new JMenuBar();
          
          // Menüelemente erzeugen
          datei = new JMenu("Datei");
          optionen = new JMenu("Optionen");
          
          // Untermenüelemente erzeugen
          
          
          beenden = new JMenuItem("Beenden");
          beenden.addActionListener(this);
          refbox = new JMenuItem("Refbox");
          refbox.addActionListener(this);
          robo = new JMenuItem("Robotino");
          robo.addActionListener(this);
          
          
          // Menüelemente hinzufügen
          menueLeiste.add(datei);
          menueLeiste.add(optionen);
          
          // Untermenüelemente hinzufügen
          
          datei.add(beenden);
          optionen.add(refbox);
          optionen.add(robo);

          // Textfeld erzeugen
                 
          
          applikation.add(menueLeiste, BorderLayout.NORTH);
          applikation.setSize(sizeX, sizeY);
          applikation.setLocation(100, 100);
          applikation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          
          panel = new InterfacePanel(sizeX, sizeY);
          
          
          applikation.add(panel);
          applikation.setVisible(true);
     }
     
    @Override
     public void actionPerformed(ActionEvent object) {
          
          
          if (object.getSource() == beenden){
               System.exit(0);
          }
          if (object.getSource() == refbox){
              opt = new RefboxFrame(panel);
          }
          if (object.getSource() == robo){
              
          }
     }
     
     
}