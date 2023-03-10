package mp9.uf3.tcp.jocLlista;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {
    /* Servidor TCP que genera un número perquè ClientTcpAdivina_Obj.java jugui a encertar-lo
     * i on la comunicació dels diferents jugadors la gestionaran els Threads : ThreadServidorAdivina_Obj.java
     * */

    private int port;

    private TCPServer(int port ) {
        this.port = port;
    }

    private void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                ThreadTasca FilServidor = new ThreadTasca(clientSocket);
                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(5566);
        tcpServer.listen();
    }
}

