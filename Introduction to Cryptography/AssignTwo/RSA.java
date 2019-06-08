/*
    Author: Liam Pilling
    Last Date Modified: 13/15/2018

    Filename: RSA.java
    Description: Performs the RSA encryption. Reads in a file,
                 calculates two prime numbers between 10,000 and
                 100,000. It then calculates e, phi, n and d. It then
                 performs the RSA encryption and decryption to the
                 texts and finally writes the encrypted and decrypted
                 strings to files. Contains functions to read and write
                 to files, check primality of numbers and execute the
                 Extended Euclidean Algorithm. Also has a function for
                 modular exponentiation for performing (b^e)mod n style
                 equations with very large numbers.
*/
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.math.*;
import java.io.*;

public class RSA
{
     /*
        Function Name: main
        Purpose: Performs the RSA encryption. Reads in a file,
                 calculates two prime numbers between 10,000 and
                 100,000. It then calculates e, phi, n and d. It then
                 performs the RSA encryption and decryption to the
                 texts and finally writes the encrypted and decrypted
                 strings to files.
        Parameters:
            args - the filename to be read as a command line parameter
        Returns:
            void
    */
    public static void main(String[] args)
    {
        //Using longs over ints as we are working with large numbers
        long p, q, n, e, phi, d, ciphNum;
        String filename = args[0];
        String message, cipherMessage;
        StringBuilder cipherText = new StringBuilder();
        StringBuilder decryptText = new StringBuilder();
        Random rand = new Random();

        System.out.println("Opening " + filename);
        message = readFile(filename);
        System.out.println(checkPrime(100003));
        System.out.println(checkPrime(10003));
        System.out.println(checkPrime(0));
        System.out.println("Selecting p and q values");
        //We keep selecting random numbers in the range 10,000 to
        //100,000 until the values are prime
        /*p = rand.nextInt(90000-1) + 10000;
        while(!checkPrime((int)p))
        {
            p = rand.nextInt(90000-1) + 10000;
        }

        q = rand.nextInt(90000-1) + 10000;
        while(!checkPrime((int)q))
        {
            q = rand.nextInt(90000-1) + 10000;
        }*/
        p = 7;
        q = 5;
        System.out.println(p + ":" + q);
        phi = (p - 1) * (q -1);
        n = p * q;

        //Keep selecting random values until an e value between 1 and
        //phi until the gcd(e, phi) = 1
        /*e = ThreadLocalRandom.current().nextLong(1, phi-1);
        while(Euclidean.euclid(phi, e) != 1)
        {
             e = ThreadLocalRandom.current().nextLong(1, phi-1);
        }*/
        
        e = 11;
        d = extendedEuclid(e, phi);
        //We add phi if d < 0 to keep d positive
        if(d < 0)
            d += phi;

        System.out.println("Public key (e, n) = (" + e + ", " + n + ")");
        System.out.println("Private key (d, n) = (" +d + ", " + n + ")");

        //This is the loop responsible for the encryption following the
        //equation c = (c^e) mod n
        for(int i = 0; i < message.length(); i++)
        {
            char c = message.charAt(i);
            ciphNum = modExp((long)c, e, n);
            cipherText.append(ciphNum + " ");
        }
        System.out.println("Writing ciphertext to RSAText.txt");
        writeToFile(cipherText.toString(), "RSAText.txt");

        //This is the loop responsible for the decryption following the
        //equation m = (c^d) mod n. We are using a string tokenizer to
        //split up the values in the encrypted file
        cipherMessage = readFile("RSAText.txt");
        StringTokenizer ciphers = new StringTokenizer(cipherMessage);
        while(ciphers.hasMoreTokens())
        {
            long num = Long.parseLong(ciphers.nextToken());
            long letter = modExp(num, d, n);
            decryptText.append((char)letter);
        }
        System.out.println("Writing decrypted text to RSADecrypt.txt");
        writeToFile(decryptText.toString(), "RSADecrypt.txt");
    }
    /*
        Function Name: checkPrime
        Purpose: To check if a given integer is prime using
                 Lehmann's algorithm.
        Parameters:
            p - the integer that we are testing primality
        Returns:
            prob - a boolean that is true if prime, otherwise false
    */
    public static boolean checkPrime(int p)
    {
        long r;
        boolean prob = false;
        int a = 0, e = 0, t = 10, tick = 0;
        Random rand = new Random();
        if(p > 0)
        {
           //Runs 10 times to increase chance of value being prime
           for(int i = 0; i < t; i++)
           {
               a = rand.nextInt(p-1) + 1;
               e = (int)((p - 1) / 2);
               r = modExp((long)a, (long)e, (long)p);
               if((r % (long)p == 1) || (r % (long)p == p-1))
               {
                   tick++;
               }
           }
        }
        //Only returns true if prime at least 9 times
        if(tick >= 9)
            prob = true;
        return prob;
    }
    /*
        Function Name: extendedEuclid
        Purpose: Performs the Extended Euclidean Algorithm on two longs.
                 From the equation ax + by = d = gcd(a, b) we are
                 retrieving x as our d value for the RSA encryption.
                 Based on pseudo code found on
                 https://www.di-mgt.com.au/euclidean.html
        Parameters:
            a - this is our e value we calculated in main
            b - this is our phi value calculated in main
        Returns:
            x2 - this is the x value calculated and becomes d in our
                 equation
    */
    public static long extendedEuclid(long a, long b)
    {
        long x, x1, x2, y, y1, y2 , d, q, r;
        x2 = 1;
        x1 = 0;
        y2 = 0;
        y1 = 1;
        while(b > 0)
        {
            q = (long)(a/b);
            System.out.println(q);
            r = a - (q * b);
            x = x2 - (q * x1);
            y = y2 - (q * y1);
            a = b;
            b = r;
            x2 = x1;
            x1 = x;
            y2 = y1;
            y1 = y;
        }
        //We are only returning the x value since it's the only one
        //we care about for RSA
        return x2;
    }
    /*
        Function Name: modExp
        Purpose: Uses modular exponentiation to calculate c = b^d mod n
                 for very large values efficiently. This function was
                 required due to the nature of working with very large
                 numbers. Based on psuedocode found on Wikipedia:
                 https://en.wikipedia.org/wiki/Modular_exponentiation
        Parameters:
            b - this is the base value
            e - the is the exponent value
            m - the is the value we are modding by
        Returns:
            c - the calculated value
    */

