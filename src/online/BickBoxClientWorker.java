package online;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static online.Protocol.*;

public class BickBoxClientWorker implements Runnable {
    private JTextArea logTextArea;
    private BufferedReader in;
    private boolean active;

    /***
     * Constructor for client worker, takes in a JTextArea to update the chat log, and updates it with info retrieved
     * from the server using the reader.
     */
    public BickBoxClientWorker(BufferedReader in, JTextArea logTextArea) throws IOException {
        this.active = true;
        this.logTextArea = logTextArea;
        this.in = in;
    }

    /***
     * Run and reads from server then updates the chat log text area as long as client is active.
     */
    @Override
    public void run() {
        while(active){
            try {
                String msg = in.readLine();
                String protocol = msg.substring(0, 1);

                //formats the message to include username if the protocol indicates a new message
                if(protocol.equals(NEW_MESSAGE)){
                    //first string is PUsername, removes the P (protocol)
                    String username = msg.split(" ")[0].substring(1);

                    int msgStart = username.length() + 2;
                    logTextArea.append(username+"\n" + msg.substring(msgStart) + "\n\n");
                } else if (protocol.equals(USER_JOINED)){ //new user has joined, only need one line
                    String username = msg.split(" ")[0].substring(1);
                    logTextArea.append(username+" has joined the chat!" + "\n\n");
                } else if (protocol.equals(USER_LEFT)){ //user has left, only need one line
                    String username = msg.split(" ")[0].substring(1);
                    logTextArea.append(username+" has left the chat!" + "\n\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Inactivates server
     */
    public void stop(){
        this.active = false;
    }
}
