import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BickFrame extends JFrame implements ActionListener {
    private JTextField chatTextField;
    private JTextArea logTextArea;
    public BickFrame(){
        //create frame
        JFrame frame = new JFrame("Bick Box");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,800);

        //create bottom panel and components - text field and buttons
        JPanel botPanel = new JPanel();

        JTextArea chatTextArea = new JTextArea("Type: ");
        chatTextArea.setEditable(false);
        chatTextArea.setHighlighter(null);

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
        JPanel midPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logTextArea = new JTextArea();

        logTextArea.setEditable(false);
        logTextArea.setHighlighter(null);
        logTextArea.setAlignmentX(LEFT_ALIGNMENT);

        //add to mid panel
        midPanel.add(logTextArea);

        //set up frame
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)(((dimension.getWidth()) - frame.getWidth())/2);
        int y = (int)(((dimension.getHeight()) - frame.getHeight())/2);
        frame.setLocation(x, y);
        frame.getContentPane().add(BorderLayout.SOUTH, botPanel);
        frame.getContentPane().add(BorderLayout.CENTER, midPanel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        if(str.equals("send")){
            String txt = chatTextField.getText();
            if(txt.isBlank()) return;
            chatTextField.setText("");
            logTextArea.append("\nYou:\n"+txt);
        }
    }
}
