/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           29.05.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */

package ComView;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ComView extends Thread
{
    int counter = 0;

    // UDP - Socket
    
    UDPServer com;
    DatagramSocket serverSocket;
    
    // Speicher für lesende Nachricht
    
   // int[] msg;
    
    // Senden
    
    int x;                      // X - Koordinate
    int y;                      // Y - Koordinate
    int phi;                    // Winkel
    int check;                  // Kontrollbit
    int station;                // Stationsanfahren
    int go;                     // Empfangsbestätigung
    
    // Empfangen
    
    int ready;                  // Step ausgeführt
    int red;                    // Zustand rote Lampe
    int orange;                 // Zustand orange Lampe
    int green;                  // Zustand güne Lampe
    int ende;                   // Empfangsbestätigung
    
    // Schleiffenbedingung
    
    public boolean run = true;

    public ComView() throws SocketException
    {
        // Reverentieren
        com = new UDPServer();
        
        
       // msg = new int[10];

        x = 0;
        y = 0;
        phi = 0;
        check = 0;
        station = 0;
        go = 0;
        
        ready = 0;
        red = 0;
        orange = 0;
        green = 0;
        ende = 0;


    }
    
     @Override
    public void run()
    {
        while(run == true)
        {
           
            
            try
            {
                serverSocket = new DatagramSocket(5000);
                int[] sendKoor = {x, y, phi, station, check, go, 0, 0};

                com.sendViewMessage(sendKoor, "127.0.0.1", 5001);
                
                int[]  msg = com.getViewMessagr(serverSocket);
                ready = msg[0];
                red  = msg[1];
                orange = msg[2];
                green = msg[3];
                ende = msg[4];
                
                serverSocket.close();

            }
            
            catch (Exception ex)
            {
                Logger.getLogger(ComView.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+- " + ++counter + " -+-+-+-+-+-+-+-+-+-+-+-+-");

            try
            {
                Thread.sleep(100);
            }
            
            catch (InterruptedException ex)
            {
                Logger.getLogger(ComView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

 
// ------------------------- setAllKoor ----------------------------------------

    public void setAllKoor(String xyphi)
    { 
       
       String [] koor = xyphi.split(" ");
	
       check = Integer.valueOf( koor[0]);
       x =Integer.valueOf( koor[1]);
       y =Integer.valueOf( koor[2]);
       phi =Integer.valueOf( koor[3]);
           
        System.out.println(">>>>>>>>>>>>> SET ALL KOORDINAEN <<<<<<<<<<<<<<<");
    }
    
    public void setGo(int go)
    {
            this.go = go;    
    }
      
    /**
     * Gibt die zuletzt gelesenen Lampenfarben zurück: rot,orange,grün
     * 0 = aus, 1 = leuchtet, 2 = blinkt
     * @return 
     */
    public int[] getLamp()
    {
        int[] lamp = {red,orange,green};
        return lamp;
    }
    
    public int getReady()
    {
        System.out.println(">>>>>>>>>>>>>>> GET NOW READY <<<<<<<<<<<<<<");
        return ready;
    }
    
    public int getEnde()
    {
        
        System.out.println("Ende wurde empfagnen: " + ende);
        return ende;
    }
    
    /**
     * Gibt befehl die Station anzufahren 1/0
     * @return 
     */
    public void setStation(int go)
    {
        station = go;
        System.out.println(station);
    }
 
}
class Main
{
    public static void main(String[] args)
    {
        try
        {
            ComView com = new ComView();
            com.start();
        }
        catch (SocketException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }


}
