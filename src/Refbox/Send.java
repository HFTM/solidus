/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           29.05.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package Refbox;

import org.robocup_logistics.llsf_comm.ProtobufClient;
import org.robocup_logistics.llsf_comm.ProtobufMessage;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos;
import org.robocup_logistics.llsf_msgs.PuckInfoProtos;
import org.robocup_logistics.llsf_msgs.TimeProtos;
import org.robocup_logistics.llsf_tools.NanoSecondsTimestampProvider;

/**
 *
 * @author stecm1
 */
public class Send extends Thread
{

    ProtobufClient client;
    NanoSecondsTimestampProvider nstp;

    Send(ProtobufClient client, NanoSecondsTimestampProvider nstp)
    {

        this.client = client;
        this.nstp = nstp;
    }

    @Override
    public void run()
    {
        while (true)
        {
            long ms = System.currentTimeMillis();
            long ns = nstp.currentNanoSecondsTimestamp();

            int sec = (int) (ms / 1000);
            long nsec = ns - (ms * 1000000L);

            TimeProtos.Time t = TimeProtos.Time.newBuilder().setSec(sec).setNsec(nsec).build();

// ----------------------------- Team und Gerätename senden --------------------
            BeaconSignalProtos.BeaconSignal bs = BeaconSignalProtos.BeaconSignal.newBuilder().setTime(t).setSeq(1).setPeerName("MrPink").setTeamName("Solidus").build();

            ProtobufMessage msg = new ProtobufMessage(2000, 1, bs);

            client.enqueue(msg);

            client.<PuckInfoProtos.PuckInfo>add_message(PuckInfoProtos.PuckInfo.class);

        }
    }

    public static void main(String[] args)
    {
        ProtobufClient client = new ProtobufClient("192.168.1.4", 4444);
        NanoSecondsTimestampProvider nstp = new NanoSecondsTimestampProvider();
        Send send = new Send(client, nstp);
        send.start();
    }
}
