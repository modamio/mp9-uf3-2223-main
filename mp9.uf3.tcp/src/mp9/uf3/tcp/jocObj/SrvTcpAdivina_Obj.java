package mp9.uf3.tcp.jocObj;

import mp9.uf3.udp.unicast.joc.SecretNum;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SrvTcpAdivina_Obj {
    /* Servidor TCP que genera un número perquè ClientTcpAdivina_Obj.java jugui a encertar-lo
     * i on la comunicació dels diferents jugadors la gestionaran els Threads : ThreadServidorAdivina_Obj.java
     * */

    private int port;
    private SecretNum ns;
    private Tauler t;


    public Tauler getT() {
        return t;
    }

    public void setT(Tauler t) {
        this.t = t;
    }

    private SrvTcpAdivina_Obj(int port, Tauler tauler ) {
        this.port = port;
        ns = new SecretNum(100);
        t = new Tauler();
    }

    private void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació
                //sumem 1 al numero de jugadors
                t.addNUmPlayers();
                ThreadSevidorAdivina_Obj FilServidor = new ThreadSevidorAdivina_Obj(clientSocket, ns, t);
                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(SrvTcpAdivina_Obj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException {
        Tauler tauler = new Tauler();
        SrvTcpAdivina_Obj srv = new SrvTcpAdivina_Obj(5558,tauler);
        Thread thread = new Thread(()->srv.listen());
        thread.start();
        MulticastSocketServer multicastSocketServer = new MulticastSocketServer(5556,"224.0.0.12",tauler);
        multicastSocketServer.runServer();
    }
}
