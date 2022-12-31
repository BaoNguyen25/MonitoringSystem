package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Vector;


public class ServerReceive implements Runnable{

    private Socket client;
    private ArrayList<Socket> clientList;
    private Vector<String> clientName;
    private Map<String, Socket> map;

    public ServerReceive(Socket client, ArrayList<Socket> clientList, Vector<String> clientName, Map<String, Socket> map) {
        this.client = client;
        this.clientList = clientList;
        this.clientName = clientName;
        this.map = map;
    }

    @Override
    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while(true) {
                String s = in.readLine();
                String[] str = s.split(",,");
                String info = str[0];
                String name = str[1];
                String msg = str[2];
                String path = str[3];

                if (info.equals("1")) {
                    new ServerSender(clientList, name, "1", "");
                } else if (info.equals("2")) {
                    if (!clientName.contains(name)) {
                        clientName.add(name);
                        ServerManageFrame.map.put(name, client);
                        ServerManageFrame.mapPath.put(name, path);
                        ServerManageFrame.clients.setListData(clientName);
                        new ServerSender(clientList, name, "2", msg);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[] { ServerManageFrame.jobsModel.getRowCount() + 1, path,
                                dateFormat.format(date), "Connected",
                                name,
                                msg };

                        String data = "{" + (ServerManageFrame.jobsModel.getRowCount() + 1) + ","
                                + path + "," +
                                dateFormat.format(date) + "," + "Connected" + "," +
                                name + "," +
                                msg + "}";

                        FileHandler fh = new FileHandler();
                        fh.writeToFile(data, ServerManageFrame.pathDirectory);
                        ServerManageFrame.jobsModel.addRow(obj);
                        ServerManageFrame.table.setModel(ServerManageFrame.jobsModel);
                    } else {
                        clientList.remove(client);
                        new ServerSender(client, "", "4", "server");
                    }
                } else if (info.equals("3")) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[]{ServerManageFrame.jobsModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Disconnected",
                            name,
                            msg};

                    String data = "{" + (ServerManageFrame.jobsModel.getRowCount() + 1) + ","
                            + path + "," +
                            dateFormat.format(date) + "," + "Disconnected" + "," +
                            name + "," +
                            msg + "}";

                    FileHandler fh = new FileHandler();
                    fh.writeToFile(data, ServerManageFrame.pathDirectory);
                    ServerManageFrame.jobsModel.addRow(obj);
                    ServerManageFrame.table.setModel(ServerManageFrame.jobsModel);

                    clientName.remove(name);
                    clientList.remove(client);
                    ServerManageFrame.map.remove(name);
                    ServerManageFrame.mapPath.remove(name);
                    ServerManageFrame.clients.setListData(clientName);
                    new ServerSender(clientList, clientName, "3", msg);
                    client.close();
                    break;
                }
                else if (info.equals("10")) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { ServerManageFrame.jobsModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Created",
                            name,
                            msg };

                    String data = "{" + (ServerManageFrame.jobsModel.getRowCount() + 1) + ","
                            + path + "," +
                            dateFormat.format(date) + "," + "Created" + "," +
                            name + "," +
                            msg + "}";

                    FileHandler fh = new FileHandler();
                    fh.writeToFile(data, ServerManageFrame.pathDirectory);
                    ServerManageFrame.jobsModel.addRow(obj);
                    ServerManageFrame.table.setModel(ServerManageFrame.jobsModel);

                } else if (info.equals("11")) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { ServerManageFrame.jobsModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Deleted",
                            name,
                            msg };

                    String data = "{" + (ServerManageFrame.jobsModel.getRowCount() + 1) + ","
                            + path + "," +
                            dateFormat.format(date) + "," + "Deleted" + "," +
                            name + "," +
                            msg + "}";

                    FileHandler fh = new FileHandler();
                    fh.writeToFile(data, ServerManageFrame.pathDirectory);
                    ServerManageFrame.jobsModel.addRow(obj);
                    ServerManageFrame.table.setModel(ServerManageFrame.jobsModel);

                } else if (info.equals("12")) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();

                    Object[] obj = new Object[] { ServerManageFrame.jobsModel.getRowCount() + 1, path,
                            dateFormat.format(date), "Modified",
                            name,
                            msg };

                    String data = "{" + (ServerManageFrame.jobsModel.getRowCount() + 1) + ","
                            + path + "," +
                            dateFormat.format(date) + "," + "Modified" + "," +
                            name + "," +
                            msg + "}";

                    FileHandler fh = new FileHandler();
                    fh.writeToFile(data, ServerManageFrame.pathDirectory);
                    ServerManageFrame.jobsModel.addRow(obj);
                    ServerManageFrame.table.setModel(ServerManageFrame.jobsModel);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
