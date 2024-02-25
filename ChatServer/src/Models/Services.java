package Models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Services {
    private final static Services services = new Services();
    public static Services gI(){
        return services;
    }
    
    private final int port = 5555;
    public ArrayList<Room> rooms = new ArrayList<>();
    public ArrayList<ClientListener> clients = new ArrayList<>();
    
    public Room getRoomByID(int id){
        for(Room room : rooms){
            if(room.id == id){
                return room;
            }
        }
        return null;
    }
    
    public String getCommandOfAllRoom(){
        if(rooms.isEmpty()) return "rooms#null";
        String x = "rooms#";
        for(Room r: rooms){
            x += String.format("%d|%d", r.id, r.members.size()) + ",";
        }
        x = x.substring(0, x.length() - 1);
        return x;
    }
    
    public void tellGlobal(String command){
        this.clients.forEach(client->{
            client.sendMessage(command);
        });
    }
    
    public void updateListRoomGlobal(){
        Services.gI().tellGlobal(Services.gI().getCommandOfAllRoom());
    }
    
    public void startSocket(){
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("[ServerSocket] Start at " + port);
            while(true){
                Socket socket = ss.accept();
                ClientListener client = new ClientListener(socket);
                Thread threadClient = new Thread(client);
                threadClient.start();
                this.clients.add(client);
            }
        } catch (Exception e) {
        }
    }
}
