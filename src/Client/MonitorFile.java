package Client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonitorFile implements Runnable{
    public static WatchService watchService;
    private Socket s;

    public MonitorFile(Socket s) {
        this.s = s;
    }

    public void dispose() throws IOException {
        watchService.close();
    }

    @Override
    public void run() {
        try {
            System.out.println("Watching directory for changes");
            watchService = FileSystems.getDefault().newWatchService();
            Path directory = Path.of(ClientHandler.pathDirectory);
            WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

            while (true) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;

                    Path fileName = pathEvent.context();

                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        Object[] obj = new Object[] { ClientHandler.jobsModel.getRowCount() + 1,
                                ClientHandler.pathDirectory,
                                dateFormat.format(date), "Created",
                                ClientHandler.nameClient,
                                "A new file is created : " + fileName };

                        String data = "{" + (ClientHandler.jobsModel.getRowCount() + 1) + ","
                                + ClientHandler.pathDirectory + "," +
                                dateFormat.format(date) + "," + "Created" + "," +
                                ClientHandler.nameClient + "," +
                                "A new file is created : " + fileName + "}";

                        ClientHandler.jobsModel.addRow(obj);
                        ClientHandler.jTable.setModel(ClientHandler.jobsModel);
                        FileHandler fh = new FileHandler();
                        fh.writeToFile(data, ClientHandler.pathDirectory, ClientHandler.nameClient);
                        new ClientSender(s, "A file has been created : " + fileName, "10", ClientHandler.nameClient,
                                ClientHandler.pathDirectory);
                    }

                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println("A file has been deleted: " + fileName);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();

                        Object[] obj = new Object[] { ClientHandler.jobsModel.getRowCount() + 1,
                                ClientHandler.pathDirectory,
                                dateFormat.format(date), "Deleted",
                                ClientHandler.nameClient,
                                "A file has been deleted : " + fileName };

                        String data = "{" + (ClientHandler.jobsModel.getRowCount() + 1) + ","
                                + ClientHandler.pathDirectory + "," +
                                dateFormat.format(date) + "," + "Deleted" + "," +
                                ClientHandler.nameClient + "," +
                                "A file has been deleted : " + fileName + "}";

                        ClientHandler.jobsModel.addRow(obj);
                        ClientHandler.jTable.setModel(ClientHandler.jobsModel);
                        FileHandler fh = new FileHandler();
                        fh.writeToFile(data, ClientHandler.pathDirectory, ClientHandler.nameClient);
                        new ClientSender(s, "A file has been deleted : " + fileName, "11", ClientHandler.nameClient,
                                ClientHandler.pathDirectory);
                    }
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        if(fileName.toString().contains("logs.txt")) continue;
                        System.out.println("A file has been modified: " + fileName);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        Object[] obj = new Object[] { ClientHandler.jobsModel.getRowCount() + 1,
                                ClientHandler.pathDirectory,
                                dateFormat.format(date), "Modified",
                                ClientHandler.nameClient,
                                "A file has been modified : " + fileName };

                        String data = "{" + (ClientHandler.jobsModel.getRowCount() + 1) + ","
                                + ClientHandler.pathDirectory + "," +
                                dateFormat.format(date) + "," + "Modified" + "," +
                                ClientHandler.nameClient + "," +
                                "A file has been modified : " + fileName + "}";

                        ClientHandler.jobsModel.addRow(obj);
                        ClientHandler.jTable.setModel(ClientHandler.jobsModel);
                        FileHandler fh = new FileHandler();
                        fh.writeToFile(data, ClientHandler.pathDirectory, ClientHandler.nameClient);
                        new ClientSender(s, "A file has been modified : " + fileName, "12", ClientHandler.nameClient,
                                ClientHandler.pathDirectory);
                    }

                }

                boolean valid = watchKey.reset();
                if (!valid) {
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
