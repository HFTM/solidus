/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           29.05.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package Programm;

import ComView.FileIO;
import ComView.ComView;
import Refbox.Handler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stecm1
 */
public class ProgrammRobo extends Thread
{
    
    

    private static Handler handler;
    private String name;
    private FileIO station;
    private boolean newStep = false;
    
    private File richtung;
// Gibt dei Koordinaten an die Robos weiter
//    private File send;
// Nimmt das Position erreicht Signal der Robos entgegen
 //   private File empfang;
// Nimmt die Lampenzustände der Stationen entgegen: ort, orange, grün. 0 = aus, 1 = leuchtet, 2 = blinkt
  //  private File lamp;
// Gitb das Station erreicht signal und den Stationswinkel an den Robo
   // private File machinOrder;
    
    private boolean firstExplo;
    private boolean firstProdu;
    private boolean firstPre;
    private boolean firstPost;
    // Der auszufürende Schritt in der Schrittkette
    private int step;
    private int mainStep;
    private int ende;
    public boolean run = true;
    ComView comView;
// Stationsausrichtung
    /**
     *******************************
     *                             *
     *             90°             *
     *              |              *
     *              v              *
     *   0° ---> Station <--- 180° *
     *              A              *
     *              |              * 
     *             270°            *
     *                             *
     *******************************
     */
    private int m1  = 270;
    private int m2  = 0;
    private int m3  = 180;
    private int m4  = 270;
    private int m5  = 270;
    private int m6  = 0;
    private int m7  = 180;
    private int m8  = 180;
    private int m9  = 0;
    private int m10 = 270;
    
    // Lampen Farben der Maschinen in der Explorationsphase
    
    private int[] lampM1;
    private int[] lampM2;
    private int[] lampM3;
    private int[] lampM4;
    private int[] lampM5;
    private int[] lampM6;
    private int[] lampM7;
    private int[] lampM8;
    private int[] lampM9;
    private int[] lampM10;
    
    boolean startblond;

    public ProgrammRobo(Handler handler, String name, ComView comView) throws SocketException
    {
        
        
        this.comView = comView;
        ProgrammRobo.handler = handler;
        this.name = name;
        startblond = false;
        station = new FileIO();

        
        richtung = new File("C:/Robotino/auftrag");
      //  send = new File("C:/Robotino/auftrag");
     //  empfang = new File("C:/Robotino/ready");
     //   machinOrder = new File("C:/Robotino/go");
     //   lamp = new File("C:/Robotino/lampinfo");
        
       


// Setzt die erstbedingung für die Explophase
        firstExplo = false;
        firstProdu = false;
        firstPre = false;
        firstPost = false;
        
        

    }

