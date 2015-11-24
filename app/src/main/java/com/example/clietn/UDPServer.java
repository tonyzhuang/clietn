package com.example.clietn;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by 远航 on 2015/10/24.
 */
public class UDPServer implements Runnable{

    private static final int PORT = 6000;

    private byte[] msg = new byte[1024];

    private boolean life = true;

    public UDPServer() {
    }

    /**
     * @return the life
     */
    public boolean isLife() {
        return life;
    }

    /**
     * @param life
     * the life to set
     */
    public void setLife(boolean life) {
        this.life = life;
    }

    @Override
    public void run() {
        DatagramSocket dSocket = null;
        DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
        try {
            dSocket = new DatagramSocket(PORT);
            while (life) {
                try {
                    dSocket.receive(dPacket);
                    Log.i("msg sever received", new String(dPacket.getData()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

}
