package cl.deloitte.wsib;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by jinostrozau on 2017-06-20.
 */
public class SSHTest {

    private JSch jsch = null;//new JSch();
    private Session session = null;//new Session();
    private Channel channel = null;
    private InputStream input = null;
    private OutputStream output = null;
    private FileWriter fileWriter = null;
//    private String inputPath = "C://JIU/input.txt";
//    private String outputPath = "C://JIU/output.txt";

    private String inputPath = "/Users/jiu/input.txt";
    private String outputPath = "/Users/jiu/output.txt";


    public boolean openConnection(String host, int port, String user, String password, int timeout){
        boolean result = false;

        jsch = new JSch();
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
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

    public boolean sendCommand(String command){
        boolean result = false;

        try{
            if(this.output != null ){
                this.output.write(command.getBytes());
                this.output.flush();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String recData(){
        String data = "";
        String st = "same claim number ";
        ArrayList<String> lista = new ArrayList<>();

        try{
            if(this.output != null){

                int iAvailable = this.input.available();

                while(iAvailable > 0 ){
                    byte[] btBuffer = new byte[iAvailable];
                    int iByteRead = this.input.read(btBuffer);
                    iAvailable = iAvailable - iByteRead;
                    data += new String(btBuffer);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            fileWriter = new FileWriter(inputPath);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            FileWriter fileWriter2 = new FileWriter(outputPath);

            fileReader = new FileReader(inputPath);
            bufferedReader = new BufferedReader(fileReader);

            while ((data = bufferedReader.readLine()) != null) {
                if (data.contains(st)) {
                    data = data.substring(data.indexOf(st) + st.length(), data.length());

                    data = data.replaceAll(": ", ";");
                    data = data.replaceAll(", ", ";");
                    data = data.concat("\n");

                    lista.add(data);

                   // data = borraRepetidos(data);

                    System.out.println("Imprimitendo: " + data);

                    //fileWriter2.write(data);
                }
            }

            int cont =0;

            for(String el: lista){

                System.out.println(el);
                cont++;
            }
            System.out.println("total "+cont);

            Object[] str = lista.toArray();
            for (Object s : str) {
                if (lista.indexOf(s) != lista.lastIndexOf(s)) {
                    lista.remove(lista.lastIndexOf(s));
                }
            }

            cont = 0;


            for(String el: lista){

                System.out.println(el);
                cont++;
                fileWriter2.write(el);
            }

            System.out.println("total "+cont);



            fileWriter2.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return data;
    }

    public void close(){
        if(this.session != null){
            this.session.disconnect();
        }

        if(this.channel != null){
            this.channel.isClosed();
        }

        if(this.input != null){
            try {
                this.input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(this.output != null){
            try {
                this.output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String borraRepetidos(String data){

        //algoritmo: ordenarlos primero y luego borrar los repetidos

        BufferedReader br = null;

        ArrayList<String> lista = new ArrayList<>();


        try {
            FileReader fr = new FileReader(outputPath);
            br = new BufferedReader(fr);

            while ((data = br.readLine()) != null) {
                lista.add(data);
            }

            int cont = 0 ;

            for(String el: lista){

                System.out.println(el);
                cont++;
            }
            System.out.println("total "+cont);

            Object[] st = lista.toArray();
            for (Object s : st) {
                if (lista.indexOf(s) != lista.lastIndexOf(s)) {
                    lista.remove(lista.lastIndexOf(s));
                }
            }

            cont = 0;


            for(String el: lista){

                System.out.println(el);
                cont++;
            }

            System.out.println("total "+cont);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        return data;
    }

    public static void main(String[] args) throws InterruptedException {
        SSHTest test = new SSHTest();
        String dateYesterday = "2017-06-20";


        //test.borraRepetidos();
        if(test.openConnection("plnxin01.wsib.on.ca",22, "uex422","May2017!", 120000)){
            System.out.println("Connected to server");
            test.sendCommand("cd /appllog01/GW/Claims_R3_V2/PROD/CCTOImageViewer \n");
            //test.sendCommand("grep \"The TcmDocuments requested do not all belong to the same claim number\" *.log|grep -oP '(?<=<Details>).*?(?=</Details>)'|sed  's/&quot\t//g' \n");
            test.sendCommand("grep 'The TcmDocuments requested do not all belong to the same claim number' Audit_WSIB_ACES_CC_To_ImageViewer_MF.log.0*."+dateYesterday+" *.log|grep -oP '(?<=<Details>).*?(?=</Details>)'|sed  's/&quot\t//g' \n");
            Thread.sleep(2000);
            test.recData();
            //System.out.println("Result: "+test.recData());
            test.close();
        }else{
            System.out.println("Cannot connect to server \r\n");
        }
    }
}