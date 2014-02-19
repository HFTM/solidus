/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           08.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package Interface;

import MainPack.Main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author stecm1
 */
public class InterfacePanel extends JPanel implements MouseListener, ChangeListener, Observer
{
    
    public JTextArea logText;
    JScrollPane scrollPane;
    
    // Text Felder
    
    JTextArea pointText;
    JTextArea stateText;
    JTextArea phaseText;
    JTextArea timeText;
    
    // Beschriftungen
    
    JLabel logLabel;
    JLabel pointLabel;
    JLabel stateLabel;
    JLabel phaseLabel;
    JLabel timeLabel;
    
    JLabel m1Label;
    JLabel m2Label;
    JLabel m3Label;
    JLabel m4Label;
    JLabel m5Label;
    JLabel m6Label;
    JLabel m7Label;
    JLabel m8Label;
    JLabel m9Label;
    JLabel m10Label;
    
    // Knöpfe
    
   JButton start;
   JButton stop;
    
    
    
    public InterfacePanel(int x, int y)
    {
        
        
        setLayout(null);
        setSize(x, y);
        setLocation(0, 0);
        setBackground(Color.CYAN);
        
        // 
        /*
        m1Label = new JLabel("PUNKTE:");
        pointLabel.setSize(100, 45);
        pointLabel.setLocation(120, 0);
        */
        logText = new JTextArea();
        logText.setLineWrap(true);
        logText.setEditable(false);
        logText.setVisible(true);
        
        pointLabel = new JLabel("PUNKTE:");
        pointLabel.setSize(100, 45);
        pointLabel.setLocation(120, 0);
        
        pointText = new JTextArea();
        pointText.setLineWrap(true);
        pointText.setEditable(false);
        pointText.setVisible(true);
        pointText.setSize(100, 25);
        pointText.setLocation(120, 30);
        
        stateLabel = new JLabel("STATE:");
        stateLabel.setSize(100, 45);
        stateLabel.setLocation(10, 0);
        
        stateText = new JTextArea();
        stateText.setLineWrap(true);
        stateText.setEditable(false);
        stateText.setVisible(true);
        stateText.setSize(100, 25);
        stateText.setLocation(10, 30);
        
        phaseLabel = new JLabel("PHASE:");
        phaseLabel.setSize(100, 45);
        phaseLabel.setLocation(10, 50);
        
        phaseText = new JTextArea();
        phaseText.setLineWrap(true);
        phaseText.setEditable(false);
        phaseText.setVisible(true);
        phaseText.setSize(100, 25);
        phaseText.setLocation(10, 80);
        
        timeLabel = new JLabel("PHASE TIME:");
        timeLabel.setSize(100, 45);
        timeLabel.setLocation(10, 100);
        
        timeText = new JTextArea();
        timeText.setLineWrap(true);
        timeText.setEditable(false);
        timeText.setVisible(true);
        timeText.setSize(100, 25);
        timeText.setLocation(10, 130);
        
        start = new JButton();
        start.setText("START");
        start.setSize(100, 50);
        start.setLocation(10, 340);
        start.addMouseListener(this);
        
        stop = new JButton();
        stop.setText("STOP");
        stop.setSize(100, 50);
        stop.setLocation(110, 340);
        stop.addMouseListener(this);
        
        scrollPane = new JScrollPane(logText);
        scrollPane.setSize(370, 350);
        scrollPane.setLocation(400, 50);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        logLabel = new JLabel("LOG:");
        logLabel.setSize(100, 45);
        logLabel.setLocation(400, 10);

        add(stop);
        add(pointLabel);
        add(pointText);
        add(phaseLabel);
        add(stateLabel);
        add(timeLabel);
        add(logLabel);
        add(start);
        add(scrollPane, BorderLayout.CENTER);
        add(stateText);
        add(phaseText);
        add(timeText);
        setVisible(true);
    }

   /**
    * 
    * @param in
    * Schreibt die mitgegebene Nachricht in das Log-Fenster
    */
    
   public void printLog(String in)
   {
       // Setzt das Log-Panel immer ganz nach unten
        
        
        // Schreibt eine Nachricht in das Log-Fenster
        
        logText.append(in + "\n");
        scrollDown();
   }
   
   public void scrollDown()
   {
   /*
    int max;
   
        max = scrollPane.getVerticalScrollBar().getMaximum();
        scrollPane.getVerticalScrollBar().setValue( max );
        scrollPane.repaint();
        */
   }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            
            if (e.getClickCount() == 1)
            {
                
                // connect
                if (e.getSource() == start)
                {
                    try {
                        Main.startServer();
                    } catch (SocketException ex) {
                        Logger.getLogger(InterfacePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
                 // disconect
                if (e.getSource() == stop)
                {
                    Main.stopServer();
                    
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

    @Override
    public void stateChanged(ChangeEvent e)
    {
        
    }
    
           @Override
    public void update(Observable o, Object arg)
    {
        String[] send = (String[]) arg;
        
     String gamePoints = send[0];
     String gamePhase = send[1];
     String gameState = send[2];
     String gameTime = send[3];
     
     
    
    pointText.setText(gamePoints);
    phaseText.setText(gamePhase);
    stateText.setText(gameState);
    timeText.setText(gameTime);
   
    
     
    }

   
    
}
