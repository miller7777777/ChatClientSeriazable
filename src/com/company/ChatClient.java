package com.company;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Student08 on 19.05.2015.
 */
public class ChatClient {

    private static final int PORT = 7000;
    private static final String IP = "localhost";
    private Socket socket;
    Scanner scanner;
    String messageToServer;
    String consoleMessage;
    private String login;
    private String receiver;
    private ServerListener listener;
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private Packet pToServer;

    public void connect(){

        try {
            socket = new Socket(IP, PORT);
            os = new ObjectOutputStream(socket.getOutputStream());
            os.flush();
            is = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        scanner = new Scanner(System.in);

        System.out.println("Input login:");

        login = scanner.nextLine();
        pToServer = new Packet(login,null,null);

        sendToServer(pToServer);

        listener = new ServerListener(is, login);
        consoleReader();

    }

    private void consoleReader(){
        //считываем сообщения с консоли и отправляем на сервер




        System.out.println("Ready to messaging");



        try {
            while (true){


                Scanner scanner = new Scanner(System.in);

                consoleMessage = scanner.nextLine();
//


                // Разбор введенной строки.
                int atDog = consoleMessage.indexOf("@");
                if (atDog == 0) {
                    int doubleDot = consoleMessage.indexOf(":");
                    receiver  = consoleMessage.substring(atDog + 1, doubleDot);
                    messageToServer = consoleMessage.substring(doubleDot + 1);
                }else {
                    receiver = null;
                    messageToServer = consoleMessage;
                }






                pToServer = new Packet(login, receiver, messageToServer);
                sendToServer(pToServer);

                if (messageToServer.equals("exit")){
                    scanner.close();
                    close();
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToServer(Packet pToServer) {

        try {
            os.writeObject(pToServer);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() throws IOException{

        is.close();
        listener.stop();
        os.close();
        socket.close();

    }
}