    @Override
    public void run()
    {
        try
        {
            startRobo();
            
            
            
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ProgrammRobo.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ProgrammRobo.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ProgrammRobo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("empty-statement")
    public void startRobo() throws FileNotFoundException, IOException, InterruptedException
    {

        while (run == true)
        {

// -----------------------------------------------------------------------------
// ------------------------------   PRE_GAME   ---------------------------------
// -----------------------------------------------------------------------------

            while ("PRE_GAME".equals(handler.getPhase()))
            {
                if (firstPre == false)
                {
                    System.out.println("****************** PRE_GAME ******************");
                    firstExplo = false;
                    firstProdu = false;
                    firstPost = false;
                    firstPre = true;

                    step = 1;
                    mainStep = 0;

                    comView.setStation(0);
                    comView.setAllKoor("0 0 0 0");

                }


                Thread.sleep(1000);
                if ("INIT".equals(handler.getState()))
                {
                }

                if ("WAIT_START".equals(handler.getState()))
                {
                }

                if ("RUNNING".equals(handler.getState()))
                {
                }

                if ("PAUSED".equals(handler.getState()))
                {
                }
            }

// -----------------------------------------------------------------------------
// ---------------------------   EXPLORATION   ---------------------------------
// -----------------------------------------------------------------------------            


            while ("EXPLORATION".equals(handler.getPhase()))
            {
                
// ------------------------- PAUSE KONTROLLE -----------------------------------

                while ("PAUSED".equals(handler.getState()))
                {
                    Thread.sleep(1000);

                }
// -----------------------------------------------------------------------------



                if ("RUNNING".equals(handler.gameState))
                {


                    if (firstExplo == false)
                    {
                        System.out.println("****************** EXPLORATION ******************");
                        firstExplo = true;
                        firstProdu = false;
                        firstPost = false;
                        firstPre = false;

                        step = 1;
                        mainStep = 0;


                        comView.setStation(0);
                        comView.setAllKoor("0 0 0 0");


                    }
                    
// ----------------------------   MR.PINK   ------------------------------------
                    
                    if ("MrPink".equals(name))
                    {

                        Thread.sleep(110);
                        int ready = comView.getReady();
                        System.out.println("READY: " + ready);

                        if (ready == 1)
                        {
                            System.out.println("READY OK");
                            
                            System.out.println("ENDE: " + ende);
                            while(ende == 0)
                            {
                                comView.setGo(1);
                                Thread.sleep(110);
                                ende = comView.getEnde();
                                System.out.println("BLUB BLUB BLUB BLUB BLUB BLUB  " + ende);
                                ready = 0;
                                
                                
                            }
                            
                            comView.setGo(0);
                            Thread.sleep(110);
                            
                            step++;
                            System.out.println("************* STEP " + step + " *****************");
                            ende = 0;
                        }
                        

                        switch (mainStep)
                        {
                            case 0:
                                //System.out.println("!!!!! GO TO MACHIE 9 !!!!!");
                                if (m9 == 0)
                                {
                                    Station9_0();
                                }

                                if (m9 == 90)
                                {
                                    Station9_90();
                                }

                                if (m9 == 180)
                                {
                                    Station9_180();
                                }

                                if (m9 == 270)
                                {
                                    Station9_270();
                                }
                                break;

                            case 1:
                                
                                System.out.println("!!!!! GO TO MACHIE 7 !!!!!");
                                 if (m7 == 0)
                                {
                                    Station7_0();
                                }

                                if (m7 == 90)
                                {
                                    Station7_90();
                                }

                                if (m7 == 180)
                                {
                                    Station7_180();
                                }

                                if (m7 == 270)
                                {
                                    Station7_270();
                                }
                                break;
                                
                            case 2:
                                System.out.println("!!!!! GO TO MACHIE 10 !!!!!");
                                 if (m10 == 0)
                                {
                                    Station10_0();
                                }

                                if (m10 == 90)
                                {
                                    Station10_90();
                                }

                                if (m10 == 180)
                                {
                                    Station10_180();
                                }

                                if (m10 == 270)
                                {
                                    Station10_270();
                                }
                                break;

                                
                            default:
                                System.out.println("!!!!! OUT OFF PROGRAM !!!!!");
                                System.out.println("MainStep: " + mainStep + ", Step: " + step);
                                comView.setAllKoor("0 0 0 0");
                                break;
                        }
                    }

// -------------------------------- MR.BLOND -----------------------------------

                     if ("MrBlond".equals(name))
                    {

                        Thread.sleep(110);
                        int ready = comView.getReady();
                        System.out.println("READY: " + ready);

                        if (ready == 1)
                        {
                            System.out.println("READY OK");
                            
                            System.out.println("ENDE: " + ende);
                            while(ende == 0)
                            {
                                comView.setGo(1);
                                Thread.sleep(110);
                                ende = comView.getEnde();
                                System.out.println("BLUB BLUB BLUB BLUB BLUB BLUB  " + ende);
                                ready = 0;
                                
                                
                            }
                            
                            comView.setGo(0);
                            Thread.sleep(110);
                            
                            step++;
                            System.out.println("************* STEP " + step + " *****************");
                            ende = 0;
                        }
                        switch (mainStep)
                        {
                            case 0:
                                
                                System.out.println("!!!!! GO TO MACHIE 5 !!!!!");
                                
                                if (startblond == false)
                                {
                                    Thread.sleep(2000);
                                    startblond = true;
                                }
                                
                                if (m5 == 0)
                                {
                                    Station5_0();
                                }

                                if (m5 == 90)
                                {
                                    Station5_90();
                                }

                                if (m5 == 180)
                                {
                                    Station5_180();
                                }

                                if (m5 == 270)
                                {
                                    Station5_270();
                                }
                                break;

                            case 1:
                                
                                System.out.println("!!!!! GO TO MACHIE 2 !!!!!");
                                 if (m2 == 0)
                                {
                                    Station2_0();
                                }

                                if (m2 == 90)
                                {
                                    Station2_90();
                                }

                                if (m2 == 180)
                                {
                                    Station2_180();
                                }

                                if (m2 == 270)
                                {
                                    Station2_270();
                                }
                                break;
                                
                            case 2:
                                System.out.println("!!!!! GO TO MACHIE 6 !!!!!");
                                 if (m6 == 0)
                                {
                                    Station6_0();
                                }

                                if (m6 == 90)
                                {
                                    Station6_90();
                                }

                                if (m6 == 180)
                                {
                                    Station6_180();
                                }

                                if (m6 == 270)
                                {
                                    Station6_270();
                                }
                                break;

                                
                            default:
                                System.out.println("!!!!! OUT OFF PROGRAM !!!!!");
                                System.out.println("MainStep: " + mainStep + ", Step: " + step);
                                comView.setAllKoor("0 0 0 0");
                                break;
                        }

                    }
// -------------------------------- MR.BROWN -----------------------------------
                     
 if ("MrBrown".equals(name))
                    {

                        Thread.sleep(110);
                        int ready = comView.getReady();
                        System.out.println("READY: " + ready);

                        if (ready == 1)
                        {
                            System.out.println("READY OK");
                            
                            System.out.println("ENDE: " + ende);
                            while(ende == 0)
                            {
                                comView.setGo(1);
                                Thread.sleep(110);
                                ende = comView.getEnde();
                                System.out.println("BLUB BLUB BLUB BLUB BLUB BLUB  " + ende);
                                ready = 0;
                                
                                
                            }
                            
                            comView.setGo(0);
                            Thread.sleep(110);
                            
                            step++;
                            System.out.println("************* STEP " + step + " *****************");
                            ende = 0;
                        }

                        switch (mainStep)
                        {
                            case 0:
                                System.out.println("!!!!! GO TO MACHIE 3 !!!!!");
                                if (m3 == 0)
                                {
                                    Station3_0();
                                }

                                if (m3 == 90)
                                {
                                    Station3_90();
                                }

                                if (m3 == 180)
                                {
                                    Station3_180();
                                }

                                if (m3 == 270)
                                {
                                    Station3_270();
                                }
                                break;

                            case 1:
                                
                                System.out.println("!!!!! GO TO MACHIE 1 !!!!!");
                                 if (m1 == 0)
                                {
                                    Station1_0();
                                }

                                if (m1 == 90)
                                {
                                    Station1_90();
                                }

                                if (m1 == 180)
                                {
                                    Station1_180();
                                }

                                if (m1 == 270)
                                {
                                    Station1_270();
                                }
                                break;
                                
                            case 2:
                                System.out.println("!!!!! GO TO MACHIE 4 !!!!!");
                                 if (m4 == 0)
                                {
                                    Station4_0();
                                }

                                if (m4 == 90)
                                {
                                    Station4_90();
                                }

                                if (m4 == 180)
                                {
                                    Station4_180();
                                }

                                if (m4 == 270)
                                {
                                    Station4_270();
                                }
                                break;
                                
                            case 3:
                                System.out.println("!!!!! GO TO MACHIE 8 !!!!!");
                                if (m8 == 0)
                                {
                                    Station8_0();
                                }

                                if (m8 == 90)
                                {
                                    Station8_90();
                                }

                                if (m8 == 180)
                                {
                                    Station8_180();
                                }

                                if (m8 == 270)
                                {
                                    Station8_270();
                                }
                                break;

                                
                            default:
                                System.out.println("!!!!! OUT OFF PROGRAM !!!!!");
                                System.out.println("MainStep: " + mainStep + ", Step: " + step);
                                comView.setAllKoor("0 0 0 0");
                                break;
                        }

                    }
 
 
 
                }
                if ("INIT".equals(handler.getState()))
                {
                }

                if ("WAIT_START".equals(handler.getState()))
                {
                }


            }

// -----------------------------------------------------------------------------
// ------------------------------   PRODUCTION   -------------------------------
// -----------------------------------------------------------------------------

            while ("PRODUCTION".equals(handler.getPhase()))
            {
                if (firstProdu == false)
                {
                    System.out.println("****************** PRODUCTION ******************");
                    firstExplo = false;
                    firstProdu = true;
                    firstPost = false;
                    firstPre = false;

                    step = 1;
                    mainStep = 0;

                    comView.setStation(0);
                    comView.setAllKoor("0 0 0 0");

                }


                Thread.sleep(1000);
                if ("INIT".equals(handler.getState()))
                {
                }

                if ("WAIT_START".equals(handler.getState()))
                {
                }

                if ("RUNNING".equals(handler.getState()))
                {
                }

                if ("PAUSED".equals(handler.getState()))
                {
                }
            }

// -----------------------------------------------------------------------------
// ------------------------------   POST_GAME   --------------------------------
// -----------------------------------------------------------------------------


            while ("POST_GAME".equals(handler.getPhase()))
            {

                if (firstPost == false)
                {
                    System.out.println("****************** POST_GAME ******************");
                    firstExplo = false;
                    firstProdu = false;
                    firstPost = true;
                    firstPre = false;

                    step = 1;
                    mainStep = 0;

                    comView.setStation(0);
                    comView.setAllKoor("0 0 0 0");

                }


                if ("INIT".equals(handler.getState()))
                {
                }

                if ("WAIT_START".equals(handler.getState()))
                {
                }

                if ("RUNNING".equals(handler.getState()))
                {
                }

                if ("PAUSED".equals(handler.getState()))
                {
                }

            }

        } // wihle(true) END
    }

// ----------------------------------------------------------------------------- 
// -----------   WEGFAHRMETHODEN MR.PINK STATION 7, 9 UND 10   -----------------
// -----------------------------------------------------------------------------
    
    
// ----------------------------------------------------------------------------- 
// ------------------------------ Station 9 ------------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 9 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station9_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 920 560 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 1680 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 9 MIT EINEM WINKEL VON 0 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                comView.setStation(0);
                lampM9 = comView.getLamp();
                handler.lampM9 = lampM9;
                handler.sendMachine("M9");
                
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 9 : " + String.valueOf(lampM9));
                
                break;


            case 5:
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 9 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station9_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 920 560 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 2240 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("1 0 -560 180");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 9 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:
                lampM9 = comView.getLamp();
                handler.lampM9 = lampM9;
                handler.sendMachine("M9");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 560 90");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 9 : " + String.valueOf(lampM9));
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 9 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station9_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 920 560 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 1680 -1120 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 9 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM9 = comView.getLamp();
                handler.lampM9 = lampM9;
                handler.sendMachine("M9");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 -560 180");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 9 : " + String.valueOf(lampM9));
                break;

