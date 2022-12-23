package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class ServerReceive implements Runnable{

    private Socket client;
    private ArrayList<Socket> clientList;
    private ArrayList<String> clientName;
    private Map<String, Socket> map;

    public ServerReceive(Socket client, ArrayList<Socket> clientList, ArrayList<String> clientName, Map<String, Socket> map) {
        this.client = client;
        this.clientList = clientList;
        this.clientName = clientName;
        this.map = map;
    }

    @Override
    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while(true) {
                String s = in.readLine();
                String[] str = s.split(",,");
                String info = str[0];
                String line = str[1];
                String name = str[2];
                String path = str[3];


            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
