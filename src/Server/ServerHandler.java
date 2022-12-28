package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ServerHandler implements Runnable{

    private int port;
    public static ServerSocket serverSocket = null;
    public static ArrayList<Socket> clientList = null;
    public static Map<String,Socket> map = null;
    public static Vector<String> clientName = null;
    public static boolean flag = true;

    public ServerHandler(int port) throws IOException {
        this.port = port;
    }

    @Override
    public void run() {
        clientList = new ArrayList<>();
        map = new HashMap<>();
        clientName = new Vector<>();
        System.out.println("Server is running!");
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(serverSocket.getLocalSocketAddress());
        }catch(IOException e) {
            e.printStackTrace();
        }

        while(flag) {
            try {
                Socket client = serverSocket.accept();
                clientList.add(client);
                new Thread(new ServerReceive(client, clientList, clientName, map)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
