package Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
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
    private JLabel ipVal, portVal;
    private JTextField searchText;
    public static JTable jTable;
    public static JList<String> clients;
    public static Map<String,String> mapPath = new HashMap<>();
    public static Map<String, Socket> map = new HashMap<>();
    public static String address;
    public static String pathDirectory = "D:\\";
    public static DefaultTableModel jobsModel;

    public ServerManageFrame(int port) {

        if(ServerHandler.serverSocket != null && !ServerHandler.serverSocket.isClosed()) {
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
        loadBtn = new JButton("Load Logs");
        disconnectBtn = new JButton("Disconnect");
        searchText = new JTextField("");
        portVal = new JLabel(String.valueOf(port));
        ipVal = new JLabel(String.valueOf(address));
        clients = new JList<String>();
        JScrollPane paneUser = new JScrollPane(clients);

        ipLabel.setBounds(10, 8, 80, 30);
        ipVal.setBounds(30, 8, 100, 30);
        portLabel.setBounds(150, 8, 60, 30);
        portVal.setBounds(200, 8, 70, 30);
        listClient.setBounds(10, 80, 100, 30);
        searchBtn.setBounds(530, 70, 150, 30);
        disconnectBtn.setBounds(0, 0, 0, 0);
        searchText.setBounds(147, 70, 380, 30);
        loadBtn.setBounds(1071, 70, 100, 30);
        paneUser.setBounds(10, 110, 130, 320);

        container.setLayout(null);
        container.add(portLabel);
        container.add(ipLabel);
        container.add(listClient);
        container.add(searchBtn);
        container.add(loadBtn);
        container.add(disconnectBtn);
        container.add(searchText);
        container.add(portVal);
        container.add(ipVal);
        container.add(clients);
        container.add(paneUser);

        jobsModel = new DefaultTableModel(
                new String[] { "STT", "Monitoring directory", "Time", "Action", "Name Client", "Description" }, 0) {
            public Class getColumnClass(int column) {
                Class returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        jTable = new JTable();
        jTable.setModel(jobsModel);
        jTable.setAutoCreateRowSorter(true);
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jobsModel);
        jTable.setRowSorter(sorter);
        jTable.setBounds(145, 110, 1030, 320);

        TableColumnModel columnModel = jTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(400);

        JScrollPane sp = new JScrollPane(jTable);
        sp.setBounds(145, 110, 1030, 320);
        container.add(sp);

        this.setTitle("Server Monitoring");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200, 480);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
