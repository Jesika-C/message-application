import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class ServerForm extends JFrame{
    static JFrame server;
    private JLabel label;
    private JTextField messageField;
    private JTextArea chatArea;
    private JButton send;
    private ServerSocket serverSocket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Border blackline;
    private Border raisedbevel;

    public ServerForm() throws IOException {
        createUIComponents();
        serverSocket = new ServerSocket(1234);
        Socket socket = serverSocket.accept();
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    } // end of ServerForm

    private void createUIComponents() {
        server = new JFrame("Server");
        server.setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        blackline = BorderFactory.createLineBorder(Color.BLACK);
        raisedbevel = BorderFactory.createRaisedBevelBorder();

        // creates the area to display messages
        chatArea = new JTextArea(6,22);
        chatArea.setBorder(blackline);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 40;  // height of component
        gbc.gridwidth = 3; // 3 columns wide
        gbc.gridx = 0; // placement on first column
        gbc.gridy = 0; // placement on first row
        p.add(chatArea, gbc);

        // creates scroll for chatArea
        JScrollPane scroll = new JScrollPane(chatArea);
        p.add(scroll);

        // creates a label for messageField with label telling user to enter a message
        label = new JLabel("Enter Message");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.1;
        gbc.ipady = 0; // set back to default
        gbc.gridx = 0; // placement on first column
        gbc.gridy = 1; // placement on second row
        gbc.gridwidth = 1; // 1 column wide

        p.add(label, gbc);

        // creates the field to write messages with 16 columns
        messageField = new JTextField(16);
        messageField.setBorder(blackline);
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.ipady = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; // placement on first column
        gbc.gridy = 2; // placement on third row
        p.add(messageField, gbc);

        // creates button to send messages labeled Send
        send = new JButton("Send");
        send.setBackground(Color.darkGray);
        send.setForeground(Color.white);
        send.setBorder(raisedbevel);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.ipady = 5;
        gbc.gridx = 2; // placement on third column
        gbc.gridy = 2; // placement on third row
        p.add(send, gbc);

        server.add(p, BorderLayout.CENTER);
        server.setSize(350,250);
        server.setVisible(true);

        // Send message with button send
        send.addActionListener(event -> {
            if (!messageField.getText().isEmpty()) { // makes sure text box isn't empty
                String msg = messageField.getText();
                messageField.requestFocus();

                SendMessage(msg);
                messageField.setText("");
            }
        });

    } // end of createUIComponents

    private void SendMessage (String msg){
        try{
            out.writeObject(msg);
            chatArea.append("You: " + msg + "\n");
        } catch (Exception e) {

        }
    } // end of SendMessage

    //get message
    public void ReceiveMessage() {
        String msg;
        while(true){
            try{
                msg = (String) in.readObject();
                chatArea.append("Client: " + msg + "\n");
            } catch (Exception e){

            }
        }
    } // end of ReceiveMessage

    public static void main(String[] args) throws IOException {

        ServerForm server = new ServerForm();
        server.ReceiveMessage();

    } // end of main
}
