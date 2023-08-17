import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.math.BigInteger;
import java.util.Date;
import java.util.Vector;

public class Reciever {
    static long d, n;

    static public long gcdExtended(long a, long b) {
        long x = 0, y = 1, lastx = 1, temp;
        if(a>b){
            temp = a ;
            a=b;
            b=temp;
        }
        long k = b;
        while (b != 0) {
            long q = a / b;
            long r = a % b;

            a = b;
            b = r;

            temp = x;
            x = lastx - q * x;
            lastx = temp;
        }
        while (lastx < 0)
            lastx += k;

        return lastx;
    }

    static long decoder(long message) {
        long dtext = 1;
        long dec = d;
        while (dec != 0) {
            dtext *= message;
            dtext %= n;
            dec--;
        }
        return dtext;
    }

    static String decrypter(Vector<Long> arr) {
        String s = "";
        // calling the decrypting function decoding function
        for (int i = 0; i < arr.size(); i++)
        s += (char)(decoder(arr.get(i)));
        return s;
    }

    public static long findGCD(long a, long b) {
        if (b == 0)
            return a;
        else
            return findGCD(b, a % b);
    }

    static public long ecalculate(long phi) {
        long e = 2;
        while (true) {
            if (findGCD(e, phi) == 1)
                break;
            e++;
        }
        return e;
    }

    static Vector<Long> splitter(String message) {
        message = message.replace("[", "").replace("]", "").replace(" ", "");

        // Step 2: Split the string into substrings
        String[] substrings = message.split(",");
        Vector<Long> integers = new Vector<Long>();
        for (int i = 0; i < substrings.length; i++)
            integers.add(Long.parseLong(substrings[i]));

        return integers;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6666); // create a server socket with port 6666
        System.out.println("Reciever started.\n");

        //first calculate public and private keys, then sends the public key to the sender.
        //----reading from file-------
        String filePath = "Key.txt";
        long p = 0, q = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line1, line2;
            while ((line1 = reader.readLine()) != null && (line2 = reader.readLine()) != null) {
                p = Long.parseLong(line1);
                q = Long.parseLong(line2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //calculate public and private keys
        n = p * q;
        if(n<=0||p<=0||q<=0||p==q)
        {  System.out.println("sorry please enter a valid values for p and q"); return;}
        long phi = (p - 1) * (q - 1);
        long enc = ecalculate(phi);
        System.out.println("\033[0;1m"+"ANNOUNCEMENT to everyone that's my public key"+"\033[0;0m");
        System.out.println("n="+n);
        System.out.println("e="+enc);
        d = gcdExtended(enc, phi);
        //---sending public key to sender via a file----
        try {
            FileWriter myWriter = new FileWriter("PU-reciever");
            myWriter.write(String.valueOf(n) + "\n");
            myWriter.write(String.valueOf(enc));
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//---
        //warming up java
        Vector<Long>temp=new Vector<>();
        for(int i=0;i<0;i++)
            temp.add((long)i+65);
        for(int i=0;i<20;i++)
          decrypter(temp);

        try {
            while (true) {
                System.out.println("I am waiting for a sender to send data");
                Socket socket = ss.accept();
                System.out.println("A sender sent request and connection is established.");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String str = null;
                while ((str = in.readLine()) != null) {
                    if (str.equals("")){System.out.println("please enter a valid text");return;}
                    Vector<Long> arr = splitter(str);
                    String message = decrypter(arr);
                    System.out.println("\033[0;1m"+"As a reciever, I can read it using my private key"+"\033[0;0m");
                    System.out.println("Here's the decrypted message: "+"\033[0;1m"+message+"\033[0;0m");
                    //calculating end time of message
                    long endTime=System.currentTimeMillis();
//                    System.out.println("end time="+endTime);
                }
                System.out.println("I have printed all messages that was sent from that sender.\n");

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            /* Don't forget to close the server socket */
            ss.close();
        }
    }
}