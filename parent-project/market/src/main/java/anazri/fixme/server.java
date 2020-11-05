package anazri.fixme;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class server extends JFrame{
    public static void main(String[] args) throws IOException{

        private JTextField userText;
        private JTextArea chatWindow;
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private ServerSocket serverSocket;
        private Socket socket = serverSocket.accept();
    
        //constructor
        public server(){
            super("Fix Messenger");
            userText = new JTextField();
            userText.setEditable(false); //By default before being connected you aren't allowed to type in IM box
            userText.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent event){
                        sendMessage(event.getActionCommand());
                        userText.setText("");
                    }
                }
            ); 
            add(userText, BorderLayout.NORTH);
            chatWindow = new JTextArea();
            add(new JScrollPane(chatWindow));
            setSize(400, 200);
            setVisible(true);
        }
        //set up and run server
        public void startRunning(){
            try{
                serverSocket = new ServerSocket(5000, 100); //100 = backlog for num of people waiting in port 5000 to chat
                while(true){
                    try{
                        waitForConnection();
                    }catch(EOFException eofException){
                        //EOF = end of connection
                        showMessage("\nServer connection ended.")
                    }finally{
                        closeAll();
                    }
                }
            }catch(IOException ioException){
                ioexception.printStackTrace();
            }
        }  
    }