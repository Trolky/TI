import javax.imageio.ImageIO;
import javax.swing.*;
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
    public static JTextArea textArea;
    public static JTextField textField;
    public static Machine automat = new Machine();
    public static String input = "";

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
        BufferedImage img = ImageIO.read(new File("currentStates/A.PNG"));

        textArea = new JTextArea("Zadej symbol (nebo 'z' pro krok zpět, 'r' pro restart, 'k' pro ukončení): ");
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(1000,0,400,600);

        textField = new JTextField();
        textField.setBounds(1000,600,395,50);
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
            public void actionPerformed(ActionEvent e) {
                input = ((JTextField) e.getSource()).getText();
                switch (input) {
                    case "k":
                        win.dispatchEvent(new WindowEvent(win,WindowEvent.WINDOW_CLOSING));
                        break;
                    case "r":
                        automat.reset();
                        break;
                    case "z":
                        automat.stepBack();
                        break;
                    default:
                        automat.performTransition(input);
                        break;
                }

                //Zobrazení nového obrázku
                BufferedImage newImage;
                try {
                    newImage = ImageIO.read(new File("currentStates/"+automat.getCurrentState().toUpperCase()+".PNG"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                panel.setImg(newImage);
                panel.repaint();

                textArea.setText(textArea.getText()+"\n"+"Aktuální stav: " + automat.getCurrentState());
                textArea.setText(textArea.getText()+"\n"+"Aktuální stav: "+ "Posloupnost přechodů: " + automat.getHistoryStack() + "\n");


                if (automat.isAcceptingState()) {
                    textArea.setText(textArea.getText()+"\n"+"Automat dosáhl přijímajícího stavu!\n");

                }
                textField.setText("");
                textArea.setText(textArea.getText()+"\n"+"Zadej symbol (nebo 'z' pro krok zpět, 'r' pro restart, 'k' pro ukončení): ");
            }
        };
    }
}


