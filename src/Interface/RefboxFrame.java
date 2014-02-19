/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           08.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package Interface;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 *
 * @author stecm1
 */
public class RefboxFrame implements ActionListener 
{
     
    int sizeX = 400;
    int sizeY = 400;
     
     JFrame applikation;
     Container container;
     public RefboxPanel panel;
     
  
     public RefboxFrame(InterfacePanel interfacePanel)
     {
          applikation = new JFrame("Refbox");
          container = applikation.getContentPane();
          
          panel = new RefboxPanel(sizeX, sizeY, this, interfacePanel);
      
          
          applikation.setSize(sizeX, sizeY);
          applikation.setLocation(100, 100);
          
          applikation.add(panel);
          applikation.setVisible(true);
          
          
     }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        
    }
}
     


