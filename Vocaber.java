import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

public class Vocaber
{
    static List<Translation> words = new ArrayList<Translation>();

    private static boolean load_words(String fileName)
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

    public static void main(String[] args) throws FileNotFoundException
    {
        for (String s: args)
        {
            if (s.length() > 0)
            {
                System.out.println("File name: "+s);
                if (load_words(s))
                    System.out.println("Successfully loaded file");
                else
                    System.out.println("Failed loading file");
            }
        }

        Translation word = words.get(1);
        System.out.println(word.original + "  -  "+word.foreign);
    }
}
