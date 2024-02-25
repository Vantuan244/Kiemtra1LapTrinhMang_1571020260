package chatclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Session {
    private final static Session session = new Session();
    private Session(){
        
    }
    
    public static Session gI(){
        return session;
    }
    
    public Socket socket;
    public BufferedReader reader;
    public PrintWriter writer;
    
    public void set(Socket socket){
        this.socket = socket;
        if(socket != null){
            try {
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            } catch (Exception e) {
            }
        }
    }
    
    public void sendCommand(String command){
        this.writer.println(command);
        System.out.println("[Send] " +command);
    }
    
}
