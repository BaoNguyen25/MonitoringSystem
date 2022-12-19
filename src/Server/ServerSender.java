package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSender {

    //send message to all clients
    public ServerSender(ArrayList<Socket> clientList, Object message, String info, String name) throws IOException {
        String messages = info + "." + message + "." + name;
        for(Socket s : clientList) {
            PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
            pw.println(messages);
        }
    }

    //send message to a client
    public ServerSender(Socket socket, Object message, String info, String name) throws IOException {
        String messages = info + "." + message + "." + name;
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        pw.println(messages);
    }
}
