package sample;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

public class Client extends Observable implements Runnable {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private boolean connected;
    private int port = 5555; //default port
    private String hostName = "localhost";
    private Label feedbackLabel;
    private StringProperty feedback;
    public Client(Label feedbackLabel) {
        connected = false;
        feedback = new SimpleStringProperty();
        feedbackLabel.textProperty().bindBidirectional(feedback);
    }

    public void connect(String hostName, int port) throws IOException {
        if (!connected) {
            this.hostName = hostName;
            this.port = port;
            this.feedbackLabel = feedbackLabel;
            socket = new Socket(hostName, port);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
           // feedbackLabel.setText("Connected to server");
            Thread t = new Thread(this);
            t.start();
        }
    }

    public void sendMessage(String msg) throws IOException {
        if (connected) {
            pw.println(msg);
        } else throw new IOException("Not connected to server");
    }

    public void disconnect() {
        if (socket != null && connected) {
            try {
                socket.close();
            } catch (IOException ioe) {

            } finally {
                this.connected = false;
            }
        }
    }

    @Override
    public void run() {
        String msg = "";
        try {
            while (connected && (msg = br.readLine()) != null) {
                System.out.println("Server: " + msg);
                feedback.setValue("Server: "+msg);
                this.setChanged();
                this.notifyObservers(msg);
            }
        } catch (IOException ioe) {

        } finally {
            connected = false;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public int getPort(){
        return port;
    }

    public void setPort(int port){
        this.port = port;
    }

    public String getHostName(){
        return hostName;
    }

    public void setHostName(String hostName){
        this.hostName = hostName;
    }
}
