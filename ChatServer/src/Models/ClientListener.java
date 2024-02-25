package Models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientListener extends Client implements Runnable{
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientListener(Socket socket){
        this.id = Client.idCurrent++;
        this.socket = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        } catch (Exception e) {
        }
    }
    
    public void sendMessage(String command){
        this.writer.println(command);
    }
    
    @Override
    public void run() {
        while(true){
            try {
                String in = reader.readLine();
                System.out.println("[Received] " +in);
                String function = in.split("\\#")[0];
                switch(function){
                    case "reload":{
                        this.sendMessage(Services.gI().getCommandOfAllRoom());
                        break;
                    }
                    case "name":{
                        this.name = in.split("\\#")[1].strip();
                        this.sendMessage(Services.gI().getCommandOfAllRoom());
                        break;
                    }
                    case "create_room":{
                        Room r = new Room();
                        r.members.add(this);
                        Services.gI().rooms.add(r);
                        this.sendMessage("join_room#null");
                        r.joinRoom(this);
                        Services.gI().updateListRoomGlobal();
                        break;
                    }
                    case "join_room" :{
                        int idRoom = Integer.parseInt(in.split("\\#")[1]);
                        Room r = Services.gI().getRoomByID(idRoom);
                        if(this.room == null){
                            r.joinRoom(this);
                        }
                        this.sendMessage("join_room#null");
                        break;
                    }
                    case "message":{
                        String message = in.split("\\#")[1];
                        this.room.chat(this, message);
                        break;
                    }
                    case "leave_room":{
                        if(this.room != null){
                            this.room.leaveRoom(this);
                            this.room = null;
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

}