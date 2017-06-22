package cl.deloitte.wsib;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;

import java.io.*;

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
    private String path = "C://JIU/data.txt";


    public boolean openConnection(String host, int port, String user, String password, int timeout){
        boolean result = false;

        jsch = new JSch();
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        this.jsch.setConfig(config);

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

        //System.out.println("Data: "+data);

        /*try {
            fileWriter = new FileWriter(path);
            fileWriter.write(data);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        BufferedReader bufferedReader = new BufferedReader(path);

        while ((data = bufferedReader.readLine()) != null) {
            if (data.contains(st)) {
                data = data.substring(data.indexOf(st) + st.length(), data.length());

                System.out.println("Imprimitendo: " + data);

                dataExt = dataExt.replaceAll(": ", "\t");
                //dataExt = dataExt.replaceAll(", ", "\t");
                //dataExt.concat("\n");
                //dataFinal+=dataExt;
            }
        }*/
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

    /*public String parseData(){

        String st = "same claim number ";
        String dataExt = "";

        String data2 = "";
        FileReader fileReader = null;
        String dataFinal = "";

        try {
            fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((data2 = bufferedReader.readLine()) != null) {

                if(data2.contains(st)) {
                    dataExt = data2.substring(data2.indexOf(st) + st.length(), data2.length());

                    dataExt = dataExt.replaceAll(": ", "\t");
                    dataExt = dataExt.replaceAll(", ", "\t");
                    dataExt.concat("\n");
                    dataFinal+=dataExt;
                }
            }

            FileWriter fw = new FileWriter("C://JIU/results.txt");
            fw.write(dataFinal);
            fw.close();

            System.out.println(dataFinal);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataExt;
    }*/

    public static void main(String[] args) throws InterruptedException {
        SSHTest test = new SSHTest();
        String dateYesterday = "2017-06-20";

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