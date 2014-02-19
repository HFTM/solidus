/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           29.05.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package Refbox;

import Interface.UserFrame;
import MainPack.Main;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_comm.ProtobufMessage;
import org.robocup_logistics.llsf_comm.ProtobufMessageHandler;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos.BeaconSignal;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationInfo;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationSignal;
import org.robocup_logistics.llsf_msgs.GameStateProtos;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState.Phase;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState.State;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.LightSpec;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.LightState;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.Machine;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.MachineInfo;
import org.robocup_logistics.llsf_msgs.MachineReportProtos;
import org.robocup_logistics.llsf_msgs.MachineReportProtos.MachineReport;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.Order;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.OrderInfo;
import org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D;
import org.robocup_logistics.llsf_msgs.PuckInfoProtos;
import org.robocup_logistics.llsf_msgs.PuckInfoProtos.PuckInfo;
import org.robocup_logistics.llsf_msgs.PuckInfoProtos.PuckState;
import org.robocup_logistics.llsf_msgs.TimeProtos;
import org.robocup_logistics.llsf_tools.NanoSecondsTimestampProvider;

/**
 *
 * @author stecm1
 */
public class Handler extends Observable implements ProtobufMessageHandler
{

    int counter;
    static UserFrame frame;
// -------------------------- Game State ---------------------------------------
    public String gamePoints;           // Akutueller Punktestand
    public String gamePhase;            // Aktuelle Game Phase (PRE_GAME, EXPLORATION, PRODUCTION, POST_GAME)
    public String gameState;            // Aktueller Game Status (WAIT_STRT, RUNNING, PAUSED)
    public String hasTime;              // Ist noch zeit zur Verfügung
    public String gameTime;             // Aktuelle Spielzeit (EXPLORATION: 0-180, PRODUCTION: 0-900)
// ------------------------- BeaconInfo ----------------------------------------
    public int[] mTyp;                  // Lampen Informationen für Maschinentypen
// ---------------------------- Verbindung -------------------------------------
    ProtobufBroadcastPeer peer;
    String[] send = new String[5];
// ---------------------------- MachineReportt ---------------------------------
    public int[] lampM1;
    public int[] lampM2;
    public int[] lampM3;
    public int[] lampM4;
    public int[] lampM5;
    public int[] lampM6;
    public int[] lampM7;
    public int[] lampM8;
    public int[] lampM9;
    public int[] lampM10;
    
    public String[] Mtyp;
    
    private String machinetyp;     // Maschinentyp für die Produktionsphase
   

    public Handler(UserFrame frame, ProtobufBroadcastPeer peer)
    {

        this.peer = peer;
        this.addObserver(frame.panel);
        this.frame = frame;
        counter = 0;
        frame.panel.printLog("Der Server wurde gestartet !!!");
    }

    @Override
    public void handle_message(ByteBuffer in_msg, GeneratedMessage msg)
    {
        NanoSecondsTimestampProvider nstp = new NanoSecondsTimestampProvider();

        long ms = System.currentTimeMillis();
        long ns = nstp.currentNanoSecondsTimestamp();

        int sec = (int) (ms / 1000);
        long nsec = ns - (ms * 1000000L);

        TimeProtos.Time t = TimeProtos.Time.newBuilder().setSec(sec).setNsec(nsec).build();
        BeaconSignal bs = BeaconSignal.newBuilder().setTime(t).setSeq(1).setPeerName(Main.name).setNumber(0).setTeamName("Solidus").build();
        
        ProtobufMessage udpOut = new ProtobufMessage(2000, 1, bs);
        peer.enqueue(udpOut);
        //sendMachine("M1", "T4");
// -------------------------- Puck Info ----------------------------------------

        if (msg instanceof PuckInfo)
        {


            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);

            PuckInfoProtos.PuckInfo info;

            try
            {
                info = PuckInfoProtos.PuckInfo.parseFrom(array);
                int count = info.getPucksCount();
                System.out.println("Number of pucks: " + count);
                List<PuckInfoProtos.Puck> pucks = info.getPucksList();
                for (int i = 0; i < pucks.size(); i++)
                {
                    PuckInfoProtos.Puck puck = pucks.get(i);
                    int id = puck.getId();
                    System.out.println("  puck ID: " + id);
                }

            }
            catch (InvalidProtocolBufferException e)
            {
                e.printStackTrace();
            }
        }

// -------------------------------- Order Info ---------------------------------


