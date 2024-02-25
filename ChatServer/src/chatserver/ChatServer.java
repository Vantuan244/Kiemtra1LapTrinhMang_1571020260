package chatserver;

import Models.Services;

public class ChatServer {
    public static void main(String[] args){
        Services.gI().startSocket();
    }  
}
