import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.Panel;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

public class Vocaber
{
    static final boolean ENABLE_DEBUG = true;

    static List<Translation> words = new ArrayList<Translation>();
    static int randomIndex = 0;
    static VocabGUI gui;

    public static void knowTranslation()
    {
        // Delete known word and show new one
        debug("Know Word");
        showTranslationAnswer();
        removeWord(randomIndex);
        if (words.size() > 0) {
            showRandomTranslation();
        } else {
            JOptionPane.showMessageDialog(null, "Congrats\n"
                    + "you know Everything.");
            System.exit(1);
        }
    }

    public static void dontKnowTranslation()
    {
        // Show new word
        debug("Don't Know Word");
        showTranslationAnswer();
        showRandomTranslation();
    }

    public static void showTranslationAnswer()
    {
        Translation answer = getWord(randomIndex);
        gui.lastTestedWordLabel.setText(answer.original);
        gui.answerTestedWordLabel.setText(answer.foreign);
    }

    public static void showRandomTranslation()
    {
        // Generate random int
        Random gen = new Random();
        randomIndex = gen.nextInt(words.size());
        debug("Random Int: "+Integer.toString(randomIndex));
        Translation newWord = getWord(randomIndex);
        gui.testedWordLabel.setText(newWord.original);
    }

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

    // Debug method
    private static void debug(String statement)
    {
        if (ENABLE_DEBUG)
            System.out.println(statement);
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        for (String s: args)
        {
            if (s.length() > 0)
            {
                debug("File name: "+s);
                if (loadWords(s))
                    debug("Successfully loaded file");
                else
                    debug("Failed loading file");
            }
        }

        Translation word = getWord(1);
        debug(word.original + "  -  " + word.foreign);

        // Start the application
        JFrame.setDefaultLookAndFeelDecorated(true);

        gui = new VocabGUI();
        //frame.pack();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        showRandomTranslation();
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

        JButton knowButton;
        JButton dontKnowButton;
        JLabel testedWordLabel;
        JLabel lastTestedWordLabel;
        JLabel answerTestedWordLabel;


        public VocabGUI()
        {
           testedWordLabel = new JLabel("");
           lastTestedWordLabel = new JLabel("");
           answerTestedWordLabel = new JLabel("");

           createButtons();
            createPanel();
            setSize(FRAME_WIDTH, FRAME_HEIGHT);
            setTitle("Vocaber");
            enableButtons();
        }

        private void enableButtons()
        {
            knowButton.setEnabled(true);
            dontKnowButton.setEnabled(true);
        }

        private void createButtons()
        {
            Action knowListener = new AbstractAction() {
                public void actionPerformed(ActionEvent actionEvent) {
                    knowTranslation();
                }
            };
            Action dontKnowListener = new AbstractAction() {
                public void actionPerformed(ActionEvent actionEvent) {
                    dontKnowTranslation();
                }
            };

            knowButton = new JButton("Know (N)");
            dontKnowButton = new JButton("Don't know (M)");

            knowButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("N"),"know");
            knowButton.getActionMap().put("know", knowListener);

            dontKnowButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("M"),"dont");
            dontKnowButton.getActionMap().put("dont", dontKnowListener);

            knowButton.addActionListener(knowListener);
            dontKnowButton.addActionListener(dontKnowListener);
        }

        private void createPanel()
        {
            Panel panel = new Panel();
            panel.setLayout(new GridLayout(0,2));
            panel.add(lastTestedWordLabel);
            panel.add(answerTestedWordLabel);
            panel.add(testedWordLabel);
            panel.add(new JLabel(""));

            panel.add(knowButton);
            panel.add(dontKnowButton);

            add(panel);
        }
    }

}
