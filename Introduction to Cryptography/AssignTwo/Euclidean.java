/*
    Authour: Liam Pilling
    Last Date Modified: 16/15/2018
    
    Filename: Euclidean.java
    Description: this program takes in two values a and b where
                 a >  0 and b > 0 and then calculates the gcd(a, b).
*/
import java.util.*;

public class Euclidean
{
    /*
        Function Name: main
        Purpose: Takes in two numbers, a and b, checks if they are
                 greater than zero. It then sends the values to the eucild
                 function to calculate the greatest common denominator.
        Parametres:
            args - command line parametres
        Returns:
            void
    */

    public static void main(String[] args)
    {
        int a = 0, b = 0;
        long value;
        boolean valid = false;
        Scanner keyboard = new Scanner(System.in);

        //Makes sure values are greater than zero
        while(!valid)
        {
            valid = true;
            System.out.println("Enter your a and b values: ");
            a = keyboard.nextInt();
            b = keyboard.nextInt();
            if((a <= 0) || (b <= 0))
            {
                System.out.println("a and b must both be greater than 1.");
                valid = false;
            }
        }
        value = euclid((long)a, (long)b);
        System.out.println("The GCD(" + a + "," + b + ") = " + value);
    }
    /*
    Function Name: euclid
    Purpose: calculates the gcd(a, b) using the algorithm found in the
             textbook.
    Parametres:
        a - the first value
        b - the second value
    Returns:
        divisor - the gcd(a, b) after the loop is complete
    */
    public static long euclid(long a, long b)
    {
        long dividend = a;
        long divisor = b;
        long q = dividend / divisor;
        long r = dividend % divisor;

        while(r != 0)
        {
            dividend = divisor;
            divisor = r;
            q = dividend / divisor;
            r = dividend % divisor;
        }
        //When r is 0 the final divisor is the GCD
        return divisor;
    }
}
