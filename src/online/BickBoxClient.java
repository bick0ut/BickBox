package online;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class BickBoxClient extends JFrame implements ActionListener {
    private JTextField chatTextField;
    private JTextArea logTextArea;
    private Socket cilentSocket;
    private BufferedReader in;
    private PrintWriter out;

    /***
     * Constructor for client, creates all of the GUI using AWT and Swing. Creates a client worker.
     * @param hostname
     * @param port
     * @throws IOException
     */
    public BickBoxClient(String username, String hostname, int port) throws IOException {
        //set up sockets and in/outs + worker thread
        this.cilentSocket = new Socket(hostname, port);
        this.in = new BufferedReader(new InputStreamReader(cilentSocket.getInputStream()));
        this.out = new PrintWriter(cilentSocket.getOutputStream(), true);


        //create frame
        JFrame frame = new JFrame("Bick Box");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,800);

        //create bottom panel and components - text field and buttons
        JPanel botPanel = new JPanel();

        JTextArea chatTextArea = new JTextArea("Type: ");
        chatTextArea.setEditable(false);
        chatTextArea.setHighlighter(null);
        chatTextArea.setBackground(null);

        chatTextField = new JTextField(30);
        chatTextField.setAlignmentX(JTextField.LEADING);
        chatTextField.addActionListener(this);
        chatTextField.setActionCommand("send");

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        sendButton.setActionCommand("send");

        //add to bottom panel
        botPanel.add(chatTextArea);
        botPanel.add(chatTextField);
        botPanel.add(sendButton);

        //create middle panel and components - chat log
        JPanel midPanel = new JPanel();
        logTextArea = new JTextArea();

        logTextArea.setEditable(false);
        logTextArea.setHighlighter(null);
        logTextArea.setAlignmentX(LEFT_ALIGNMENT);
        logTextArea.setBackground(null);

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setPreferredSize(new Dimension(580, 680));

        //add to middle panel
        midPanel.add(scrollPane);

        //create top panel and components - top label
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextArea topTextArea = new JTextArea("Chat");
        topTextArea.setBackground(null);
        topTextArea.setHighlighter(null);
        topTextArea.setEditable(false);

        //add to top panel
        topPanel.add(topTextArea);

        //create worker thread and start it
        BickBoxClientWorker worker = new BickBoxClientWorker(in, logTextArea);
        Thread workerThread = new Thread(worker);
        workerThread.start();
        out.println(username);

        //set up frame
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)(((dimension.getWidth()) - frame.getWidth())/2);
        int y = (int)(((dimension.getHeight()) - frame.getHeight())/2);
        frame.setLocation(x, y);
        frame.getContentPane().add(BorderLayout.SOUTH, botPanel);
        frame.getContentPane().add(BorderLayout.CENTER, midPanel);
        frame.getContentPane().add(BorderLayout.NORTH, topPanel);
        frame.setVisible(true);
        frame.setResizable(false);
        System.out.println("Successfully connected.");
    }

    /***
     * Determines what happens when an action is performed (eg. button click)
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if(str.equals("send")){
            String txt = chatTextField.getText();
            if(txt.isBlank()) return;
            chatTextField.setText("");
            out.println(txt);
        }
    }

    /***
     * Initializes a client and runs it
     * @param args username hostname port
     */
    public static void main(String[] args){
        if(args.length != 3){
            System.out.println("Usage: java BickBoxCilent username hostname port");
            System.exit(0);
        }

        try {
            int port = Integer.parseInt(args[2]);
            BickBoxClient bickFrameClient = new BickBoxClient(args[0], args[1], port);
        } catch (ConnectException e){
            System.out.println("Connection timed out!");
            System.exit(0);
        } catch (NumberFormatException e){
            System.out.println("Please enter a valid port");
            System.exit(0);
        } catch (Exception e){
            System.out.println("Please contact me");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
