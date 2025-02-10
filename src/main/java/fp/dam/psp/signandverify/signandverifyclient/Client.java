package fp.dam.psp.signandverify.signandverifyclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Client extends JFrame{

    private Client() {
        super("Firma y verificación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            setContentPane(new MainPanel());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }

}
