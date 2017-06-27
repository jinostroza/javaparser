package cl.deloitte.wsib;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jinostrozau on 2017-06-20.
 */
public class SSHTest {

    private JSch jsch = null;
    private Session session = null;
    private Channel channel = null;
    private InputStream input = null;
    private OutputStream output = null;
    private FileWriter fileWriter = null;
    private FileReader fileReader = null;
    private BufferedReader bufferedReader = null;
    private String inputPath = "input.txt";
    private String outputPath = "output.txt";
    private String dateYesterday = "";
    private String dateToday = "";

    public static void main(String[] args) throws InterruptedException {
        new SSHTest().init();
    }

    public void init(){
        this.dateToday = DateUtils.dateFormat(new Date());
        this.dateYesterday = DateUtils.dateFormat(DateUtils.getDateYesterday());

        System.out.println("Initializing Connection at " + new Date());

        try {
            if (openConnection("plnxin01.wsib.on.ca", 22, "uex422", "May2017!", 120000)) {
                System.out.println("Connected to server");
                sendCommand("cd /appllog01/GW/Claims_R3_V2/PROD/CCTOImageViewer \n");

                System.out.println("Getting Errors from "+dateYesterday);
                sendCommand("grep 'The TcmDocuments requested do not all belong to the same claim number' Audit_WSIB_ACES_CC_To_ImageViewer_MF.log.0*." + dateYesterday + "|grep -oP '(?<=<Details>).*?(?=</Details>)'|sed 's/&quot\t//g' \n");
                Thread.sleep(2000);
                receiveData(dateYesterday);

                System.out.println("Getting Errors from "+dateToday);
                sendCommand("grep 'The TcmDocuments requested do not all belong to the same claim number' *.log|grep -oP '(?<=<Details>).*?(?=</Details>)'|sed 's/&quot\t//g' \n");
                Thread.sleep(2000);
                receiveData(dateToday);

                closeConnection();
            } else {
                System.out.println("Cannot connect to server \r\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean openConnection(String host, int port, String user, String password, int timeout) {
        boolean result = false;

        jsch = new JSch();
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
        JSch.setConfig(config);

        try {
            this.session = jsch.getSession(user, host, port);
            this.session.setPassword(password);
            this.session.connect(timeout);
            this.channel = this.session.openChannel("shell");
            this.channel.connect();

            try {
                this.input = this.channel.getInputStream();
                this.output = this.channel.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            result = true;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean sendCommand(String command) {
        boolean result = false;

        try {
            if (this.output != null) {
                this.output.write(command.getBytes());
                this.output.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void receiveData(String date) {
        String data = "";

        try {
            if (this.output != null) {

                int iAvailable = this.input.available();

                while (iAvailable > 0) {
                    byte[] btBuffer = new byte[iAvailable];
                    int iByteRead = this.input.read(btBuffer);
                    iAvailable = iAvailable - iByteRead;
                    data += new String(btBuffer);
                }
            }
            writeFile(data, date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFile (String data, String date){
        String findString = "same claim number ";
        ArrayList<String> lista = null;

        try {
            fileWriter = new FileWriter(inputPath);
            fileWriter.write(data);
            fileWriter.close();
            fileWriter = new FileWriter(outputPath);
            fileReader = new FileReader(inputPath);
            bufferedReader = new BufferedReader(fileReader);
            lista = new ArrayList<>();
            lista.add("Date: "+date+"\n");

            while ((data = bufferedReader.readLine()) != null) {
                if (data.contains(findString)) {
                    data = data.substring(data.indexOf(findString) + findString.length(), data.length());
                    data = data.replaceAll(": ", ";");
                    data = data.replaceAll(", ", ";");
                    data = data.concat(";\n");
                    lista.add(data);
                }
            }

            //agrega solo elementos unicos a la lista
            Object[] str = lista.toArray();
            for (Object s : str) {
                if (lista.indexOf(s) != lista.lastIndexOf(s)) {
                    lista.remove(lista.lastIndexOf(s));
                }
            }

            for (String el : lista) {
                fileWriter.write(el);
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (this.session != null) {
            this.session.disconnect();
        }

        if (this.channel != null) {
            this.channel.isClosed();
        }

        if (this.input != null) {
            try {
                this.input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (this.output != null) {
            try {
                this.output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}