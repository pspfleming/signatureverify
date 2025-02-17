package fp.dam.psp.signandverify.verifyserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException {
        ExecutorService service = Executors.newFixedThreadPool(50);
        ServerSocket serverSocket = new ServerSocket(9000);
        System.out.println("Servidor de verificación de firmas escuchando en puerto 9000");
        while (true) {
            service.submit(new RequestHandler(serverSocket.accept()));
        }
    }

}
