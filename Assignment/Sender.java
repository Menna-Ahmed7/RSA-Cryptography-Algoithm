import java.util.Date;
import java.io.*;
import java.net.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class Sender {
    static long n = 0, enc = 0;

    static long encoder(long message) {
        long etext = 1;
        long e = enc;
        while (e != 0) {
            etext *= message;
            etext %= n;
            e--;
        }
        return etext;
    }

    static Vector<Long> Encrypter(String message) {
        Vector<Long> v = new Vector<>();
        for (int i = 0; i < message.length(); i++)
            v.add(encoder(message.charAt(i)));
        return v;
    }
 static boolean isPerfectSquare(long n)
{int x=(int)Math.sqrt(n);
    return (n==x*x);}
    public static void main(String[] args) {
        String filePath = "PU-reciever";

        try {

            System.out.println("Hi I am the sender");
            Socket socket = new Socket("localhost", 6666);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//reading data from file
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line1, line2;
                while ((line1 = reader.readLine()) != null && (line2 = reader.readLine()) != null) {
                    n = Long.parseLong(line1);
                    enc = Long.parseLong(line2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (n <= 0 || enc <= 0||isPerfectSquare(n)) {
                System.out.println("sorry please enter a valid values for p and q");
                return;
            }

            //warming up java
            for (int i = 0; i < 20; i++)
                Encrypter("hello");

            //encrypting
            // The following code sends any number of messages that the user enters in the sender console encrypted
            Scanner scanner = new Scanner(System.in);
            String message = null;
            for (int i = 0; (message = scanner.nextLine()) != null; i++) {
                if (message.equals("q"))
                    break;
                //calculating start time of message
                long startTime = System.currentTimeMillis();
//                System.out.println("start time= " + startTime);
                Vector<Long> arr = Encrypter(message);
                System.out.println("\033[0;1m"+"I send the reciever the following encrypted message: \n"+arr+"\033[0;0m");
                out.println(arr);
                System.out.println("I have sent message number " + (i + 1) + " to the reciever.");
            }
            scanner.close();

            out.close();
            socket.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}