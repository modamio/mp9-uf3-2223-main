package mp9.uf3.tcp.jocLlista;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ThreadTasca implements Runnable {
    /* Thread que gestiona la comunicació de SrvTcPAdivina_Obj.java i un cllient ClientTcpAdivina_Obj.java */

    private Socket clientSocket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private boolean seguent;
    private Llista llista;

    public ThreadTasca(Socket clientSocket)throws IOException {
        this.clientSocket = clientSocket;
        seguent = false;

        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
        System.out.println("canals i/o creats amb un nou jugador");
    }

    @Override
    public void run() {
        try {
            while(!seguent) {
                ObjectInputStream ois = new ObjectInputStream(in);
                try {
                    llista = (Llista) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("jugada: " + llista.getNom() + "->" + llista.getNumberList());
                Set<Integer> set = new HashSet<>();
                set.addAll(llista.getNumberList());
                llista.setNumberList(set.stream().toList());
                seguent=true;
            }
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(llista);
            oos.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
