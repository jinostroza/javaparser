package cl.deloitte.wsib;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jinostrozau on 2017-06-20.
 */
public class SSHTest {

    private JSch jsch = null;//new JSch();
    private Session session = null;//new Session();
    private Channel channel = null;
    private InputStream input = null;
    private OutputStream output = null;


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

    public static void main(String[] args) throws InterruptedException {
        SSHTest test = new SSHTest();

        //if(test.openConnection("127.0.0.1",22, "root","12345", 120000)){
        if(test.openConnection("dlnxin01.wsib.on.ca",22, "uex422","May2017!", 120000)){
            System.out.println("Connected to server");
            test.sendCommand("cd /users/deloitte/uex422 \n");
            test.sendCommand("ls -ltr \n");
            Thread.sleep(3000);
            System.out.println("Result: "+test.recData());
            test.close();
        }else{
            System.out.println("Cannot connect to server \r\n");
        }
    }
}
