package Client;

import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender {

    //send message to server
    public ClientSender(Socket socket, Object message, String info, String name, String path) throws Exception {
        String messages = info + ",," + name + ",," + message + ",," + path;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(messages);
    }
}
