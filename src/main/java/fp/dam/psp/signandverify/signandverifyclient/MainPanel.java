package fp.dam.psp.signandverify.signandverifyclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;

public class MainPanel extends JPanel {

    private final KeyStore ks;
    private static final ArrayList<String> aliases = new ArrayList<>();
    private static final String [] hashAlgorithms = {"SHA224", "SHA256", "SHA384", "SHA512", "SHA3-224", "SHA3-256", "SHA3-384", "SHA3-512"};
    private final JComboBox<String> algorithmComboBox;
    private final JComboBox<String> aliasComboBox;
    private final JTextArea info;

    public MainPanel() throws GeneralSecurityException, IOException {
        super(new GridBagLayout());
        ks = KeyStore.getInstance("PKCS12");
        ks.load(ClassLoader.getSystemResourceAsStream("keystore.p12"), "practicas".toCharArray());

        // Almacenar los alias en el ArrayList aliases

        // ********************************************************************************************************************

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        addButton(this::signListener, "sign.png", "Firmar", 0, constraints);
        addSeparator(1, constraints);
        addButton(this::verifyListener, "verify.png", "Verificar", 2, constraints);
        constraints.gridy = 1;
        algorithmComboBox = addComboBox("Algoritmo", hashAlgorithms, 0, constraints, null);
        addSeparator(1, constraints);
        aliasComboBox = addComboBox("Certificado", aliases.toArray(new String[0]), 2, constraints, this::certificateListener);
        info = new JTextArea(30, 80);
        info.setEditable(false);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        add(new JScrollPane(info), constraints);
        certificateListener(null);
    }

    private void addButton(ActionListener listener, String resource, String toolTip, int x, GridBagConstraints c) {
        JButton button = new JButton(new ImageIcon(ClassLoader.getSystemResource(resource)));
        button.setFocusable(false);
        button.addActionListener(listener);
        button.setToolTipText(toolTip);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                button.getBorder()
        ));
        c.gridx = x;
        c.weightx = 1;
        add(button, c);
    }

    private void addSeparator(int x, GridBagConstraints c) {
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 10),
                separator.getBorder()
        ));
        c.gridx = x;
        c.weightx = 0;
        add(separator, c);
    }

    private JComboBox<String> addComboBox(String title, String [] options, int x, GridBagConstraints c, ActionListener listener) {
        c.gridx = x;
        c.weightx = 1;
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 1;
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.DARK_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
        )));
        panel.add(new JLabel(title), constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setFocusable(false);
        if (listener != null)
            comboBox.addActionListener(listener);
        panel.add(comboBox, constraints);
        add(panel, c);
        return comboBox;
    }

    private void certificateListener(ActionEvent e) {
        String alias = (String) aliasComboBox.getSelectedItem();
        if (alias != null)
            try {
                X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
                info.setText(certificate.toString());
                info.setCaretPosition(0);
            } catch (KeyStoreException ex) {
                JOptionPane.showMessageDialog(this, ex.getLocalizedMessage(), "Error al obtener el certificado", JOptionPane.ERROR_MESSAGE);
            }
        else
            JOptionPane.showMessageDialog(this, "No se ha seleccionado ningún certificado", "Error al obtener el certificado", JOptionPane.ERROR_MESSAGE);
    }

    private void signListener(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona el fichero a firmar");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Obtener el alias y el algoritmo seleccionados en los JComboBox aliasComboBox y algorithmComboBox


            // ********************************************************************************************************************

            //  Firmar el fichero seleccionado con el algoritmo seleccionado y el certificado correspondiente al alias seleccionado
            //  y guardar la firma en un fichero con el mismo nombre que el fichero original añadiendo la extensión .signature

            // ********************************************************************************************************************
        }
    }

    private void verifyListener(ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona el fichero a firmar");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            File signatureFile = new File(file.getAbsolutePath() + ".signature");
            // Enviar la firma y el fichero al servidor y mostrar el resultado de la verificación en un JOptionPane

            // ********************************************************************************************************************
        }
    }
}
