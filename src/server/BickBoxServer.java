package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class BickBoxServer implements Runnable{
    private ServerSocket serverSocket;
    private ArrayList<BickBoxServerWorker> workers;
    private HashSet<String> users;

    /***
     * Constructor for server, takes in a port to start up the server socket. Creates hashset for database of users.
     * Used for accepting clients.
     * @param port
     * @throws IOException
     */
    public BickBoxServer(int port) throws IOException {
        this.users = new HashSet<>();
        this.serverSocket = new ServerSocket(port);
        this.workers = new ArrayList<>();
    }

    /***
     * Gets list of server workers
     * @return workers
     */
    public ArrayList<BickBoxServerWorker> getWorkers(){
        return this.workers;
    }

    public boolean checkUser(String username){
        if(users.contains(username)){
            return false;
        } else {
            users.add(username);
            return true;
        }
    }

    public void userLeave(BickBoxServerWorker worker){
        users.remove(worker.getUsername());
        workers.remove(worker);
    }
    /***
     * Creates a new server worker for every client socket accepted, adds the worker to the list of server workers.
     */
    public void run() {
        System.out.println("Opening server and accepting connections...");
        while(true){
            try{
                Socket cilentSocket = serverSocket.accept();
                BickBoxServerWorker worker = new BickBoxServerWorker(cilentSocket, this);
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
