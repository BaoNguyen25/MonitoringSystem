package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ServerManageFrame extends JFrame implements ActionListener {

    private JButton disconnectBtn, searchBtn, loadBtn;
    private JTable jTable;
    private JList<String> user;
    private Map<String,String> mapPath = new HashMap<>();
    private Map<String, Socket> map = new HashMap<>();
    public static String address;
    public static String pathDirectory = "D:\\";

    public ServerManageFrame(int port) {

        if(ServerHandler.server != null && !ServerHandler.server.isClosed()) {
            JOptionPane.showMessageDialog(this, "Server is running!");
        } else {
            if(port != 0) {
                try {
                    ServerHandler.flag = true;
                    address = InetAddress.getLocalHost().getHostAddress();
                    new Thread(new ServerHandler(port)).start();
                    pathDirectory = Paths.get(".").normalize().toAbsolutePath().toString();
                } catch(IOException e) {
                    JOptionPane.showMessageDialog(this, "Cannot start server");
                }
            }
        }

        Container container = this.getContentPane();
        JLabel portLabel = new JLabel("Port: ");
        JLabel ipLabel = new JLabel("IP: ");
        JLabel listClient = new JLabel("Clients");
        searchBtn = new JButton("Search");

        this.setTitle("Server Monitoring");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
