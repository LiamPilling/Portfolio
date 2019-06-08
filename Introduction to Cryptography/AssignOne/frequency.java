import java.util.*;
import java.io.*;

public class frequency
{
    public static void main(String[] args)
    {
        int highestValue = 0, interval = 1;
        String filename = null, line = null;
        Scanner keyboard = new Scanner(System.in);
        int[] freq = new int[26];
        System.out.println("Enter the name of the file:");
        filename = keyboard.nextLine();
        char[] reference = {'a','b','c','d','e','f','g','h','i','j',
        'k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

        try
        {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            while((line = br.readLine().toLowerCase()) != null)
            {
                for(int i = 0; i < line.length(); i++)
                {
                    char c = line.charAt(i);
                    for(int j = 0; j < 26; j++)
                    {
                        if(c == reference[j])
                        {
                            freq[j]++;
                            if(freq[j] > highestValue)
                                highestValue = freq[j];
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Invalid filename");
        }

        System.out.println("Character results:");
        for (int i = 0; i < 26; i++)
        {
            System.out.println((char)('A' + i) + " : " + freq[i]);
        }
    }
}
