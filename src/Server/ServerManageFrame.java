package Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ServerManageFrame extends JFrame implements ActionListener{

    private JButton disconnectBtn, searchBtn, loadBtn;
    private JLabel ipVal, portVal;
    private JTextField searchText;
    public static JTable table;
    public static JList<String> clients;
    public static Map<String,String> mapPath = new HashMap<>();
    public static Map<String, Socket> map = new HashMap<>();
    public static String address;
    public static String pathDirectory = "D:\\";
    public static DefaultTableModel jobsModel;
    private Container container;
    final TableRowSorter<TableModel> sorter = new TableRowSorter<>();

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

        container = this.getContentPane();
        JLabel portLabel = new JLabel("Port: ");
        JLabel ipLabel = new JLabel("IP: ");
        JLabel listClient = new JLabel("Clients");
        searchBtn = new JButton("Search");
        loadBtn = new JButton("Load Logs");
        disconnectBtn = new JButton("Disconnect");
        searchText = new JTextField("");
        portVal = new JLabel(String.valueOf(port));
        ipVal = new JLabel(String.valueOf(address));

        clients = new JList<>();
        JScrollPane paneUser = new JScrollPane(clients);

        portLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 20));
        portLabel.setForeground(Color.orange);
        portVal.setFont(new Font("Gill Sans MT", Font.BOLD, 20));
        portVal.setForeground(Color.orange);

        ipLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 20));
        ipLabel.setForeground(Color.orange);
        ipVal.setFont(new Font("Gill Sans MT", Font.BOLD, 20));
        ipVal.setForeground(Color.orange);
        listClient.setFont(new Font("Gill Sans MT", Font.BOLD, 20));
        listClient.setForeground(Color.orange);

        searchBtn.setBackground(Color.PINK);
        loadBtn.setBackground(Color.PINK);

        ipLabel.setBounds(400, 20, 80, 30);
        ipVal.setBounds(430, 20, 200, 30);
        portLabel.setBounds(700, 20, 60, 30);
        portVal.setBounds(750, 20, 70, 30);
        listClient.setBounds(1080, 80, 100, 30);
        searchBtn.setBounds(400, 70, 150, 30);
        disconnectBtn.setBounds(0, 0, 0, 0);
        searchText.setBounds(10, 70, 380, 30);
        loadBtn.setBounds(940, 70, 100, 30);
        paneUser.setBounds(1048, 110, 130, 320);


        loadBtn.addActionListener(this);
        searchBtn.addActionListener(this);
        disconnectBtn.addActionListener(this);

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

        table = new JTable();
        table.setModel(jobsModel);
        table.setBounds(145, 110, 1030, 320);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(400);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(10, 110, 1030, 320);
        container.add(sp);
        container.setBackground(new Color(81,80,106));
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(ServerHandler.clientList != null && ServerHandler.clientList.size() != 0) {
                    try {
                        new ServerSender(ServerHandler.clientList, "Server die", "5", "Server");
                    } catch(IOException e1) {
                        e1.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });

        this.setTitle("Server Monitoring");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200, 480);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loadBtn) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select file");
            if(Files.isDirectory(Paths.get(pathDirectory))) {
                fileChooser.setCurrentDirectory(new File(pathDirectory));
            }

            int result = fileChooser.showOpenDialog(container);
            if(result == fileChooser.APPROVE_OPTION) {
                String path = fileChooser.getCurrentDirectory().getPath() + "\\logs.txt";
                FileHandler fh = new FileHandler();
                clearTable();
                fh.readFile(path);
            }
        }


    }
    void clearTable() {
        int rowCount = jobsModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            jobsModel.removeRow(i);
        }
    }
}
