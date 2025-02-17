package fp.dam.psp.signandverify.verifyserver;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Base64;

public class RequestHandler implements Runnable {

    private final Socket socket;

    public RequestHandler(Socket socket) throws SocketException {
        this.socket = socket;
        socket.setSoTimeout(10000);
    }

    @Override
    public void run() {
        try (socket) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String [] signatureData = in.readUTF().split("#");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] fileSignature = decoder.decode(signatureData[0]);
            String hashAlgorithm = signatureData[1];
            byte[] certEncoded = decoder.decode(signatureData[2]);
            ByteArrayInputStream certIn = new ByteArrayInputStream(certEncoded);
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(certIn);
            Signature signature = Signature.getInstance(hashAlgorithm + "with" + certificate.getPublicKey().getAlgorithm());
            signature.initVerify(certificate.getPublicKey());
            byte [] buffer = new byte[1024];
            int n;
            while ((n = in.read(buffer)) != -1)
                signature.update(buffer, 0, n);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            if (signature.verify(fileSignature)) {
                System.out.println("firma verificada" + " : " +
                        socket.getInetAddress() + " : " + LocalDateTime.now());
                out.writeUTF("firma verificada");
            }
            else {
                System.out.println("firma no verificada" + " : " +
                        socket.getInetAddress() + " : " + LocalDateTime.now());
                out.writeUTF("firma no verificada");
            }

        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Error: " + e.getLocalizedMessage() + " : " +
                    socket.getInetAddress() + " : " + LocalDateTime.now());
        }
    }
}