    public static long modExp(long b, long e, long m)
    {
        //We are using BigIntegers here since the calculations can
        //exceed the max value of a long
        BigInteger bigB = BigInteger.valueOf(b);
        BigInteger bigE = BigInteger.valueOf(e);
        BigInteger bigM = BigInteger.valueOf(m);
        BigInteger c = BigInteger.valueOf(1);

        bigB = bigB.mod(bigM);
        while (bigE.longValue() > 0)
        {
            if(bigE.mod(BigInteger.valueOf(2)).
                        compareTo(BigInteger.ONE) == 0)
            {
                c = c.multiply(bigB).mod(bigM);
            }
            bigE = bigE.shiftRight(1);
            bigB = bigB.multiply(bigB).mod(bigM);
        }
        //We return a long here since the final value won't be larger
        //than it's max value
        return c.longValue();
    }
    /*
        Function Name: readFile
        Purpose: Reads a file into a string. Same function as my DES assignment
        Parameters:
            filename - the name of the file to be read
        Returns:
            plainText - the string from the file
    */
    public static String readFile(String filename)
    {
        StringBuilder plainText = new StringBuilder();
        int c;
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                                new FileInputStream(filename), "UTF8"));
            while((c = br.read()) != -1)
                plainText.append((char)c);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        return plainText.toString();
    }
    /*
        Function Name: writeToFile
        Purpose: writes a string into a file. Same function as my DES assignment
        Parameters:
            text     - the text to be written to file
            filename - the name of the file to be written to
        Returns:
            void
    */
    public static void writeToFile(String text, String filename)
    {
        try
        {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.print(text);
            writer.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
}
