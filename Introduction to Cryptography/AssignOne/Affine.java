/*
    Authour: Liam Pilling
    Last Date Modified: 15/04/2018

    Description: This file takes in a text file with a message to be encrypted
    and then takes in two keys which are checked in the file Key.java. The
    message is then encrypted with the two keys using the Affine cypher and
    written to file. It is then decrypted and printed to written to file.
*/

import java.util.*;
import java.io.*;

public class Affine
{
    //The main function first reads the file and then calls the Key.java file
    //to input keys. It then encrypts the message and prints the cipher text and
    //then decrypts the message and prints the message.
    public static void main(String[] args)
    {
        String filename = null, cipher = null;
        Scanner keyboard = new Scanner(System.in);
        int c;
        StringBuilder message = new StringBuilder();
        System.out.println("Enter your filename");
        filename = keyboard.nextLine();
        //Read in the file.
        try
        {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            while((c = br.read()) != -1)
                message.append((char)c);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        Key keys = new Key(); //Takes in two keys and checks validity
        cipher = getCipher(message.toString(), keys); //Get the cipher
        System.out.println("Cipher text written to file AffineCipher.txt");
        writeToFile(cipher, "AffineCipher.txt"); //Write it to a file
        System.out.println("Decrypted text written to file AffineMessage.txt");
        //Write the message to a file
        writeToFile(decryptCipher(cipher,keys), "AffineMessage.txt");

    }

    //Function to encrypt the message. It takes in a string message and the keys
    //and then performs the encryption
    public static String getCipher(String message, Key keys)
    {
        StringBuilder cipher = new StringBuilder();

        for (int i = 0; i < message.length(); i++)
        {
            //Checks first if letter is uppercase or lowercase
            if((message.charAt(i) >= 'A') && (message.charAt(i) <= 'Z'))
                cipher.append(((char)('A' + (keys.getA() *
                          (message.charAt(i) - 'A') + keys.getB()) % 26)));
            else if((message.charAt(i) >= 'a') && (message.charAt(i) <= 'z'))
                cipher.append(((char)('a' + (keys.getA() *
                          (message.charAt(i) - 'a') + keys.getB()) % 26)));
            else
                cipher.append(message.charAt(i)); //Ignoring other characters
        }

        return cipher.toString();
    }

    //Function to decrypt the message. It takes in a string cipher and the keys
    //and then performs the decryption
    public static String decryptCipher(String cipher, Key keys)
    {
        StringBuilder message = new StringBuilder();

        for (int i = 0; i < cipher.length(); i++)
        {
            //Checks first if letter is uppercase or lowercase
            if((cipher.charAt(i) >= 'A') && (cipher.charAt(i) <= 'Z'))
                message.append((char)('A' + ((((keys.getInvA() *
                ((cipher.charAt(i) - 'A') - keys.getB())) % 26)) + 26 ) % 26));
            else if((cipher.charAt(i) >= 'a') && (cipher.charAt(i) <= 'z'))
                message.append((char)('a' + ((((keys.getInvA() *
                ((cipher.charAt(i) - 'a') - keys.getB())) % 26)) + 26 ) % 26));
            else
                message.append(cipher.charAt(i)); //Ignoring other characters
        }

        return message.toString();
    }

    // This is a function to write a string to a given filename
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
