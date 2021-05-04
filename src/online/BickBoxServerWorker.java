package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BickBoxServerWorker implements Runnable {
    private Socket cilentSocket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean active;
    public BickBoxServerWorker(Socket cilentSocket) throws IOException {
        this.active = true;
        this.cilentSocket = cilentSocket;
        this.in = new BufferedReader(new InputStreamReader(cilentSocket.getInputStream()));
        this.out = new PrintWriter(cilentSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        while(active) {
            //do stuff like update
            try {
                String input = in.readLine();
                out.print("You said " + input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            //tell client to close
    }

    public void stop(){
        this.active = false;
    }
}
