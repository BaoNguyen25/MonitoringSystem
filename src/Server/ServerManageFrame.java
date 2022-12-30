package Server;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
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
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServerManageFrame extends JFrame implements ActionListener{

    private JButton disconnectBtn, searchBtn, loadBtn;
    private JLabel ipVal, portVal;
    private JTextField searchText;
    public static JTable table;
    public static JList<String> clients;
    public static Map<String,String> mapPath = new HashMap<>();
    public static Map<String,Socket> map = new HashMap<>();
    public static String address;
    public static String pathDirectory = "D:\\";
    public static DefaultTableModel jobsModel;
    public static DefaultTableModel tempModel;
    private Container container;

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
        clients.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                JList source = (JList) e.getSource();
                String selected = source.getSelectedValue().toString();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select Folder");
                if(Files.isDirectory(Paths.get(mapPath.get(selected)))) {
                    fileChooser.setCurrentDirectory(new File(mapPath.get(selected)));
                }
                int result = fileChooser.showOpenDialog(container);
                if(result == fileChooser.APPROVE_OPTION) {
                    String pathClient = fileChooser.getCurrentDirectory().getAbsolutePath();
                    try {
                        new ServerSender(map.get(selected), pathClient, "13", "Server");
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[] { jobsModel.getRowCount() + 1, pathClient,
                                dateFormat.format(date), "Change path",
                                selected, "Change path monitoring system"};

                        String data = "{" + (jobsModel.getRowCount() + 1) + ","
                                + pathClient + "," +
                                dateFormat.format(date) + "," + "Change path" + "," +
                                selected + "," +
                                "Change path monitoring system" + "}";
                        FileHandler fh = new FileHandler();
                        fh.writeToFile(data, pathDirectory);
                        jobsModel.addRow(obj);
                        table.setModel(jobsModel);
                    } catch(IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
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
        columnModel.getColumn(0).setWidth(20);
        columnModel.getColumn(1).setWidth(150);
        columnModel.getColumn(2).setWidth(100);
        columnModel.getColumn(3).setWidth(100);
        columnModel.getColumn(4).setWidth(100);
        columnModel.getColumn(5).setWidth(400);

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
        else if(e.getSource() == searchBtn) {
            String text = searchText.getText();
            if(text.length() == 0) {
                table.setModel(jobsModel);
            } else {
                String[][] searchVal = search(text);
                tempModel = new DefaultTableModel(searchVal, new String[] { "STT", "Monitoring directory", "Time", "Action", "Name Client", "Description" });
                table.setModel(tempModel);
            }

        }

    }
    void clearTable() {
        int rowCount = jobsModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            jobsModel.removeRow(i);
        }
    }

    String[][] search(String text) {
        Vector<Vector> vectors = jobsModel.getDataVector();
        ArrayList<Vector> list = new ArrayList<>();
        System.out.println(text);
        for(Vector v : vectors) {
            if(v.get(4).toString().contains(text))
                list.add(v);
        }

        String[][] array = new String[list.size()][];
        for(int i = 0; i < list.size(); i++) {
            Vector v = list.get(i);
            ArrayList<String> cur = new ArrayList<>();
            for(Object o : v) {
                cur.add(o.toString());
            }
            array[i] = cur.toArray(new String[cur.size()]);
        }

        return array;
    }
}