        if (msg instanceof OrderInfo)
        {


            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);
            
            OrderInfoProtos.OrderInfo info;
            try
            {
                info = OrderInfoProtos.OrderInfo.parseFrom(array);
                
                List<Order> list = info.getOrdersList();
                int length = list.size();
                
                for (int i = 0; i < length; i++)
                {
                    System.out.println("Order " + (i+1) + ": " + list.get(i));
                    
                }
            }
            catch (InvalidProtocolBufferException ex)
            {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

//------------------------------------------------------------------------------------------------                
// -------------------------------------   Game State   ------------------------------------------
//------------------------------------------------------------------------------------------------
// Gibt die Aktuelle Spielphase, Spielstatus, Spielzeit, die Punkte und ob noch Zeit vorhanden ist.

        if (msg instanceof GameState)
        {


            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);

            GameStateProtos.GameState info;

            try
            {
                info = GameStateProtos.GameState.parseFrom(array);
                int points = info.getPoints();
                Phase phase = info.getPhase();
                State state = info.getState();
                TimeProtos.Time time = info.getGameTime();

                hasTime = info.hasGameTime() + "";


                setChanged();
                notifyObservers(send);


                gamePoints = points + "";
                gamePhase = phase.name();
                gameState = state.name();
                gameTime = time.getSec() + "";


            }
            catch (InvalidProtocolBufferException e)
            {
                e.printStackTrace();
            }


        }

// -------------------------------- Machine Info ---------------------------------
                
         if (msg instanceof MachineInfo)
         {
             System.out.println("MACHINE INFO");
            
                byte[] array = new byte[in_msg.capacity()];
                in_msg.rewind();
                in_msg.get(array);
                               
                MachineInfoProtos.MachineInfo info;
            try
            {                
                
                
                info = MachineInfoProtos.MachineInfo.parseFrom(array);
                List<Machine> list = info.getMachinesList();
                
                int length = list.size();
                Mtyp = new String[length];
               
                
                for ( int i = 0; i < length; i++)
                {
                    Machine m = list.get(i);
                    
                   String name =  m.getName();
                   String typ =  m.getType();
                   Pose2D pose = m.getPose();
                   
                   PuckState out = m.getOutput();
                   //PuckState in = m.getInputs(i);
                   
                   Mtyp[i] = typ;
                    System.out.println("Maschine " + name + " ist Maschinentyp " +Mtyp[i]);
                    
                    if (Mtyp.equals("T5"))
                    {
                        machinetyp = Mtyp[i];
                    }
                }
                
            }
            catch (InvalidProtocolBufferException ex)
            {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }
                
               
            /*    
               try
            { 
             
                
                int count = info.getMachinesCount();
                System.out.println("Anzahl Mashcinen: " + count);
                List<MachineInfoProtos.Machine> machines = info.getMachinesList();
                                      
             
                MachineInfoProtos.Machine machine = machines.get(0);
            
                int inputsCount = machine.getInputsCount();
                List<LightSpec> lightlist = machine.getLightsList();
                
                
            }
            catch (InvalidProtocolBufferException ex)
            {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            }
        */
	
			                      
         }
         
        
// -----------------------------------------------------------------------------------
// -------------------------------- Exploration Info ---------------------------------
// -----------------------------------------------------------------------------------

        if (msg instanceof ExplorationInfo)
        {
            

            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);

            ExplorationInfoProtos.ExplorationInfo info;


