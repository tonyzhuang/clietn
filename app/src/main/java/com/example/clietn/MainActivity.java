package com.example.clietn;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final String SERVERIP = "127.0.0.1";
    public static final int SERVERPORT = 4444;
    public TextView text1;
    public EditText input;
    public Button btn;
    public boolean start;
    public Handler Handler;

    private String receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.textView1);
        input = (EditText) findViewById(R.id.editText1);
        btn = (Button) findViewById(R.id.button1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = true;
            }

        });


        start = false;
        //new Thread(new Server()).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }
        new Thread(new Client()).start();
        Handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                text1.append(text);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class Client implements Runnable {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void run() {
            while (start == false) {
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                InetAddress serverAddr = InetAddress.getByName("192.168.137.1");
                updatetrack("Client: Start connecting\n");
                DatagramSocket socket = new DatagramSocket();
                byte[] buf;
                if (!input.getText().toString().isEmpty()) {
                    buf = input.getText().toString().getBytes();
                } else {
                    buf = ("Default message").getBytes();
                }
                DatagramPacket packet = new DatagramPacket(buf, buf.length,
                        serverAddr, 5050);
                updatetrack("Client: Sending ‘" + new String(buf) + "’\n");
                socket.send(packet);
                updatetrack("Client: Message sent\n");
                updatetrack("Client: Succeed!\n");
            } catch (Exception e) {
                updatetrack("Client: Error!\n");
            }
        }
    }

    public class Server implements Runnable {
        @Override
        public void run() {
            while (start == false) {
            }
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                updatetrack("\nServer: Start connecting\n");
                DatagramSocket socket = new DatagramSocket(SERVERPORT,
                        serverAddr);
                byte[] buf = new byte[17];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                updatetrack("Server: Receiving\n");
                socket.receive(packet);
                updatetrack("Server: Message received: ‘"
                        + new String(packet.getData()) + "’\n");
                updatetrack("Server: Succeed!\n");
            } catch (Exception e) {
                updatetrack("Server: Error!\n");
            }
        }
    }

    public void updatetrack(String s) {
        Message msg = new Message();
        String textTochange = s;
        msg.obj = textTochange;
        Handler.sendMessage(msg);
    }
}
