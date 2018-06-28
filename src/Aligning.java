import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Aligning extends JFrame implements ActionListener {
    JPanel mainPanel;
    JTextField getText;
    String fileName;
    String sentence;

    int DEFAULT_WIDTH = 640;
    int DEFAULT_HEIGHT = 640;

    Aligning() {
        getFileName();
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void getFileName() {
        getText = new JTextField("Default");
        getText.addActionListener(this);
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(getText, BorderLayout.NORTH);

        add(mainPanel);


    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == getText) {
            fileName = getText.getText();
            try {
                File f = new File(fileName);
                String sentence = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
                System.out.println(sentence);
                mainPanel.add(new drawComponent(sentence));
                /*
                JTextPane textPane = new JTextPane();
                textPane.setText(sentence);
                System.out.println(textPane.getAlignmentX());
                System.out.println(textPane.getAlignmentY());
                mainPanel.add(textPane, BorderLayout.CENTER);
                */
                repaint();
                revalidate();

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "File not found!\n" + e);
                System.exit(0);
            }
        }
    }

    class drawComponent extends JComponent {
        String sent;

        drawComponent(String s) {
            sent = s;
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            FontRenderContext fContext = g2.getFontRenderContext();
            g2.setFont(new Font("Arial",Font.PLAIN,14));
            Font f = g2.getFont();
            Rectangle2D bounds = f.getStringBounds(sent, fContext);
            double stringWidth = bounds.getWidth();
            double stringHeight = bounds.getWidth();
            String[] parts = sent.split(" ");
            int numberOfLines = (int) stringWidth / DEFAULT_WIDTH;
            int numberOfWords=parts.length;
            int result=0;
            int x =0;
            int y = 10;
            for (int i = 0; i < numberOfWords-1; i++) {
                int fMetrics=g2.getFontMetrics().stringWidth(parts[i]);
                    if(result<DEFAULT_WIDTH-30) {
                        g2.drawString(parts[i],x,y);
                        result += g2.getFontMetrics().stringWidth(parts[i] + g2.getFontMetrics().charWidth(' '));
                        x+=g2.getFontMetrics().stringWidth(parts[i])+g2.getFontMetrics().charWidth(' ');
                        System.out.println(result);
                    }
                    else {result=0;x=0;  y+=g2.getFontMetrics().getHeight();}
            }
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new Aligning());
    }
}
