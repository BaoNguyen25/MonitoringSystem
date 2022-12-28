package Client;

import Server.ServerManageFrame;

import java.io.*;
import java.util.Scanner;

public class FileHandler {

    public void readFile(String path) {
        try {
            Scanner scan = new Scanner(new File(path), "UTF-8");
            while(scan.hasNext()) {
                String data1 = scan.nextLine();
                String data2 = data1.replaceAll("\\{", "");
                String data3 = data2.replaceAll("\\}", "");
                String[] arrOfStr = data3.split(",");
                Object[] obj = new Object[] {
                        ClientHandler.jobsModel.getRowCount()+1,
                        arrOfStr[1],
                        arrOfStr[2],
                        arrOfStr[3],
                        arrOfStr[4],
                        arrOfStr[5],
                };
                ClientHandler.jobsModel.addRow(obj);
            }
            ClientHandler.jTable.setModel(ClientHandler.jobsModel);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String value, String pathDirectory, String nameClient) {
        String PATH = pathDirectory + "\\" + nameClient + "\\";
        String directoryName = PATH;
        String fileName = "logs.txt";

        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }

        try {
            FileOutputStream fos = new FileOutputStream(directoryName + "\\" + fileName + "\\", true);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            bufferedWriter.append(value + "\r\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