            case 5:
                System.out.println("---------------------- Step 5 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 9 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station9_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 920 560 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 1120 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 9 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM9 = comView.getLamp();
                handler.lampM9 = lampM9;
                handler.sendMachine("M9");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 9 : " + String.valueOf(lampM9));
                break;


            case 5:
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

// ----------------------------------------------------------------------------- 
// ------------------------------ Station 7 ------------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 7 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station7_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 7 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 7 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM7 = comView.getLamp();
                handler.lampM7 = lampM7;
                 handler.sendMachine("M7");
                

                comView.setStation(0);
                comView.setAllKoor("1 -290 -560 180");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 7 : " + String.valueOf(lampM7));
                break;

            case 4:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 7 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station7_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 7 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 940 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
            case 2:
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;    

            

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 7 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                
                lampM7 = comView.getLamp();
                handler.lampM7 = lampM7;
                handler.sendMachine("M7");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 7 : " + String.valueOf(lampM7));
                break;

            case 5:
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 7 vom Startpun§kt "Mr.Pink" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station7_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 7 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 930 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setAllKoor("1 1120 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                // Step1: nachinrOrder nullen "0  "
                comView.setAllKoor("1 0 -370 180");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 7 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:
                lampM7 = comView.getLamp();
                handler.lampM7 = lampM7;
                handler.sendMachine("M7");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 300 0");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 7 : " + String.valueOf(lampM7));
                break;

            case 6:
                comView.setAllKoor("1 830 260 0");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 7 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station7_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 7 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 7 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM7 = comView.getLamp();
                handler.lampM7 = lampM7;
                handler.sendMachine("M7");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 560 90");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 7 : " + String.valueOf(lampM7));
                break;