            try
            {
                info = ExplorationInfoProtos.ExplorationInfo.parseFrom(array);

                List<ExplorationSignal> slist = info.getSignalsList();
                ExplorationSignal m1 = slist.get(0);
                ExplorationSignal m2 = slist.get(1);
                ExplorationSignal m3 = slist.get(2);
                ExplorationSignal m4 = slist.get(3);
                ExplorationSignal m5 = slist.get(4);

// -------------------  Maschinen - Typ 1 --------------------------------------

                String m1typ = m1.getType();
                LightSpec m1l1 = m1.getLights(0);
                LightSpec m1l2 = m1.getLights(1);
                LightSpec m1l3 = m1.getLights(2);

                LightState m1l1state = m1l1.getState();
                LightState m1l2state = m1l2.getState();
                LightState m1l3state = m1l3.getState();

                int lampred1 = m1l1state.getNumber();
                int lampornge1 = m1l2state.getNumber();
                int lampgreen1 = m1l3state.getNumber();


// -------------------  Maschinen - Typ 2 --------------------------------------

                String m2typ = m2.getType();
                LightSpec m2l1 = m2.getLights(0);
                LightSpec m2l2 = m2.getLights(1);
                LightSpec m2l3 = m2.getLights(2);

                LightState m2l1state = m2l1.getState();
                LightState m2l2state = m2l2.getState();
                LightState m2l3state = m2l3.getState();

                int lampred2 = m2l1state.getNumber();
                int lampornge2 = m2l2state.getNumber();
                int lampgreen2 = m2l3state.getNumber();



// -------------------  Maschinen - Typ 3 --------------------------------------

                String m3typ = m3.getType();
                LightSpec m3l1 = m3.getLights(0);
                LightSpec m3l2 = m3.getLights(1);
                LightSpec m3l3 = m3.getLights(2);

                LightState m3l1state = m3l1.getState();
                LightState m3l2state = m3l2.getState();
                LightState m3l3state = m3l3.getState();

                int lampred3 = m3l1state.getNumber();
                int lampornge3 = m3l2state.getNumber();
                int lampgreen3 = m3l3state.getNumber();


// -------------------  Maschinen - Typ 4 --------------------------------------

                String m4typ = m4.getType();
                LightSpec m4l1 = m4.getLights(0);
                LightSpec m4l2 = m4.getLights(1);
                LightSpec m4l3 = m4.getLights(2);

                LightState m4l1state = m4l1.getState();
                LightState m4l2state = m4l2.getState();
                LightState m4l3state = m4l3.getState();

                int lampred4 = m4l1state.getNumber();
                int lampornge4 = m4l2state.getNumber();
                int lampgreen4 = m4l3state.getNumber();

// -------------------  Maschinen - Typ 5 --------------------------------------

                String m5typ = m5.getType();
                LightSpec m5l1 = m5.getLights(0);
                LightSpec m5l2 = m5.getLights(1);
                LightSpec m5l3 = m5.getLights(2);

                LightState m5l1state = m5l1.getState();
                LightState m5l2state = m5l2.getState();
                LightState m5l3state = m5l3.getState();

                int lampred5 = m5l1state.getNumber();
                int lampornge5 = m5l2state.getNumber();
                int lampgreen5 = m5l3state.getNumber();

// ------------------ Array[15] füllen: Station 1 - 3 platz 0 - 2... -------------
                mTyp = new int[15];
                mTyp[0] = lampred1;
                mTyp[1] = lampornge1;
                mTyp[2] = lampgreen1;
                mTyp[3] = lampred2;
                mTyp[4] = lampornge2;
                mTyp[5] = lampgreen2;
                mTyp[6] = lampred3;
                mTyp[7] = lampornge3;
                mTyp[8] = lampgreen3;
                mTyp[9] = lampred4;
                mTyp[10] = lampornge4;
                mTyp[11] = lampgreen4;
                mTyp[12] = lampred5;
                mTyp[13] = lampornge5;
                mTyp[14] = lampgreen5;

                int i;
                int m = 1;
                /*
                for (i = 0; i < 15; i = i + 3)
                {
                    frame.panel.printLog("Machinetyp " + m + " => RED: " + mTyp[i] + " ORANGE: " + mTyp[i + 1] + " GREEN: " + mTyp[i + 2]);
                    m++;
                }
                */
            }
            catch (InvalidProtocolBufferException e)
            {
                e.printStackTrace();
            }
        }

