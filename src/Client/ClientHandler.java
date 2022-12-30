package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    private JButton browseBtn, loadBtn;
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
        browseBtn = new JButton("Browse");
        loadBtn = new JButton("Load logs");
        connectBtn = new JButton("Disconnect");
        pathLabel = new JLabel("Path: " + pathDirectory);
        textPort = new JTextField(Integer.toString(port));
        textIp = new JTextField(ip);

        ipLabel.setBounds(110, 28, 200, 30);
        textIp.setBounds(140, 28, 100, 30);
        portLabel.setBounds(400, 28, 200, 30);
        textPort.setBounds(450, 28, 50, 30);
        nameLabel.setBounds(650, 28, 200, 30);
        connectBtn.setBounds(980, 28, 150, 30);
        pathLabel.setBounds(400, 80, 600, 30);
        browseBtn.setBounds(950, 80, 100, 30);
        loadBtn.setBounds(1060, 80, 100, 30);

        connectBtn.setBackground(Color.PINK);
        loadBtn.setBackground(Color.PINK);
        browseBtn.setBackground(Color.PINK);
        pathLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 20));
        pathLabel.setForeground(Color.orange);
        ipLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 20));
        ipLabel.setForeground(Color.orange);
        portLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 20));
        portLabel.setForeground(Color.orange);
        nameLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 20));
        nameLabel.setForeground(Color.orange);


        connectBtn.addActionListener(this);
        browseBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        container.add(portLabel);
        container.add(ipLabel);
        container.add(nameLabel);
        container.add(browseBtn);
        container.add(loadBtn);
        container.add(connectBtn);
        container.add(pathLabel);
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
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(socket != null && socket.isConnected()) {
                    try {
                        new ClientSender(socket, "Disconnected", "3", nameClient, pathDirectory);
                        MonitorFile.watchService.close();
                    } catch(Exception e1) {
                        e1.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });
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
                    JOptionPane.showMessageDialog(this, "Wrong Port or IP!");
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
            myFileChooser.setDialogTitle("Select folder");
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
        } else if(e.getSource() == loadBtn) {
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
