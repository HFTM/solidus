/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           08.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */

package Interface;

import ComView.FileIO;
import MainPack.Main;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author stecm1
 */
public class RefboxPanel extends JPanel  implements MouseListener 
{
    
    JLabel ipLabel = new JLabel("Refbox IP:");
    JLabel portLabel = new JLabel("Refbox Port:");
    JLabel nameLabel = new JLabel("Robotino Name:");
    
    JTextField ip = new JTextField();
    JTextField port = new JTextField();
    JTextField roboname = new JTextField();
    
    JButton ok = new JButton();
    RefboxFrame frame;
    InterfacePanel interfacePanel;
    

    
    public RefboxPanel(int sizeX, int sizeY, RefboxFrame frame, InterfacePanel interfacePanel)
    {
        this.interfacePanel = interfacePanel;
        this.frame = frame;
        
          setLayout(null);
          setLocation(0, 0);
          setSize(sizeX, sizeY);
          setBackground(Color.MAGENTA);
          
          ip.setText(Main.refBoxIp);
          ip.setLocation(200, 100);
          ip.setSize(180, 40);
          
          port.setText(Main.refBoxPortIn + "");
          port.setLocation(200, 150);
          port.setSize(180, 40);
          
          roboname.setText(Main.name);
          roboname.setLocation(200, 200);
          roboname.setSize(180, 40);
          
          portLabel.setSize(100, 45);
          portLabel.setLocation(100, 150);
          
          nameLabel.setSize(100, 45);
          nameLabel.setLocation(100, 200);
          
          ipLabel.setSize(100, 45);
          ipLabel.setLocation(100, 100);
          
          ok.setText("OK");
          ok.setSize(100, 50);
          ok.setLocation(10, 320);
          ok.addMouseListener(this);
          
          add(roboname);
          add(nameLabel);
          add(port);
          add(portLabel);
          add(ipLabel);
          add(ip);
          add(ok);
          setVisible(true);
          
          
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            
            if (e.getClickCount() == 1)
            {
                
                // Zulaufventil
                if (e.getSource() == ok)
                {
                    try
                    {
                        Main.setIpRefbox(ip.getText());
                        Main.setNameRobo(roboname.getText());
                        frame.applikation.dispose();
                        interfacePanel.printLog("Refbox-IP wurde auf " + Main.refBoxIp + ":" + Main.refBoxPortIn + " geändert!");
                        
                    }
                    catch (FileNotFoundException ex)
                    {
                        Logger.getLogger(RefboxPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(RefboxPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
    }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        
    }

  
     
   
         
     }
     