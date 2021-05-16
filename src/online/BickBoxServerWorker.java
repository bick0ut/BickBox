package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Array;
import java.util.ArrayList;

import static online.Protocol.*;

public class BickBoxServerWorker implements Runnable {
    private Socket cilentSocket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean active;
    private String username;
    private ArrayList<BickBoxServerWorker> workers;

    /***
     * Constructor for the server worker, takes in a clientSocket in order to read and write. Used for handling clients.
     * @param cilentSocket
     * @throws IOException
     */
    public BickBoxServerWorker(Socket cilentSocket, ArrayList<BickBoxServerWorker> workers) throws IOException {
        this.workers = workers;
        this.active = true;
        this.cilentSocket = cilentSocket;
        this.in = new BufferedReader(new InputStreamReader(cilentSocket.getInputStream()));
        this.out = new PrintWriter(cilentSocket.getOutputStream(), true);
        this.username = "";
    }

    /***
     * Gets the print writer
     * @return out
     */
    public PrintWriter getOut(){
        return this.out;
    }

    /***
     * Run and responds to client as long as server is active
     */
    @Override
    public void run() {
        String input;
        try {
            input = in.readLine();
            username = input;
            System.out.println(username + " has connected.");
            //notifies all users of this new user's arrival
            for (BickBoxServerWorker server : workers) {
                server.getOut().println(USER_JOINED + username);
            }
        } catch (IOException e){
            stop();
        }
        while(active && cilentSocket.isConnected()) {
            //do stuff like update
            try {
                input = in.readLine();
                //sends info to every other server worker's writer which will reach the client worker's chat log
                for (BickBoxServerWorker server : workers) {
                    server.getOut().println(NEW_MESSAGE + username + ": " + input);
                }
            } catch (IOException e) {
                stop();
            }
        }
        //notifies all users of this user's departure
        for (BickBoxServerWorker server : workers) {
            server.getOut().println(USER_LEFT + username);
        }
        System.out.println(username + " has disconnected.");
    }

    /***
     * Inactivates server
     */
    public void stop(){
        this.active = false;
    }
}
