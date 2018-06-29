import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.AttributedString;
import java.util.ArrayList;

public class Aligning extends JFrame implements ActionListener {
    JPanel mainPanel;
    JTextField getText;
    String fileName;
    String sentence;
    Rectangle r = new Rectangle().getBounds();
    double h = r.height;
    double w = r.width;
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
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            ArrayList<Integer> positions=new ArrayList<>();

            for(int i=0;i<sent.length();i++) {
                if(sent.charAt(i)==' ')
                    positions.add(i+1);
            }
            for(Integer k:positions){
                System.out.println("Position of space: "+k);
            }

            String[] parts = sent.split(" ");

            int numberOfWords = parts.length;
            int result = 0;
            int x = 10;
            int y = 20;
            int spaceNumber=positions.get(0);
            int j=1;
            StringBuilder sb = new StringBuilder(sent);
            for (int i = 0; i < numberOfWords - 1; i++) {
                if (result < DEFAULT_WIDTH - 40) {
                    // TODO: 28.06.2018 this statement helps to fit the text in the frame; i'll maybe do set it to dynamically change when window size changes
                    g2.drawString(parts[i], x, y);
                    result += g2.getFontMetrics().stringWidth(parts[i] + g2.getFontMetrics().charWidth(' '));
                    x += g2.getFontMetrics().stringWidth(parts[i]) + g2.getFontMetrics().charWidth(' ');
                    System.out.println(result);
                } else {
                    // TODO: 28.06.2018 Find out how to justify the text
                    while(result>re) {//result!=DEFAULT_WIDTH-40
                        System.out.println("Result: " + result);
                        if (j > 10)
                            break;
                        sb.insert(spaceNumber, "  ");
                        spaceNumber = positions.get(j - 1);
                        j++;

                    }
                    while(result!=DEFAULT_WIDTH-40) {
                        x+=g2.getFontMetrics().stringWidth((parts[i]+g2.getFontMetrics().charWidth(' ')));
                    }
                    result = 0;
                    y += g2.getFontMetrics().getHeight();
                    x = 10;

                }
            }
        }
    }
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new Aligning());
    }
}
