package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class ClientHandler extends JFrame implements ActionListener {

    public static JTable jTable;
    public static Socket socket = null;
    public static String nameClient = "Client";
    public static DefaultTableModel jobsModel;
    public static String pathDirectory = "";


    private JLabel pathLabel;
    private JTextField textIp, textPort;
    private JTextField searchText;
    private JButton searchBtn, browseBtn, connectBtn, loadBtn;

    public ClientHandler(int port, String ip, String name) {
        Container container = this.getContentPane();

        JLabel portLabel = new JLabel("Port: ");
        JLabel ipLabel = new JLabel("IP: ");
        JLabel nameLabel = new JLabel("Name: " + name);
        searchBtn = new JButton("Search");
        browseBtn = new JButton("Browse");
        loadBtn = new JButton("Load logs");
        connectBtn = new JButton("Disconnect");
        pathLabel = new JLabel("Path + " + pathDirectory);
        searchText = new JTextField("");
        textPort = new JTextField(port);
        textIp = new JTextField(ip);

        ipLabel.setBounds(10, 28, 70, 30);
        textIp.setBounds(80, 28, 70, 30);
        portLabel.setBounds(180, 28, 50, 30);
        textPort.setBounds(230, 28, 50, 30);
        nameLabel.setBounds(300, 28, 100, 30);
        connectBtn.setBounds(400, 28, 150, 30);
        pathLabel.setBounds(600, 28, 600, 30);
        browseBtn.setBounds(1050, 28, 100, 30);
        searchBtn.setBounds(400, 80, 150, 30);
        searchText.setBounds(10, 80, 380, 30);
        loadBtn.setBounds(1050, 80, 100, 30);

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

        this.setTitle("Client Monitoring");
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