            case 4:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

// ----------------------------------------------------------------------------- 
// ------------------------------ Station 10 -----------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 10 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station10_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 10 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 10 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM10 = comView.getLamp();
                handler.lampM10 = lampM10;
                handler.sendMachine("M10");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 0 270");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 10: " + lampM10);
                break;

            case 5:
                comView.setAllKoor("1 3360 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                comView.setAllKoor("1 0 -720 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 10 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station10_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 10 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -930 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 10 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM10 = comView.getLamp();
                handler.lampM10 = lampM10;
                handler.sendMachine("M10");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 10: " + lampM10);
                break;

            case 5:
                comView.setAllKoor("1 3630 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                comView.setAllKoor("1 0 -720 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 10 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station10_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 10 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 10 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM10 = comView.getLamp();
                handler.lampM10 = lampM10;
                handler.sendMachine("M10");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 10: " + lampM10);
                break;

            case 4:
                comView.setAllKoor("1 830 0 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setAllKoor("1 2800 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;


            case 6:
                comView.setAllKoor("1 0 -720 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 10 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station10_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 10 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 10 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM10 = comView.getLamp();
                handler.lampM10 = lampM10;
                handler.sendMachine("M10");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 10: " + lampM10);
                break;

            case 4:
                comView.setAllKoor("1 0 560 180");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setAllKoor("1 2800 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;


            case 6:
                comView.setAllKoor("1 0 -720 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                
                break;

            default:
        };
    }
    
// ----------------------------------------------------------------------------- 
// -----------   WEGFAHRMETHODEN MR.Blond STATION 2, 5 UND 6   -----------------
// -----------------------------------------------------------------------------
    
      
// ----------------------------------------------------------------------------- 
// ------------------------------ Station 5 ------------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 5 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station5_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 5 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 2040 1680 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 5 MIT EINEM WINKEL VON 0 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM5 = comView.getLamp();
                handler.lampM5 = lampM5;
                handler.sendMachine("M5");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 5 : " + lampM5);
                break;


            case 4:
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                System.out.println("---------------------- Step 5 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 5 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station5_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 2040 1680 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 0 560 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 5 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:
                 lampM5 = comView.getLamp();
                handler.lampM5 = lampM5;
                handler.sendMachine("M5");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 560 90");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 5 : " + lampM5);
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 5 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station5_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 5 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 2040 1680 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
            case 3:
                comView.setAllKoor("1 1120 0 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;
                
            case 4:
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 5:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println(">>>> MASCHINE 5 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 6:
                lampM5 = comView.getLamp();
                handler.lampM5 = lampM5;
                handler.sendMachine("M5");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 -560 180");
                System.out.println("---------------------- Step 6 ------------------------");
                System.out.println("STATION 5 : " + lampM5);
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 5 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station5_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 5 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 2040 1680 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
            case 3:
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 5 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:
                lampM5 = comView.getLamp();
                handler.lampM5 = lampM5;
                handler.sendMachine("M5");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 5 : " + lampM5);
                break;


            case 6:
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

// ----------------------------------------------------------------------------- 
// ------------------------------ Station 2 ------------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 2 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station2_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 2 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                 lampM2 = comView.getLamp();
                handler.lampM2 = lampM2;
                handler.sendMachine("M2");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 -560 180");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 2 : " + lampM2);
                break;

            case 4:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 2 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station2_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;    

            

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 2 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                 lampM2 = comView.getLamp();
                handler.lampM2 = lampM2;
                handler.sendMachine("M2");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 2 : " + lampM2);
                break;

            case 5:
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 2 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station2_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;    

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 2 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                 lampM2 = comView.getLamp();
                handler.lampM2 = lampM2;
                handler.sendMachine("M2");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 2: " + lampM2);
                break;
                
             case 5:
                

                 comView.setAllKoor("1 830 0 0");
                 System.out.println("---------------------- Step 5 ------------------------");
                 break;    

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 2 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station2_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 2 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                 lampM2 = comView.getLamp();
                handler.lampM2 = lampM2;
                handler.sendMachine("M2");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 560 90");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 2 : " + lampM2);
                break;

            case 4:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

// ----------------------------------------------------------------------------- 
// ------------------------------ Station 6 -----------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 6 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station6_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 6 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 6 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM6 = comView.getLamp();
                handler.lampM6 = lampM6;
                handler.sendMachine("M6");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 -560 270");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 6: " + lampM6);
                break;

            case 5:
                comView.setAllKoor("1 2240 -1120 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                comView.setAllKoor("1 0 720 920");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 6 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station6_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 6 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -1120 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 6 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM6 = comView.getLamp();
                handler.lampM6 = lampM6;
                handler.sendMachine("M6");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 10: " + lampM6);
                break;

            case 5:
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                comView.setAllKoor("1 1120 0 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;
                
            case 7:
                comView.setAllKoor("1 2240 0 0");
                System.out.println("---------------------- Step 7 ------------------------");
                break;    

            case 8:
                System.out.println("---------------------- Step 8 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 6 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station6_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 6 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 6 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM6 = comView.getLamp();
                handler.lampM6 = lampM6;
                handler.sendMachine("M6");
                
                comView.setStation(0);
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 6: " + lampM6);
                break;

            case 4:
                comView.setAllKoor("1 830 0 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setAllKoor("1 2240 -1120 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;


            case 6:
                comView.setAllKoor("1 0 -720 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 6 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station6_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 6 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 6 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM6 = comView.getLamp();
                handler.lampM6 = lampM6;
                handler.sendMachine("M6");
                
                comView.setStation(0);
                comView.setAllKoor("1 -290 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 6: " + lampM6);
                break;

            case 4:
                comView.setAllKoor("1 0 560 180");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setAllKoor("1 2240 -1120 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;


            case 6:
                comView.setAllKoor("1 0 -720 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                
                break;

            default:
        };
    }
    
/*
 * ******************************************************************************************************************************************
 * ----------------------------------------- MrBrown -------------------------------------------------------------------------------------- -
 * ******************************************************************************************************************************************
 */   
    private void Station3_0()throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 2040 0 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 3:
                lampM3 = comView.getLamp();
                handler.lampM3 = lampM3;
                handler.sendMachine("M3");
                
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

            case 4:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 5:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }
    
    private void Station3_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 2040 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM3 = comView.getLamp();
                handler.lampM3 = lampM3;
                handler.sendMachine("M3");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -290 560 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

                case 5:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;

                
            default:
        };
    }
    
    private void Station3_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 2040 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
            case 3:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 5:
                lampM3 = comView.getLamp();
                handler.lampM3 = lampM3;
                handler.sendMachine("M3");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -290 -560 180");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
                case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station3_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 2040 -280 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                 lampM3 = comView.getLamp();
                handler.lampM3 = lampM3;
                handler.sendMachine("M3");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station1_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 1 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 3:
                lampM1 = comView.getLamp();
                handler.lampM1 = lampM1;
                handler.sendMachine("M1");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -290 -560 180");
                System.out.println("---------------------- Step 3 ------------------------");
                break;
                
           case 4:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station1_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 1 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM1 = comView.getLamp();
                handler.lampM1 = lampM1;
                handler.sendMachine("M1");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station1_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 1 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM1 = comView.getLamp();
                handler.lampM1 = lampM1;
                handler.sendMachine("M1");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
           case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station1_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 1 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 3:
                lampM1 = comView.getLamp();
                handler.lampM1 = lampM1;
                handler.sendMachine("M1");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -290 560 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;
                
            case 4:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    // ********************************************************** Station 4 **********************************************
    
    private void Station4_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM4 = comView.getLamp();
                handler.lampM4 = lampM4;
                handler.sendMachine("M4");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -290 -560 180");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 5:
                System.out.println("---------------------- Step 5 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station4_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -1120 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;
                
            // Lampenerkennung

            case 4:
                 lampM4 = comView.getLamp();
                handler.lampM4 = lampM4;
                handler.sendMachine("M4");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }
    
    private void Station4_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 3:
                lampM4 = comView.getLamp();
                handler.lampM4 = lampM4;
                handler.sendMachine("M4");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

            case 4:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 5:
                System.out.println("---------------------- Step 5 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station4_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 3:
                lampM4 = comView.getLamp();
                handler.lampM4 = lampM4;
                handler.sendMachine("M4");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -290 560 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;
                
            case 4:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    // *********************************************** Station 8 **************************************
    
    private void Station8_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 8 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM8 = comView.getLamp();
                handler.lampM8 = lampM8;
                handler.sendMachine("M8");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -1210 -1120 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 5:
                System.out.println("---------------------- Step 5 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station8_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 8 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -1120 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM8 = comView.getLamp();
                handler.lampM8 = lampM8;
                handler.sendMachine("M8");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1390 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
            case 6:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -720 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;
                
           case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station8_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 8 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 3:
                lampM8 = comView.getLamp();
                handler.lampM8 = lampM8;
                handler.sendMachine("M8");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

            case 4:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1750 560 180");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
           case 5:
                System.out.println("---------------------- Step 5 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    private void Station8_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 8 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 3:
                lampM8 = comView.getLamp();
                handler.lampM8 = lampM8;
                handler.sendMachine("M8");
               
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -850 1480 270");
                System.out.println("---------------------- Step 3 ------------------------");
                break;
                
            case 4:
                System.out.println("---------------------- Step 4 ------------------------");
                mainStep++;
                step = 1;
                break;


            default:
        };
    }
    
    
    // ----------------------------------------------------------------------------- 
// ------------------------   WEGFAHRMETHODEN    -------------------------------
// -----------------------------------------------------------------------------
// ----------------------------------------------------------------------------- 
// ------------------------------ Station 9 ------------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 9 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station9_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 1120 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
            
           case 3:
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 3 ------------------------");
                break;     
// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 9 MIT EINEM WINKEL VON 0 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:
                comView.setStation(0);
                lampM9 = comView.getLamp();
                handler.lampM9 = lampM9;
                handler.sendMachine("M9");

                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 9 : " + String.valueOf(lampM9));

                break;


            case 6:
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 6 ------------------------");
                break;
                
            case 7:
                comView.setStation(0);
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 7 ------------------------");
                break;    

            case 8:
                System.out.println("---------------------- Step 8 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 9 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station9_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 9 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:
                lampM9 = comView.getLamp();
                handler.lampM9 = lampM9;
                handler.sendMachine("M9");

                comView.setStation(0);
                comView.setAllKoor("1 -100 560 90");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 9 : " + String.valueOf(lampM9));
                break;
                
            case 6:
                comView.setAllKoor("1 0 190 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;   
                
            case 7:
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 7 ------------------------");
                break;      

            case 8:
                System.out.println("---------------------- Step 8 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 9 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station9_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 9 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM9 = comView.getLamp();
                handler.lampM9 = lampM9;
                handler.sendMachine("M9");

                comView.setStation(0);
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 9 : " + String.valueOf(lampM9));
                break;
                
            case 5:
                comView.setAllKoor("1 0 -190 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
            case 6:
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;    

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 9 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station9_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0  ");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 9 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM9 = comView.getLamp();
                handler.lampM9 = lampM9;
                handler.sendMachine("M9");

                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 9 : " + String.valueOf(lampM9));
                break;


            case 5:
                comView.setAllKoor("1 830 0 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

// ----------------------------------------------------------------------------- 
// ------------------------------ Station 7 ------------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 7 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station7_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 7 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;    

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 7 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM7 = comView.getLamp();
                handler.lampM7 = lampM7;
                handler.sendMachine("M7");


                comView.setStation(0);
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 7 : " + String.valueOf(lampM7));
                break;
                
            case 5:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -190 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;    

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 7 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station7_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 7 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 940 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
            case 3:
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 3 ------------------------");
                break;    



// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 7 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:

                lampM7 = comView.getLamp();
                handler.lampM7 = lampM7;
                handler.sendMachine("M7");

                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 7 : " + String.valueOf(lampM7));
                break;

            case 6:
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 7 vom Startpun§kt "Mr.Pink" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station7_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 7 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setAllKoor("1 1120 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                // Step1: nachinrOrder nullen "0  "
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 7 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:
                lampM7 = comView.getLamp();
                handler.lampM7 = lampM7;
                handler.sendMachine("M7");

                comView.setStation(0);
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 7 : " + String.valueOf(lampM7));
                break;

            case 6:
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 7 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station7_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 7 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 7 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM7 = comView.getLamp();
                handler.lampM7 = lampM7;
                handler.sendMachine("M7");

                comView.setStation(0);
                comView.setAllKoor("1 -100 560 90");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 7 : " + String.valueOf(lampM7));
                break;
                
            case 4:
                comView.setAllKoor("1 0 190 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;    

            case 5:
                System.out.println("---------------------- Step 5 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

// ----------------------------------------------------------------------------- 
// ------------------------------ Station 10 -----------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 10 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station10_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 10 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 10 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM10 = comView.getLamp();
                handler.lampM10 = lampM10;
                handler.sendMachine("M10");

                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 10: " + lampM10);
                break;

            case 5:
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
            case 6:
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 6 ------------------------");
                break;    

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 10 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station10_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 10 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -930 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 10 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM10 = comView.getLamp();
                handler.lampM10 = lampM10;
                handler.sendMachine("M10");

                comView.setStation(0);
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 10: " + lampM10);
                break;

            case 5:
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 10 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station10_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 10 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 10 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM10 = comView.getLamp();
                handler.lampM10 = lampM10;
                handler.sendMachine("M10");

                comView.setStation(0);
                comView.setAllKoor("1 -10 560 90");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 10: " + lampM10);
                break;

            case 4:
                comView.setAllKoor("1 0 280 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 10 vom Startpunkt "Mr.Pink" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station10_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 10 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 10 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM10 = comView.getLamp();
                handler.lampM10 = lampM10;
                handler.sendMachine("M10");

                comView.setStation(0);
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 10: " + lampM10);
                break;

            case 4:
                comView.setAllKoor("1 0 280 270");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;

                break;

            default:
        };
    }

// ----------------------------------------------------------------------------- 
// -------------------------   WEGFAHRMETHODEN   -------------------------------
// -----------------------------------------------------------------------------
// ----------------------------------------------------------------------------- 
// ------------------------------ Station 5 ------------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 5 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station5_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 5 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 5 MIT EINEM WINKEL VON 0 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM5 = comView.getLamp();
                handler.lampM5 = lampM5;
                handler.sendMachine("M5");

                comView.setStation(0);
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 5 : " + lampM5);
                break;


            case 4:
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;    

            case 5:
                System.out.println("---------------------- Step 5 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 5 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station5_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 9 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 5 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM5 = comView.getLamp();
                handler.lampM5 = lampM5;
                handler.sendMachine("M5");

                comView.setStation(0);
                comView.setAllKoor("1 -100 560 90");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 5 : " + lampM5);
                break;
                
            case 4:
                comView.setAllKoor("1 0 190 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break; 
                
            case 5:
                comView.setAllKoor("1 0 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;     

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 5 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station5_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 5 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 5 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM5 = comView.getLamp();
                handler.lampM5 = lampM5;
                handler.sendMachine("M5");

                comView.setStation(0);
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 5 : " + lampM5);
                break;
                
            case 5:
                comView.setAllKoor("1 0 -190 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;    

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 5 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station5_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 5 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 5 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM5 = comView.getLamp();
                handler.lampM5 = lampM5;
                handler.sendMachine("M5");

                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 5 : " + lampM5);
                break;


            case 5:
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

// ----------------------------------------------------------------------------- 
// ------------------------------ Station 2 ------------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 2 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station2_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 2 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM2 = comView.getLamp();
                handler.lampM2 = lampM2;
                handler.sendMachine("M2");

                comView.setStation(0);
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 2 : " + lampM2);
                break;
                
            case 4:
                comView.setAllKoor("1 0 -190 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;    
                
            case 5:
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;                    

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 2 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station2_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;



// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 2 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM2 = comView.getLamp();
                handler.lampM2 = lampM2;
                handler.sendMachine("M2");

                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 2 : " + lampM2);
                break;

            case 5:
                comView.setAllKoor("1 830 0 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 2 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station2_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 2 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM2 = comView.getLamp();
                handler.lampM2 = lampM2;
                handler.sendMachine("M2");

                comView.setStation(0);
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 2: " + lampM2);
                break;

            case 5:
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
            case 6:
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 6 ------------------------");
                break;    

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 2 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station2_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

// -----   Vor der Maschine   -----
            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                System.out.println(">>>> MASCHINE 2 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 3:
                lampM2 = comView.getLamp();
                handler.lampM2 = lampM2;
                handler.sendMachine("M2");

                comView.setStation(0);
                comView.setAllKoor("1 -100 560 90");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println("STATION 2 : " + lampM2);
                break;
                
            case 4:
                comView.setAllKoor("1 0 190 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 5:
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;    

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

// ----------------------------------------------------------------------------- 
// ------------------------------ Station 6 -----------------------------------
// -----------------------------------------------------------------------------
    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 6 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 0° (Aus der Sicht der Maschine) an.
     */
    private void Station6_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 6 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
            case 3:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 3 ------------------------");
                break;     

// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 6 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:
                lampM6 = comView.getLamp();
                handler.lampM6 = lampM6;
                handler.sendMachine("M6");

                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 6: " + lampM6);
                break;

            case 6:
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 6 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 90° (Aus der Sicht der Maschine) an.
     */
    private void Station6_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 6 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
            case 3:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;    

// -----   Vor der Maschine   -----
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println(">>>> MASCHINE 6 MIT EINEM WINKEL VON 90 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 5:
                lampM6 = comView.getLamp();
                handler.lampM6 = lampM6;
                handler.sendMachine("M6");

                comView.setStation(0);
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 5 ------------------------");
                System.out.println("STATION 10: " + lampM6);
                break;

            case 6:
                comView.setAllKoor("1 830 0 90");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            case 7:
                System.out.println("---------------------- Step 7 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 6 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 180° (Aus der Sicht der Maschine) an.
     */
    private void Station6_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 6 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
            case 2:
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;    

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 6 MIT EINEM WINKEL VON 180 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM6 = comView.getLamp();
                handler.lampM6 = lampM6;
                handler.sendMachine("M6");

                comView.setStation(0);
                comView.setAllKoor("1 -100 560 90");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 6: " + lampM6);
                break;

            case 5:
                comView.setAllKoor("1 0 190 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;
                break;

            default:
        };
    }

    /**
     *
     * @param step
     * @throws FileNotFoundException
     * @throws IOException Fährt die Station 6 vom Startpunkt "Mr.Blond" mit
     * einem Winkel von 270° (Aus der Sicht der Maschine) an.
     */
    private void Station6_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 6 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
            case 2:
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;    

// -----   Vor der Maschine   -----
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                System.out.println(">>>> MASCHINE 6 MIT EINEM WINKEL VON 270 ERREICHT <<<<<");
                // vor STation Step: machuneOrder eins stellen "1  "
                comView.setStation(1);
                break;
// -----   Wegfahren   -----
            case 4:
                lampM6 = comView.getLamp();
                handler.lampM6 = lampM6;
                handler.sendMachine("M6");

                comView.setStation(0);
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 4 ------------------------");
                System.out.println("STATION 6: " + lampM6);
                break;

            case 5:
                comView.setAllKoor("1 0 -190 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                System.out.println("---------------------- Step 6 ------------------------");
                mainStep++;
                step = 1;

                break;

            default:
        };
    }

    /*
     * ******************************************************************************************************************************************
     * -----------------------------------------------------------------------------------------------------------------------------------------
     * ******************************************************************************************************************************************
     */
    private void Station3_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
            case 2:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;    
                
            case 3:
                // Step1: nachinrOrder nullen "0  "
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;    

            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 5:
                lampM3 = comView.getLamp();
                handler.lampM3 = lampM3;
                handler.sendMachine("M3");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            default:
        };
    }

    private void Station3_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -560 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM3 = comView.getLamp();
                handler.lampM3 = lampM3;
                handler.sendMachine("M3");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -100 560 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 5:
                comView.setStation(0);
                comView.setAllKoor("1 0 190 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;    

            default:
        };
    }

    private void Station3_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM3 = comView.getLamp();
                handler.lampM3 = lampM3;
                handler.sendMachine("M3");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 5:
                comView.setStation(0);
                comView.setAllKoor("1 0 -190 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;    

            default:
        };
    }

    private void Station3_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -930 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 270");
                System.out.println("---------------------- Step 3 ------------------------");
                break;    
                
            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 5:
                lampM3 = comView.getLamp();
                handler.lampM3 = lampM3;
                handler.sendMachine("M3");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            default:
        };
    }

    private void Station1_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 1 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 3:
                lampM1 = comView.getLamp();
                handler.lampM1 = lampM1;
                handler.sendMachine("M1");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 3 ------------------------");
                break;
                
            case 4:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -190 270");
                System.out.println("---------------------- Step 4 ------------------------");
                break;    

           case 5:
                comView.setStation(0);
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 5 ------------------------");
                break;     
                
            default:
        };
    }

    private void Station1_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 1 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 2700");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM1 = comView.getLamp();
                handler.lampM1 = lampM1;
                handler.sendMachine("M1");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            default:
        };
    }

    private void Station1_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 1 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM1 = comView.getLamp();
                handler.lampM1 = lampM1;
                handler.sendMachine("M1");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
            case 6:
                comView.setStation(0);
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 6 ------------------------");
                break;     

            default:
        };
    }

    private void Station1_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 1 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 3:
                lampM1 = comView.getLamp();
                handler.lampM1 = lampM1;
                handler.sendMachine("M1");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -100 560 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;
                
           case 4:
                comView.setStation(0);
                comView.setAllKoor("1 0 190 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
               
           case 5:
                comView.setStation(0);
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;     

            default:
        };
    }
    // ********************************************************** Station 4 **********************************************

    private void Station4_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
           case 3:
                comView.setStation(0);
                comView.setAllKoor("1 560 0 270");
                System.out.println("---------------------- Step 3 ------------------------");
                break;      

            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 5:
                lampM4 = comView.getLamp();
                handler.lampM4 = lampM4;
                handler.sendMachine("M4");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;
                
            case 6:
                comView.setStation(0);
                comView.setAllKoor("1 0 -190 0");
                System.out.println("---------------------- Step 6 ------------------------");
                break;     

            default:
        };
    }

    private void Station4_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;


            // Lampenerkennung

            case 3:
                lampM4 = comView.getLamp();
                handler.lampM4 = lampM4;
                handler.sendMachine("M4");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 3 ------------------------");
                break;

            case 4:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -190 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
           case 5:
                comView.setStation(0);
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;      

            default:
        };
    }

    private void Station4_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM4 = comView.getLamp();
                handler.lampM4 = lampM4;
                handler.sendMachine("M4");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            default:
        };
    }

    private void Station4_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
           case 2:
                comView.setStation(0);
                comView.setAllKoor("1 560 0 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;      

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM4 = comView.getLamp();
                handler.lampM4 = lampM4;
                handler.sendMachine("M4");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -100 560 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 5:
                comView.setStation(0);
                comView.setAllKoor("1 0 190 90");
                System.out.println("---------------------- Step 5 ------------------------");
                break;     

            default:
        };
    }
    // *********************************************** Station 8 **************************************

    private void Station8_0_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 8 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
             case 3:
                comView.setStation(0);
                comView.setAllKoor("1 0 0 270");
                System.out.println("---------------------- Step 3 ------------------------");
                break;   

            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 5:
                lampM8 = comView.getLamp();
                handler.lampM8 = lampM8;
                handler.sendMachine("M8");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 6:
                comView.setStation(0);
                comView.setAllKoor("1 830 0 270");
                System.out.println("---------------------- Step 6 ------------------------");
                break;    

            default:
        };
    }

    private void Station8_90_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 8 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
            case 3:
                comView.setStation(0);
                comView.setAllKoor("1 560 0 90");
                System.out.println("---------------------- Step 3 ------------------------");
                break;    

            case 4:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 4 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 5:
                lampM8 = comView.getLamp();
                handler.lampM8 = lampM8;
                handler.sendMachine("M8");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 560 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            case 6:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 830 0 0");
                System.out.println("---------------------- Step 6 ------------------------");
                break;

            default:
        };
    }

    private void Station8_180_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 8 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
            case 2:
                comView.setStation(0);
                comView.setAllKoor("1 0 -560 0");
                System.out.println("---------------------- Step 2 ------------------------");
                break;    

            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;

            // Lampenerkennung

            case 4:
                lampM8 = comView.getLamp();
                handler.lampM8 = lampM8;
                handler.sendMachine("M8");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -100 560 90");
                System.out.println("---------------------- Step 4 ------------------------");
                break;

            case 5:
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 190 0");
                System.out.println("---------------------- Step 5 ------------------------");
                break;

            default:
        };
    }

    private void Station8_270_P() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Station 8 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -560 0 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;
                
            case 2:
                comView.setStation(0);
                comView.setAllKoor("1 0 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                break;
                
            case 3:
                comView.setAllKoor("0 0 0 0");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                comView.setStation(1);
                break;    

            // Lampenerkennung

            case 4:
                lampM8 = comView.getLamp();
                handler.lampM8 = lampM8;
                handler.sendMachine("M8");

                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -100 -560 270");
                System.out.println("---------------------- Step 4 ------------------------");
                break;
                
            case 5:
                comView.setStation(0);
                comView.setAllKoor("1 0 -190 270");
                System.out.println("---------------------- Step 5 ------------------------");
                break;    

            default:
        };
    }

    private void ToPointP1() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 1 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            default:
        }

    }

    private void ToPointP2_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 0 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            default:
        }
    }

    private void ToPointP2_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 2 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -1120 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            default:
        }
    }

    private void ToPointP3_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setStation(0);
                comView.setAllKoor("0 1120 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "

                break;

            default:
        }
    }

    private void ToPointP3_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 3 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 -2240 270");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            default:
        }
    }

    private void ToPointP4_0() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            default:
        }
    }

    private void ToPointP4_90() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setStation(0);
                comView.setAllKoor("0 0 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                break;

            default:
        }
    }

    private void ToPointP4_180() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -1120 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setStation(0);
                comView.setAllKoor("0 0 0 270");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                break;

            default:
        }
    }

    private void ToPointP4_270() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 0 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setStation(0);
                comView.setAllKoor("0 0 2240 90");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                break;

            case 3:
                comView.setStation(0);
                comView.setAllKoor("0 0 0 270");
                System.out.println("---------------------- Step 3 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                break;

            default:
        }
    }

    private void ToPointP5() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Punkt 4 ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 0 1120 90");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            case 2:
                comView.setStation(0);
                comView.setAllKoor("0 0 0 90");
                System.out.println("---------------------- Step 2 ------------------------");
                // vor STation Step: machineOrder eins stellen "1  "
                break;

            default:
        }
    }

    private void ToDeliveryPoint() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- Delivery Point ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 1120 560 0");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            default:
        }
    }

    private void ToPuckInput() throws FileNotFoundException, IOException, InterruptedException
    {
        switch (step)
        {

// ----------------------------------- PuckInput ------------------------------------------
            case 1:
                // Step1: nachinrOrder nullen "0  "
                comView.setStation(0);
                // "fahrbestätigung, x(gerade aus), y(traversieren), phi(90links, 270rechts)" alles relativ
                comView.setAllKoor("1 -1120 -560 180");
                System.out.println("---------------------- Step 1 ------------------------");
                break;

            default:
        }
    }
         
}
