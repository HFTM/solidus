/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           08.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package MainPack;


import ComView.ComView;
import ComView.FileIO;
import Interface.InterfacePanel;
import Interface.UserFrame;
import Programm.ProgrammRobo;
import Refbox.ComRefbox;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author stecm1
 */
public class Main
{
    
  static UserFrame frame;
  public static String refBoxIp;
  public static String refBoxPortIn = "4444";
  public static String refBoxPortOut = "4444";
  public static String name = "MrPink";
  static ComRefbox comRefbox;
  static ProgrammRobo robo;
  static ComView comView;
  
  static File ipfile;
  static File portfile;
  static File namefile;
  static String relativ;
  
// -----------------------------------------------------------------------------
// ------------------------------   MAIN   -------------------------------------
// -----------------------------------------------------------------------------
    public static void main(String[] args) throws FileNotFoundException, InterruptedException
    {
        
        
        frame = new UserFrame();
        
      //  System.setOut(filestream);
        
        final Foo foo = new Foo(frame.panel);
        PrintStream someOtherStream = new PrintStream(System.out) {
 
          //  @Override
         //   public void println(String s) {
        //        foo.onEvent(s);
        //    }
        };
        
        System.setOut(someOtherStream);
       
        try
        {
      
        
        ipfile = new File("C:/Robotino/iprefbox");
        namefile = new File("C:/Robotino/name");
        
        FileIO read = new FileIO();
        read.getText(ipfile);
        read.getText(namefile);
        
        refBoxIp = read.getText(ipfile);
        name = read.getText(namefile);
        

        
        //comRefbox = new ComRefbox(refBoxIp, refBoxPort, frame);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
// -----------------------------------------------------------------------------      
      
                 
    }

    public static void setIpRefbox(String ip) throws FileNotFoundException, IOException
    {
        refBoxIp = ip;
        System.out.println(ip);
        FileWriter schreiber = new FileWriter(ipfile);
        schreiber.write(ip);
        schreiber.flush();
        
    }
    
    public static void setNameRobo(String roboname) throws FileNotFoundException, IOException
    {
        name = roboname;
        FileWriter schreiber = new FileWriter(namefile);
        schreiber.write(roboname);
        schreiber.flush();
        
    }
    
     public static void setPortRefbox(String port) throws FileNotFoundException, IOException
    {
        refBoxPortIn = port;
        System.out.println(port);
        FileWriter schreiber = new FileWriter(portfile);
        schreiber.write(port);
        schreiber.flush();
        schreiber.close();
        
    }

// -----------------------------------------------------------------------------
// ------------------------------   START   ------------------------------------
// -----------------------------------------------------------------------------


     
    public static void startServer() throws SocketException
    {
        
        System.out.println("Startbutten is cliced");
        comView = new ComView();
        comRefbox = new ComRefbox(refBoxIp, Integer.valueOf(refBoxPortIn), Integer.valueOf(refBoxPortOut),name, frame);
        robo = new ProgrammRobo(comRefbox.handler, name, comView);
        
        
        
// Nachrichten Refbox
        comRefbox.addGameStateMessage();
        comRefbox.addMachineInfo();
        comRefbox.addExplorationInfo();
        comRefbox.addOrderInfo();
        
        
// Start Robotinos
        robo.start();
        comView.start();
      
    }
    
    public static void stopServer()
    {
        comView.run = false;
        robo.run = false;
        
        
       
    }
    
     private static class Foo {
 
       // private JTextArea text;
        private InterfacePanel panel;
    
    Foo(InterfacePanel panel) {
        
        this.panel = panel;
         
        }
 
        /**
         * Fügt den String dem Textfeld hinzu
         * 
         * @param str
         */
        void onEvent(String str) {
            panel.logText.append(str + "\n");
            frame.panel.scrollDown();
        }
    }
}

