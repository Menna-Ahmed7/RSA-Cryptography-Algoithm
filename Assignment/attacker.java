import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Vector;

public class attacker {
    static long d, n;
    static Vector<Long> arr=new Vector<Long>();
    static void primes(long n) {
        long x=(int) Math.sqrt(n);

        boolean[] isa = new boolean[(int) (x+2)];
        Arrays.fill(isa, Boolean.FALSE);
        Vector<Boolean> arr22=new Vector<Boolean>((int) (x+2));
        for(long i=2;i<=x;i++) {
            for(long j=i*i;j<=x;j+=i) {

                isa[(int) j]=true;
            }
        }
        for(int i=2;i<=x;i++)
        {
            if(isa[i]==false) {
                arr.add((long)i);
            }
        }
    }
    private static boolean isPrime(long number) {
        for (long i = 2; i*i <= number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) throws IOException {

        String filePath = "PU-reciever";
        System.out.println("i'm the attacker");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line1, line2;
            while ((line1 = reader.readLine()) != null && (line2 = reader.readLine()) != null) {
                n = Long.parseLong(line1);
                d = Long.parseLong(line2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        n*=d;
        System.out.println("n="+n);
        long i;
        long startTime = System.currentTimeMillis();
        for(i=2;i<n;i++) {
            if(n%i==0&&isPrime(i))
            {
                if(isPrime(n/i)){
                    System.out.println("the p and q are "+i+" and "+n/i);
                    break;
                }
            }
        }
        long endTime=System.currentTimeMillis();
System.out.println("total time in ms i took to get the p and q= "+(endTime-startTime));

    }
}