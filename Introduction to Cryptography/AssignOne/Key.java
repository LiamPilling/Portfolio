/*
   This is a file to create the A and B keys into a key object from a user input.
   This input is then checked with to make sure that A is a coprime of 26 and 
   that B is a value between 0 and 25
*/
import java.util.*;

public class Key
{
    private int aKey;
    private int invA;
    private int bKey;
    // This array stores all of the co primes of 26 and their corresponding
    // inverse according to a * a-1 mod 26 = 1.
    private Integer[][] allAValues = {{1, 3, 5, 7, 9,11,15,17,19,21,23,25},
                                      {1, 9,21,15, 3,19, 7,23,11, 5,17,25}};
    private Scanner keyboard = new Scanner(System.in);

    public Key()
    {
        setKeys();
    }

    // This takes user input for the keys and then checks if they are valid. This
    // loops until valid integers are entered.
    private void setKeys()
    {
        System.out.println("Please enter your A and B keys:");
        aKey = keyboard.nextInt();
        bKey = keyboard.nextInt();

        List<Integer> aValues = Arrays.asList(Arrays.asList(allAValues).get(0));
        while(!aValues.contains(aKey))
        {
            System.out.println("Please enter valid A key");
            aKey = keyboard.nextInt();
        }

        while((bKey < 0) || (bKey > 25))
        {
            System.out.println("Enter a valid B key");
            bKey = keyboard.nextInt();
        }

        for(int i = 0; i < allAValues[0].length; i++)
        {
            if(aKey == allAValues[0][i])
                invA = allAValues[1][i];
        }
    }

    public int getA()
    {
        return aKey;
    }

    public int getB()
    {
        return bKey;
    }

    public int getInvA()
    {
        return invA;
    }
}
