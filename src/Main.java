import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static DrawingPanel panel;
    public static JFrame win;
    public static JTextPane textPane;
    public static JTextField textField;
    public static Machine automat = new Machine();
    public static String input = "";
    public static int length;

    public static void main(String[] args) throws IOException {
        createWindow();

    }

    public static void createWindow() throws IOException {
        win = new JFrame();
        win.setTitle("Current state");

        makeGui(win);
        win.pack();

        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setLocationRelativeTo(null);
        win.setResizable(false);
        win.setVisible(true);
    }

    public static void makeGui(JFrame win) throws IOException {
        BufferedImage img = ImageIO.read(new File("currentStates/A.jpg"));

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText("Zadej symbol (nebo 'z' pro krok zpět, 'r' pro restart, 'k' pro ukončení): ");

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(951,0,450,600);

        textField = new JTextField();
        textField.setBounds(951,600,445,50);
        textField.addActionListener(runProgram(automat));

        panel = new DrawingPanel(img);
        panel.setLayout(null);
        panel.add(scrollPane);
        panel.add(textField);

        win.add(panel);
    }

    public static ActionListener runProgram(Machine automat){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                input = ((JTextField) e.getSource()).getText();
                length = textPane.getStyledDocument().getLength();
                switch (input) {
                    case "k":
                        win.dispatchEvent(new WindowEvent(win,WindowEvent.WINDOW_CLOSING));
                        break;
                    case "r":
                        textPane.setText("");
                        length = 0;
                        automat.reset();
                        break;
                    case "z":
                        try {
                            automat.stepBack();
                        } catch (BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    default:
                        try {
                            automat.performTransition(input);
                        } catch (BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                }

                //Zobrazení nového obrázku
                BufferedImage newImage;
                try {
                    newImage = ImageIO.read(new File("currentStates/"+automat.getCurrentState().toUpperCase()+".jpg"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                panel.setImg(newImage);
                panel.repaint();

                try {
                    if(!input.equals("r")) {
                        appendText("\n" + "Aktuální stav: " + automat.getCurrentState(),null);
                        if (automat.isAcceptingState()) {
                            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
                            StyleConstants.setBold(attributeSet, true);

                            appendText("\n" + "Aktuální stav: " + "Posloupnost přechodů: " + automat.getHistoryStack(),null);
                            appendText("\n" + "Automat dosáhl přijímajícího stavu!\n",attributeSet);
                        }
                        appendText("\n" + "Aktuální stav: " + "Posloupnost přechodů: " + automat.getHistoryStack()+"\n",null);
                        appendText("\nZadej symbol (nebo 'z' pro krok zpět, 'r' pro restart, 'k' pro ukončení): ",null);
                    }

                    else appendText("Zadej symbol (nebo 'z' pro krok zpět, 'r' pro restart, 'k' pro ukončení): ",null);

                    textField.setText("");
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
                textPane.setCaretPosition(textPane.getDocument().getLength());
            }
        };
    }

    public static void appendText(String text, SimpleAttributeSet attributeSet) throws BadLocationException {
        textPane.getStyledDocument().insertString(length,text,attributeSet);
        length = textPane.getStyledDocument().getLength();
    }
}


