package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler implements Runnable{

    private int port;
    public static ServerSocket server = null;
    private ArrayList<Socket> clientList = new ArrayList<>();
    private Map<String, Socket> map = new HashMap<>();
    private ArrayList<String> clientName = new ArrayList<>();
    public static boolean flag = true;

    public ServerHandler(int port) throws IOException {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
        }catch(IOException e) {
            e.printStackTrace();
        }

        while(flag) {
            try {
                Socket client = server.accept();
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());
                clientList.add(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
