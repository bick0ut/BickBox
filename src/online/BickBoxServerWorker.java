package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import static online.Protocol.NEW_MESSAGE;

public class BickBoxServerWorker implements Runnable {
    private Socket cilentSocket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean active;
    private String username;

    /***
     * Constructor for the server worker, takes in a clientSocket in order to read and write. Used for handling clients.
     * @param cilentSocket
     * @throws IOException
     */
    public BickBoxServerWorker(Socket cilentSocket) throws IOException {
        this.active = true;
        this.cilentSocket = cilentSocket;
        this.in = new BufferedReader(new InputStreamReader(cilentSocket.getInputStream()));
        this.out = new PrintWriter(cilentSocket.getOutputStream(), true);
        this.username = "TestUser123";
    }

    /***
     * Run and responds to client as long as server is active
     */
    @Override
    public void run() {
        while(active && cilentSocket.isConnected()) {
            //do stuff like update
            try {
                String input = in.readLine();
                out.println(NEW_MESSAGE + username + ": " + input);
            } catch (IOException e) {
                stop();
            }
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
