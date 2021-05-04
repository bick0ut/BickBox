package online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BickBoxServer {
    private ServerSocket serverSocket;
    private ArrayList<Runnable> workers;
    public BickBoxServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.workers = new ArrayList<>();
    }

    public void go() {
        System.out.println("Opening server and accepting connections...");
        while(true){
            try{
                Socket cilentSocket = serverSocket.accept();
                BickBoxServerWorker worker = new BickBoxServerWorker(cilentSocket);
                workers.add(worker);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main (String[] args){
        if(args.length != 1){
            System.out.println("Usage: java BickBoxServer port");
            System.exit(0);
        }
        int port;
        try{
            port = Integer.parseInt(args[0]);
            BickBoxServer server = new BickBoxServer(port);
            server.go();
        } catch (Exception e){
            System.out.println("Please enter a valid port");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
