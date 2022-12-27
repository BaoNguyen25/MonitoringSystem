package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler extends JFrame implements ActionListener {

    public static JTable jTable;
    public static Socket socket = null;
    public static String nameClient = "Client";
    public static DefaultTableModel jobsModel;
    public static String pathDirectory = "";
    public static JButton connectBtn;

    String globalId;
    int globalPort;
    public static JLabel pathLabel;
    private JTextField textIp, textPort;
    private JTextField searchText;
    private JButton searchBtn, browseBtn, loadBtn;
    public static Container container;
    public ClientHandler(int port, String ip, String name) {
        pathDirectory = Paths.get(".").normalize().toAbsolutePath() + "\\";
        if (socket != null && socket.isConnected()) {
            JOptionPane.showMessageDialog(this, "Connected!");
        } else {
            try {
                socket = new Socket(ip, port);
                nameClient = name;
                globalId = ip;
                globalPort = port;
                new ClientSender(socket, "Connected", "2", name, pathDirectory);
                new Thread(new ClientReceive(socket)).start();

            } catch (Exception e2) {
                JOptionPane.showMessageDialog(this, "Can't connect check ip and port");
            }
        }
        new Thread(new MonitorFile(socket)).start();

        container = this.getContentPane();

        JLabel portLabel = new JLabel("Port: ");
        JLabel ipLabel = new JLabel("IP: ");
        JLabel nameLabel = new JLabel("Name: " + name);
        searchBtn = new JButton("Search");
        browseBtn = new JButton("Browse");
        loadBtn = new JButton("Load logs");
        connectBtn = new JButton("Disconnect");
        pathLabel = new JLabel("Path: " + pathDirectory);
        searchText = new JTextField("");
        textPort = new JTextField(Integer.toString(port));
        textIp = new JTextField(ip);

        ipLabel.setBounds(15, 28, 70, 30);
        textIp.setBounds(40, 28, 100, 30);
        portLabel.setBounds(180, 28, 50, 30);
        textPort.setBounds(220, 28, 50, 30);
        nameLabel.setBounds(300, 28, 100, 30);
        connectBtn.setBounds(400, 28, 150, 30);
        pathLabel.setBounds(600, 28, 600, 30);
        browseBtn.setBounds(1050, 28, 100, 30);
        searchBtn.setBounds(400, 80, 150, 30);
        searchText.setBounds(10, 80, 380, 30);
        loadBtn.setBounds(1050, 80, 100, 30);

        searchBtn.setBackground(Color.PINK);
        connectBtn.setBackground(Color.PINK);
        loadBtn.setBackground(Color.PINK);
        browseBtn.setBackground(Color.PINK);
        pathLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 18));
        pathLabel.setForeground(Color.orange);
        ipLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
        ipLabel.setForeground(Color.orange);
        portLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
        portLabel.setForeground(Color.orange);
        nameLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
        nameLabel.setForeground(Color.orange);


        connectBtn.addActionListener(this);
        browseBtn.addActionListener(this);
        searchBtn.addActionListener(this);

        container.add(portLabel);
        container.add(ipLabel);
        container.add(nameLabel);
        container.add(searchBtn);
        container.add(browseBtn);
        container.add(loadBtn);
        container.add(connectBtn);
        container.add(pathLabel);
        container.add(searchText);
        container.add(textIp);
        container.add(textPort);
        container.setLayout(null);

        jobsModel = new DefaultTableModel(
                new String[]{"STT", "Monitoring directory", "Time", "Action", "Name Client", "Description"}, 0) {
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
        jTable.setBounds(10, 120, 1160, 300);

        TableColumnModel columnModel = jTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(20);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(400);
        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(jTable);
        sp.setBounds(10, 120, 1160, 300);
        container.add(sp);

        container.setBackground(new Color(80,81,106));
        this.setTitle("Client Monitoring");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200, 480);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connectBtn) {
            if (socket == null) {
                try {
                    globalId = textIp.getText();
                    globalPort = Integer.parseInt(textPort.getText());
                    socket = new Socket(globalId, globalPort);
                    connectBtn.setText("Disconnect");

                    new ClientSender(socket, "Connected", "2", nameClient, pathDirectory);
                    new Thread(new ClientReceive(socket)).start();
                    new Thread(new MonitorFile(socket)).start();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[]{jobsModel.getRowCount() + 1, pathDirectory,
                            dateFormat.format(date), "Connected",
                            nameClient,
                            "(Notification) " + nameClient + " connected to server!"};

                    String data = "{" + (ClientHandler.jobsModel.getRowCount() + 1) + ","
                            + ClientHandler.pathDirectory + "," +
                            dateFormat.format(date) + "," + "Connected" + "," +
                            ClientHandler.nameClient + "," +
                            "(Notification) " + ClientHandler.nameClient + " connected to the server!" + "}";

                    FileHandler fh = new FileHandler();
                    fh.writeToFile(data, ClientHandler.pathDirectory, ClientHandler.nameClient);
                    jobsModel.addRow(obj);
                    jTable.setModel(jobsModel);
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(this, "Can't connect check ip and port");
                }
            } else if (socket != null && socket.isConnected()) {
                try {
                    new ClientSender(socket, "Disconnected", "3", nameClient, pathDirectory);
                    connectBtn.setText("Connect");
                    MonitorFile.watchService.close();
                    socket.close();
                    socket = null;
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[]{jobsModel.getRowCount() + 1, pathDirectory,
                            dateFormat.format(date), "Disconnected",
                            nameClient,
                            "(Notification) " + nameClient + " disconnected to server!"};

                    String data = "{" + (ClientHandler.jobsModel.getRowCount() + 1) + ","
                            + ClientHandler.pathDirectory + "," +
                            dateFormat.format(date) + "," + "Disconnected" + "," +
                            ClientHandler.nameClient + "," +
                            "(Notification) " + ClientHandler.nameClient + " disconnected to the server!" + "}";

                    FileHandler fh = new FileHandler();
                    fh.writeToFile(data, ClientHandler.pathDirectory, ClientHandler.nameClient);
                    jobsModel.addRow(obj);
                    jTable.setModel(jobsModel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } else if (e.getSource() == browseBtn) {
            JFileChooser myFileChooser = new JFileChooser();
            myFileChooser.setDialogTitle("select folder");
            if (Files.isDirectory(Paths.get(pathDirectory))) {
                myFileChooser.setCurrentDirectory(new File(pathDirectory));
            }
            int findResult = myFileChooser.showOpenDialog(container);
            if (findResult == myFileChooser.APPROVE_OPTION) {
                pathDirectory = myFileChooser.getCurrentDirectory().getAbsolutePath();
                pathLabel.setText("Path: " + pathDirectory);
                try {
                    MonitorFile.watchService.close();
                    new Thread(new MonitorFile(socket)).start();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }
}
