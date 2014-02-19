/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           07.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package Refbox;

import Interface.UserFrame;
import java.io.IOException;
import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_comm.ProtobufClient;
import org.robocup_logistics.llsf_msgs.AttentionMessageProtos.AttentionMessage;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos.BeaconSignal;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationInfo;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.MachineInfo;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.OrderInfo;
import org.robocup_logistics.llsf_msgs.PuckInfoProtos.PuckInfo;
import org.robocup_logistics.llsf_msgs.RobotInfoProtos.RobotInfo;
import org.robocup_logistics.llsf_msgs.VersionProtos.VersionInfo;

/**
 *
 * @author stecm1
 */
public class ComRefbox
{
    public ProtobufClient client;
    public Handler handler;
    static UserFrame frame;
    public ProtobufBroadcastPeer peer;

    public int gamePoints;             // Akutueller Punktestand
    public String gamePhase;           // Aktuelle Game Phase (PRE_GAME, EXPLORATION, PRODUCTION, POST_GAME)
    public String gameState;           // Aktueller Game Status (WAIT_STRT, RUNNING, PAUSED)
    public boolean hasTime;            // Ist noch zeit zur Verfügung
    public int gameTime;               // Aktuelle Spielzeit (EXPLORATION: 0-180, PRODUCTION: 0-900)

    public ComRefbox(String ip, int portIn, int portOut, String name, UserFrame frame)
    {
        // --------------------------- Verbinden mit Refbox per UDP ----------------------------
        peer = new ProtobufBroadcastPeer(ip, portIn, portOut);

        try
        {
            peer.start();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        handler = new Handler(frame, peer);
        peer.register_handler(handler);
    }

    /**
     * ?? event ??? ??? ??? refbox --> controller Client-Server
     */
    public void addAttentionMessage()
    {
        peer.<AttentionMessage>add_message(AttentionMessage.class);
    }

    /**
     * ok periodisch 1sec ??? refbox --> all P2P & C-S
     */
    public void addBeaconSignal()
    {
        client.<BeaconSignal>add_message(BeaconSignal.class);
    }

    //?? periodisch? EVENT! ??? refbox --> any       P2P & C-S
    public void addVersionInfo()
    {
        peer.<VersionInfo>add_message(VersionInfo.class);
    }

    /**
     * ?? periodisch 1sec ??? refbox --> robots PeerToPeer
     */
    public void addExplorationInfo()
    {
        peer.<ExplorationInfo>add_message(ExplorationInfo.class);
    }

    //ok periodisch 1sec ??? refbox --> all          P2P & C-S
    /**
     * Hört auf die Nachricht "GameState" werte: - Aktueller Punktestand -
     * Aktuelle Spielzeit (EXPLORATION: 0-180, PRODUCTION: 0-900) - Aktueller
     * Spiel Phase (PRE_GAME, EXPLORATION, PRODUCTION, POST_GAME) - Aktueller
     * Spiel Status (WAIT_STRT, RUNNING, PAUSED) - Ob noch Spielzeit vorhanden
     * ist.
     */
    public void addGameStateMessage()
    {
        peer.<GameState>add_message(GameState.class);
    }

    /**
     * ok periodisch 0.25sec ??? refbox --> all P2P & C-S
     */
    public void addMachineInfo()
    {
        peer.<MachineInfo>add_message(MachineInfo.class);
    }

    /**
     * ?? ??? 5sec ??? refbox --> any P2P & C-S
     */
    public void addOrderInfo()
    {
        peer.<OrderInfo>add_message(OrderInfo.class);
    }

    /**
     * ok periodisch 1sec ok refbox --> controller Client-Server
     */
    public void addPuckInfo()
    {
        peer.<PuckInfo>add_message(PuckInfo.class);
    }

    /**
     * ok periodisch 1sec ??? refbox --> controller Client-Server
     */
    public void addRobotInfo()
    {
        peer.<RobotInfo>add_message(RobotInfo.class);
    }

    /**
     *
     * @return Gibt den aktuellen Spiel Status zurück (WAIT_STRT, RUNNING,
     * PAUSED).
     */
    public String getState()
    {
        return handler.getState();
    }

    /**
     *
     * @return Gibt die aktuelle Spiel Phase zurück (PRE_GAME, EXPLORATION,
     * PRODUCTION, POST_GAME).
     */
    public String getPhase()
    {
        return handler.getPhase();
    }

    /**
     *
     * @return Git an ob noch Spielzeit vorhanden ist.
     */
    public String getHasTime()
    {
        return handler.getHasTime();
    }

    /**
     *
     * @return Gibt die aktuelle Spielzeit zurück (EXPLORATION: 0-180,
     * PRODUCTION: 0-900).
     */
    public String getTime()
    {
        return handler.getTime();
    }

    /**
     *
     * @return Gibt den Aktuellen Punktestand zurück.
     */
    public String getPoints()
    {
        return handler.getPoints();
    }

}
