package Models;

import java.util.ArrayList;

public class Room {
    public static int idCurrent = 0;
    public int id; 
    public ArrayList<ClientListener> members = new ArrayList<>();
    
    public Room(){
        id = Room.idCurrent++;
    }
    
    public void tellEveryone(String command){
        System.out.println("[CHAT] Room " + id + " - Content " + command);
        for(ClientListener client: members){
            client.sendMessage(command);
        }
    }
    
    public void chat(ClientListener clientChat, String message){
        String command = "message#" + clientChat.name + " : " + message;
        tellEveryone(command);
    }
    
    public void leaveRoom(ClientListener client){
        String nameClient = client.name;
        this.members.remove(client);
        tellEveryone("message#[Server]: "+nameClient + " đã rời khỏi cuộc trò chuyện.");
        Services.gI().updateListRoomGlobal();
    }
    
    public void joinRoom(ClientListener client){
        String nameClient = client.name;
        client.room = this;
        if(!members.contains(client))
            this.members.add(client);
        tellEveryone("message#[Server]: "+nameClient + " đã tham gia cuộc trò chuyện.");
        Services.gI().updateListRoomGlobal();
    }
}
