import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Panel;
import java.awt.GridLayout;
import javax.swing.*;

public class Vocaber
{
    static List<Translation> words = new ArrayList<Translation>();
    static int randomIndex = 0;

    public static Translation getWord(int index) {
        return words.get(index);
    }

    public static void removeWord(int index) {
        words.remove(index);
    }

    private static boolean loadWords(String fileName)
    {
        File inputFile = new File(fileName);

        try {
            Scanner in = new Scanner(inputFile);
            while (in.hasNextLine())
            {
                // Split line by |
                String trans[] = in.nextLine().split("\\|");;
                words.add(new Translation(trans[0], trans[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        for (String s: args)
        {
            if (s.length() > 0)
            {
                System.out.println("File name: "+s);
                if (loadWords(s))
                    System.out.println("Successfully loaded file");
                else
                    System.out.println("Failed loading file");
            }
        }

        Translation word = getWord(1);
        System.out.println(word.original + "  -  " + word.foreign);

        // Start the application
        JFrame.setDefaultLookAndFeelDecorated(true);

        VocabGUI frame = new VocabGUI();
        //frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Inner classes

    private static class Translation
    {
        String original;
        String foreign;

        public Translation(String original, String foreign)
        {
            this.original = original;
            this.foreign = foreign;
        }
    }

    public static class VocabGUI extends JFrame
    {
        private static final int FRAME_WIDTH = 450;
        private static final int FRAME_HEIGHT = 140;

        public VocabGUI()
        {
            createPanel();
            setSize(FRAME_WIDTH, FRAME_HEIGHT);
            setTitle("Vocaber");
        }

        private void createPanel()
        {
            Panel panel = new Panel();
            panel.setLayout(new GridLayout(0,2));

            JLabel welcomeLabel = new JLabel("Welcome");
            panel.add(welcomeLabel);
            add(panel);
        }
    }

}
