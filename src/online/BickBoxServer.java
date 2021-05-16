package online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BickBoxServer implements Runnable{
    private ServerSocket serverSocket;
    private ArrayList<BickBoxServerWorker> workers;

    /***
     * Constructor for server, takes in a port to start up the server socket. Used for accepting clients.
     * @param port
     * @throws IOException
     */
    public BickBoxServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.workers = new ArrayList<>();
    }

    /***
     * Creates a new server worker for every client socket accepted, adds the worker to the list of server workers.
     */
    public void run() {
        System.out.println("Opening server and accepting connections...");
        while(true){
            try{
                Socket cilentSocket = serverSocket.accept();
                BickBoxServerWorker worker = new BickBoxServerWorker(cilentSocket, workers);
                Thread workerThread = new Thread(worker);
                workerThread.start();
                workers.add(worker);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Sets up the server using port.
     * @param args port
     */
    public static void main (String[] args){
        if(args.length != 1){
            System.out.println("Usage: java BickBoxServer port");
            System.exit(0);
        }
        try{
            int port = Integer.parseInt(args[0]);
            BickBoxServer server = new BickBoxServer(port);
            Thread serverThread = new Thread(server);
            serverThread.start();
        } catch (Exception e){
            System.out.println("Please enter a valid port");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
