package Client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientReceive implements Runnable {
    private Socket socket;

    public ClientReceive(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(ClientHandler.socket.getInputStream()));
            Thread.sleep(500);
            while (true) {
                String s = in.readLine();
                if(s == null)
                    break;
                String[] str = s.split("\\.");
                String info = str[0];
                String msg = str[1];
                if (info.equals("1")) {
                } else if (info.equals("2") || info.equals("3")) {
                    if (info.equals("2")) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[]{ClientHandler.jobsModel.getRowCount() + 1,
                                ClientHandler.pathDirectory,
                                dateFormat.format(date), "Connected",
                                ClientHandler.nameClient,
                                "(Notification) " + ClientHandler.nameClient + " connected to server!"};

                        String data = "{" + (ClientHandler.jobsModel.getRowCount() + 1) + ","
                                + ClientHandler.pathDirectory + "," +
                                dateFormat.format(date) + "," + "Connected" + "," +
                                ClientHandler.nameClient + "," +
                                "(Notification) " + ClientHandler.nameClient + " connected to server!" + "}";

                        ClientHandler.jobsModel.addRow(obj);
                        ClientHandler.jTable.setModel(ClientHandler.jobsModel);
                        FileHandler fh = new FileHandler();
                        fh.writeToFile(data, ClientHandler.pathDirectory, ClientHandler.nameClient);
                    } else {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[]{ClientHandler.jobsModel.getRowCount() + 1,
                                ClientHandler.pathDirectory,
                                dateFormat.format(date), "Disconnected",
                                ClientHandler.nameClient,
                                "(Notification)" + ClientHandler.nameClient + " disconnected to server!"};

                        String data = "{" + (ClientHandler.jobsModel.getRowCount() + 1) + ","
                                + ClientHandler.pathDirectory + "," +
                                dateFormat.format(date) + "," + "Disconnected" + "," +
                                ClientHandler.nameClient + "," +
                                "(Notification) " + ClientHandler.nameClient + " disconnected to server!" + "}";

                        ClientHandler.jobsModel.addRow(obj);
                        ClientHandler.jTable.setModel(ClientHandler.jobsModel);
                        FileHandler fh = new FileHandler();
                        fh.writeToFile(data, ClientHandler.pathDirectory, ClientHandler.nameClient);
                    }
                } else if (info.equals("13")) {
                    ClientHandler.pathDirectory = msg + "\\";
                    ClientHandler.pathLabel.setText("Path: " + msg);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { ClientHandler.jobsModel.getRowCount() + 1,
                            ClientHandler.pathDirectory,
                            dateFormat.format(date), "Change path",
                            ClientHandler.nameClient,
                            "(Notification) Server send change path" };

                    String data = "{" + (ClientHandler.jobsModel.getRowCount() + 1) + ","
                            + ClientHandler.pathDirectory + "," +
                            dateFormat.format(date) + "," + "Change path" + "," +
                            ClientHandler.nameClient + "," +
                            "(Notification) Server send change path" + "}";

                    ClientHandler.jobsModel.addRow(obj);
                    ClientHandler.jTable.setModel(ClientHandler.jobsModel);
                    FileHandler fh = new FileHandler();
                    fh.writeToFile(data, ClientHandler.pathDirectory, ClientHandler.nameClient);

                    MonitorFile.watchService.close();
                    new Thread(new MonitorFile(ClientHandler.socket)).start();

                    break;
                } else if (info.equals("5")) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { ClientHandler.jobsModel.getRowCount() + 1,
                            ClientHandler.pathDirectory,
                            dateFormat.format(date), "Server die",
                            ClientHandler.nameClient,
                            "(Notification) Server has been die" };

                    String data = "{" + (ClientHandler.jobsModel.getRowCount() + 1) + ","
                            + ClientHandler.pathDirectory + "," +
                            dateFormat.format(date) + "," + "Server die" + "," +
                            ClientHandler.nameClient + "," +
                            "(Notification) Server has been die" + "}";

                    ClientHandler.jobsModel.addRow(obj);
                    ClientHandler.jTable.setModel(ClientHandler.jobsModel);
                    FileHandler fh = new FileHandler();
                    fh.writeToFile(data, ClientHandler.pathDirectory, ClientHandler.nameClient);
                    ClientHandler.connectBtn.setText("Connect");
                    JOptionPane.showMessageDialog(ClientHandler.container, "Server disconnect, please connect again");
                    MonitorFile.watchService.close();
                    ClientHandler.socket.close();
                    ClientHandler.socket = null;
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
