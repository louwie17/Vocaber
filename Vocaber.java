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

/**
 * @author      louwie17
 * @since       2015-02-08
 * @version     1.0
 *
 * Acts as Q cards and used to learn German vocabulary, the vocabulary text
 * file should be formatted as follows:
 *      original word | foreign word
 */

public class Vocaber
{
    private static final boolean ENABLE_DEBUG = false;

    private static List<Translation> words = new ArrayList<Translation>();
    private static Random randomGen = new Random();
    private static int randomIndex = 0;
    private static boolean reverse = false;
    private static VocabGUI gui;

    // ============  Gui logic functions

    /**
     * Deletes the known translation from the words array list
     * if all words are removed it exits the program, otherwise a
     * new word will show
     */
    private static void knowTranslation()
    {
        // Delete known word and show new one
        debug("Know Word");
        showTranslationAnswer();
        words.remove(randomIndex);
        if (words.size() > 0) {
            showRandomTranslation();
        } else {
            JOptionPane.showMessageDialog(null, "Congrats\n"
                    + "you know everything.");
            System.exit(1);
        }
    }

    /**
     * Show answer of current translation and shows new translation
     */
    private static void dontKnowTranslation()
    {
        // Show new word
        debug("Don't Know Word");
        showTranslationAnswer();
        showRandomTranslation();
    }

    /**
     * Shows the translation answer in the GUI fields
     */
    private static void showTranslationAnswer()
    {
        Translation answer = getWord(randomIndex);
        if (reverse) {
            gui.lastTestedWordLabel.setText(answer.foreign);
            gui.answerTestedWordLabel.setText(answer.original);
        } else {
            gui.lastTestedWordLabel.setText(answer.original);
            gui.answerTestedWordLabel.setText(answer.foreign);
        }
    }

    /**
     * Generates random int and shows a random translation in the GUI
     */
    private static void showRandomTranslation()
    {
        randomIndex = randomGen.nextInt(words.size());
        debug("Random Int: " + Integer.toString(randomIndex));

        Translation newWord = getWord(randomIndex);
        if (reverse) {
            gui.testedWordLabel.setText(newWord.foreign);
        } else {
            gui.testedWordLabel.setText(newWord.original);
        }
    }

    // ========== End gui logic

    /**
     * Gets the translation at the given index
     * @param index The index of the word/translation
     * @return the translation at the given index.
     */
    public static Translation getWord(int index) {
        return words.get(index);
    }

    /**
     * Loads the words into the array from the given file
     * @param fileName the given file name
     */
    private static void loadWords(String fileName)
    {
        try {
            Scanner in = new Scanner(new File(fileName));
            while (in.hasNextLine())
            {
                // Splits line by |
                String trans[] = in.nextLine().split("\\|");;
                words.add(new Translation(trans[0], trans[1]));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found: " + fileName);
            System.exit(1);
        }
    }

    /**
     *  Debug method
     *  @param statement the printed debug statement
     */
    private static void debug(String statement)
    {
        if (ENABLE_DEBUG)
            System.err.println(statement);
    }

    /**
     * Prints out how to use Vocaber
     */
    private static void usage()
    {
        System.err.println("Usage: java Vocaber <vocabulary input file> " +
                "[-r | --reverse]");
    }

    public static void main(String[] args)
    {
        if (args.length == 0 || args.length > 2) {
            usage();
            System.exit(1);
        }
        for (String s: args)
        {
            if (s.equals("-r") || s.equals("--reverse")) {
                reverse = true;
            } else {
                debug("File name: " + s);
                loadWords(s);
            }
        }

        // Start the application
        JFrame.setDefaultLookAndFeelDecorated(true);

        gui = new VocabGUI();
        //frame.pack();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        showRandomTranslation();
    }

    // Inner classes

    /**
     * Class is used to store the vocabulary words
     */
    private static class Translation
    {
        private String original;
        private String foreign;

        public Translation(String original, String foreign)
        {
            this.original = original;
            this.foreign = foreign;
        }
    }

    /**
     * Class is used to display a GUI
     */
    public static class VocabGUI extends JFrame
    {
        private static final int FRAME_WIDTH = 450;
        private static final int FRAME_HEIGHT = 140;

        private JButton knowButton;
        private JButton dontKnowButton;
        private JLabel testedWordLabel;
        private JLabel lastTestedWordLabel;
        private JLabel answerTestedWordLabel;

        /**
         * Initiates the GUI
         */
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

        /**
         * Enables the buttons
         */
        private void enableButtons()
        {
            knowButton.setEnabled(true);
            dontKnowButton.setEnabled(true);
        }

        /**
         * Creates the buttons and callbacks for the buttons, it also
         * creates two keyboard listeners for the buttons
         */
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

        /**
         * Creates the panel
         */
        private void createPanel()
        {
            Panel panel = new Panel();
            panel.setLayout(new GridLayout(0, 2));
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