// -------------------------------- Beacon Signal ---------------------------------

        if (msg instanceof BeaconSignal)
        {

            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);



        }

// -------------------------------- Version Info ---------------------------------
       
        
 // ---------------------------- MachineReportt ---------------------------------

                   
        /*
        MachineReportProtos.MachineReportEntry mi = MachineReportProtos.MachineReportEntry.newBuilder().setName("???").setType("???").build();
        
        MachineReport mr = MachineReport.newBuilder().setMachines(1, mi).build();

        ProtobufMessage machineinfo = new ProtobufMessage(2000, 60, mr);
        peer.equals(machineinfo);

*/

// -----------------------------------------------------------------------------
        frame.panel.printLog("Step -------------------------> " + counter + " <-------------------------");
        counter++;

        send[0] = gamePoints;
        send[1] = gamePhase;
        send[2] = gameState;
        send[3] = gameTime;
        send[4] = hasTime;




        setChanged();
        notifyObservers(send);
    }
    
    
   

    /**
     *
     * @return Gibt den aktuellen Spiel Status zurück (WAIT_STRT, RUNNING,
     * PAUSED).
     */
    public String getState()
    {
        return gameState;
    }

    /**
     *
     * @return Gibt die aktuelle Spiel Phase zurück (PRE_GAME, EXPLORATION,
     * PRODUCTION, POST_GAME).
     */
    public String getPhase()
    {
        return gamePhase;
    }

    /**
     *
     * @return Git an ob noch Spielzeit vorhanden ist.
     */
    public String getHasTime()
    {
        return hasTime;
    }

    /**
     *
     * @return Gibt die aktuelle Spielzeit zurück (EXPLORATION: 0-180,
     * PRODUCTION: 0-900).
     */
    public String getTime()
    {
        return gameTime;
    }

    /**
     *
     * @return Gibt den Aktuellen Punktestand zurück.
     */
    public String getPoints()
    {
        return gamePoints;
    }

    /**
     *
     * @return Gibt die von edr Refbox zugewisenen Lichter der 5 Maschinentypen
     * zurück: array[15] array[0] - array[2] Maschinentyp 1: [0] = Rote Lampe,
     * [1] = Orange Lampe, [2] = Grüne Lampe...
     */
    public int[] getMachineTyp()
    {
        return mTyp;
    }
    
    public void sendMachine(String name)
    {
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String type = "";
        
        switch (name)
        {
            case "M1":
                if (lampM1[0] == mTyp[0] && lampM1[1] == mTyp[1] && lampM1[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM1[0] == mTyp[3] && lampM1[1] == mTyp[4] && lampM1[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM1[0] == mTyp[6] && lampM1[1] == mTyp[7] && lampM1[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM1[0] == mTyp[9] && lampM1[1] == mTyp[10] && lampM1[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM1[0] == mTyp[12] && lampM1[1] == mTyp[13] && lampM1[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            case "M2":
                if (lampM2[0] == mTyp[0] && lampM2[1] == mTyp[1] && lampM2[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM2[0] == mTyp[3] && lampM2[1] == mTyp[4] && lampM2[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM2[0] == mTyp[6] && lampM2[1] == mTyp[7] && lampM2[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM2[0] == mTyp[9] && lampM2[1] == mTyp[10] && lampM2[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM2[0] == mTyp[12] && lampM2[1] == mTyp[13] && lampM2[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            case "M3":
                 if (lampM3[0] == mTyp[0] && lampM3[1] == mTyp[1] && lampM3[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM3[0] == mTyp[3] && lampM3[1] == mTyp[4] && lampM3[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM3[0] == mTyp[6] && lampM3[1] == mTyp[7] && lampM3[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM3[0] == mTyp[9] && lampM3[1] == mTyp[10] && lampM3[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM3[0] == mTyp[12] && lampM3[1] == mTyp[13] && lampM3[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            case "M4":
                 if (lampM4[0] == mTyp[0] && lampM4[1] == mTyp[1] && lampM4[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM4[0] == mTyp[3] && lampM4[1] == mTyp[4] && lampM4[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM4[0] == mTyp[6] && lampM4[1] == mTyp[7] && lampM4[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM4[0] == mTyp[9] && lampM4[1] == mTyp[10] && lampM4[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM4[0] == mTyp[12] && lampM4[1] == mTyp[13] && lampM4[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            case "M5":
                 if (lampM5[0] == mTyp[0] && lampM5[1] == mTyp[1] && lampM5[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM5[0] == mTyp[3] && lampM5[1] == mTyp[4] && lampM5[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM5[0] == mTyp[6] && lampM5[1] == mTyp[7] && lampM5[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM5[0] == mTyp[9] && lampM5[1] == mTyp[10] && lampM5[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM5[0] == mTyp[12] && lampM5[1] == mTyp[13] && lampM5[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            case "M6":
                 if (lampM6[0] == mTyp[0] && lampM6[1] == mTyp[1] && lampM6[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM6[0] == mTyp[3] && lampM6[1] == mTyp[4] && lampM6[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM6[0] == mTyp[6] && lampM6[1] == mTyp[7] && lampM6[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM6[0] == mTyp[9] && lampM6[1] == mTyp[10] && lampM6[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM6[0] == mTyp[12] && lampM6[1] == mTyp[13] && lampM6[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            case "M7":
                 if (lampM7[0] == mTyp[0] && lampM7[1] == mTyp[1] && lampM7[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM7[0] == mTyp[3] && lampM7[1] == mTyp[4] && lampM7[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM7[0] == mTyp[6] && lampM7[1] == mTyp[7] && lampM7[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM7[0] == mTyp[9] && lampM7[1] == mTyp[10] && lampM7[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM7[0] == mTyp[12] && lampM7[1] == mTyp[13] && lampM7[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            case "M8":
                 if (lampM8[0] == mTyp[0] && lampM8[1] == mTyp[1] && lampM8[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM8[0] == mTyp[3] && lampM8[1] == mTyp[4] && lampM8[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM8[0] == mTyp[6] && lampM8[1] == mTyp[7] && lampM8[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM8[0] == mTyp[9] && lampM8[1] == mTyp[10] && lampM8[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM8[0] == mTyp[12] && lampM8[1] == mTyp[13] && lampM8[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            case "M9":
                 if (lampM9[0] == mTyp[0] && lampM9[1] == mTyp[1] && lampM9[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM9[0] == mTyp[3] && lampM9[1] == mTyp[4] && lampM9[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM9[0] == mTyp[6] && lampM9[1] == mTyp[7] && lampM9[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM9[0] == mTyp[9] && lampM9[1] == mTyp[10] && lampM9[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM9[0] == mTyp[12] && lampM9[1] == mTyp[13] && lampM9[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            case "M10":
                 if (lampM10[0] == mTyp[0] && lampM10[1] == mTyp[1] && lampM10[2] == mTyp[2] )
                {
                    type = "T1";
                }
                
                if (lampM10[0] == mTyp[3] && lampM10[1] == mTyp[4] && lampM10[2] == mTyp[5] )
                {
                    type = "T2";
                }
                
                if (lampM10[0] == mTyp[6] && lampM10[1] == mTyp[7] && lampM10[2] == mTyp[8] )
                {
                    type = "T3";
                }
                
                if (lampM10[0] == mTyp[9] && lampM10[1] == mTyp[10] && lampM10[2] == mTyp[11] )
                {
                    type = "T4";
                }
                
                if (lampM10[0] == mTyp[12] && lampM10[1] == mTyp[13] && lampM10[2] == mTyp[14] )
                {
                    type = "T5";
                }
                break;
                
            default:
                return;
                
                
        }
        
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        MachineReportProtos.MachineReportEntry mi = MachineReportProtos.MachineReportEntry.newBuilder().setName(name).setType(type).build();
        MachineReport mr = MachineReport.newBuilder().addMachines(mi).build();
        ProtobufMessage machineReport = new ProtobufMessage(2000, 61, mr);
        peer.enqueue(machineReport);
    }
    
    
    public String requestedMachine()
    {
        
        return machinetyp;
    }
}