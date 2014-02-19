/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Refbox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_comm.ProtobufMessage;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos;
import org.robocup_logistics.llsf_msgs.TimeProtos;
import org.robocup_logistics.llsf_tools.NanoSecondsTimestampProvider;

/**
 *
 * @author stecm1
 */
public class SendMessage extends Thread
{
    ProtobufMessage beacon;
    
    public ProtobufBroadcastPeer peer;
    String ip;
    int portIn;
    int portOut;

    public SendMessage(String ip, int portIn, int portOut)
    {
        this.ip = ip;
        this.portIn = portIn;
        this.portOut = portOut;
    }

    public void run()
    {
        while (true)
        {
            try
            {
              Thread.sleep(1000);
            }
            catch(Exception e)
            {
            }
            peer = new ProtobufBroadcastPeer(ip, portIn, portOut);
            NanoSecondsTimestampProvider nstp = new NanoSecondsTimestampProvider();

            long ms = System.currentTimeMillis();
            long ns = nstp.currentNanoSecondsTimestamp();

            int sec = (int) (ms / 1000);
            long nsec = ns - (ms * 1000000L);

            TimeProtos.Time t = TimeProtos.Time.newBuilder().setSec(sec).setNsec(nsec).build();
            BeaconSignalProtos.BeaconSignal bs = BeaconSignalProtos.BeaconSignal.newBuilder().setTime(t).setSeq(1).setNumber(1).setPeerName(MainPack.Main.name).setTeamName("Solidus").build();

            beacon = new ProtobufMessage(2000, 1, bs);            
            try
            {
                peer.start();
            } catch (IOException ex)
            {               
            }
            peer.enqueue(beacon);
            peer.stop();             
        }
    }

    public static void main(String[] args)
    {
        //ProtobufClient client = new ProtobufClient("192.168.1.4", 4444);
        //NanoSecondsTimestampProvider nstp = new NanoSecondsTimestampProvider();
        SendMessage send = new SendMessage("192.168.1.4", 4444, 4444);
        send.start();
        //send.stop();
    }
}
