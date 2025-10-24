package chat_sockets.domain;

import java.io.DataOutputStream;
import java.net.Socket;

public class Customer implements Runnable{

    private int port;
    private String message;


    public Customer(int port, String message) {
        this.port = port;
        this.message = message;
    }


    @Override
    public void run() {

        //Host del servidor
        final String HOST = "127.0.0.1";

        //Puerto del servidor
        DataOutputStream outputStream;

        try {
            Socket socket = new Socket(HOST, port);
            outputStream = new DataOutputStream(socket.getOutputStream());

            //Enviamos el mensaje
            outputStream.writeUTF(message);

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }


    }
}
