package mp9.uf3.tcp.jocLlista;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient extends Thread {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private boolean continueConnected;
    static Llista llista;

    private TCPClient(String hostname, int port, Llista llista) {
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        continueConnected = true;
        this.llista=llista;
    }

    public void run() {
        while(continueConnected) {
            try {
                ObjectOutputStream outtt = new ObjectOutputStream(out);
                outtt.writeObject(llista);
                outtt.flush();
                this.llista=getRequest();
                break;
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        close(socket);

    }
    private Llista getRequest() {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            llista = (Llista) ois.readObject();
            System.out.println(llista.llistatoString());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return llista;
    }


    private void close(Socket socket){
        try {
            //tancament de tots els recursos
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        String jugador;

        Scanner sc = new Scanner(System.in);
        System.out.println("Introduiex el teu nom:");
        jugador = sc.next();
        List<Integer> lista = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lista.add(ThreadLocalRandom.current().nextInt(1, 100));
        }
        Llista llista = new Llista(jugador,lista);

        TCPClient tcpClient = new TCPClient("localhost",5566,llista);
        tcpClient.llista.setNom(jugador);
        tcpClient.start();
    }
}
