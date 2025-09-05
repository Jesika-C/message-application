
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class ClientForm extends JFrame {
    static JFrame client;
    private JTextField messageField;
    private JLabel label;
    private JTextArea chatArea;
    private JButton send;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Border blackline;
    private Border raisedbevel;

    public ClientForm() throws IOException {
        createUIComponents();
        Socket socket = new Socket("localHost", 1234);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    } // end of ClientForm


    private void createUIComponents() {
        client = new JFrame("Client");
        JPanel p = new JPanel();
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.setBackground(Color.gray);
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        blackline = BorderFactory.createLineBorder(Color.BLACK);
        raisedbevel = BorderFactory.createRaisedBevelBorder();

        // creates the area to display messages
        chatArea = new JTextArea(6,22);
        chatArea.setBorder(blackline);
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
        label.setForeground(Color.black);
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

        // Send message with button send
        send.addActionListener(event -> {
            if (!messageField.getText().isEmpty()) { // makes sure text box isn't empty
                String msg = messageField.getText();
                messageField.requestFocus();

                SendMessage(msg);
                messageField.setText("");
            }
        });

        client.add(p);
        client.setSize(350,250);
        client.setVisible(true);

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
                chatArea.append("Server: " + msg + "\n");
            } catch (Exception e){

            }
        }
    } // end of ReceiveMessage

    public static void main(String[] args) throws IOException {
        ClientForm client = new ClientForm();
        client.ReceiveMessage();
    } // end of main


}
