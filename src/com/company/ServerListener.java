package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Student08 on 19.05.2015.
 */
public class ServerListener extends Thread{

    String messageFromServer;
    String login;
    ObjectInputStream is;
    Packet p;
    private String status;

    public ServerListener (ObjectInputStream is, String login){
        this.is = is;
        this.login = login;
        start();


    }

    @Override
    public void run(){


        while (true){

            try {
                p = (Packet) is.readObject();
                if (p != null){

                    if (p.getSender().equals(login)){ messageFromServer = null;
                    }else messageFromServer = p.getMessage();
                    if (p.getReceiver() == null || p.getReceiver().equals("all")) {status = "";} else status = "private";
                }

                if (messageFromServer != null) {
                    outputToUser(p, status, messageFromServer);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }


    }

    private void outputToUser(Packet p, String status, String messageFromServer) {

        if (status.equals("private")) {
            System.out.println(p.getSender() + " (" + status + "): " + messageFromServer);
        }else {
            System.out.println(p.getSender() + ": " + messageFromServer);

        }

    }
}
