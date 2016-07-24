import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by kaunildhruv on 23/07/16.
 */
public class SHA512Utils {
    ArrayList<HashValues> hashValues = null;
    ArrayList<BigInteger> constantWords = null;
    ArrayList<BigInteger> W = null;
    BigInteger a, b, c, d, e, f, g, h;
    String roundConstants = null;

    ArrayList<String> messageBlock = null;
    //Takes file K80 path as argument
    public SHA512Utils(String arg) {
        this.roundConstants = arg;
        this.hashValues = new ArrayList<>();
        this.hashValues.add(new HashValues(
                new BigInteger("6a09e667f3bcc908", 16),
                new BigInteger("2b67ae8584caa73b", 16),
                new BigInteger("3c6ef372fe94f82b", 16),
                new BigInteger("554ff53a5f1d36f1", 16),
                new BigInteger("510e527fade682d1", 16),
                new BigInteger("1b05688c2b3e6c1f", 16),
                new BigInteger("1f83d9abfb41bd6b", 16),
                new BigInteger("5be0cd19137e2179", 16)
        ));
        this.constantWords = new ArrayList<>();
        System.out.println("Initializing round constants..");
        try {
            Scanner sc = new Scanner(new File(arg));
            while (sc.hasNext()){
                constantWords.add(new BigInteger(sc.next(), 16));
                //System.out.println(constantWords.get(constantWords.size()-1));
            }

            System.out.println("initialized. Total round constants = " + this.constantWords.size());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        this.W = new ArrayList<>();
    }

    public void initMessageSchedule(){

    }

    public BigInteger Ch(BigInteger e, BigInteger f, BigInteger g){
        //ch = (e^f)xor(!x^z)
        BigInteger ch = e.and(f).xor(e.not().and(g));
        return ch;
    }

    public BigInteger Maj(BigInteger x, BigInteger y, BigInteger z ){
        BigInteger Maj = x.and(y).xor(x.and(z)).xor(y.and(z));
        return Maj;
    }

    public BigInteger Z0(BigInteger a){

        BigInteger rr28 = a.shiftRight(28);
        BigInteger rr34 = a.shiftRight(34);
        BigInteger rr39 = a.shiftRight(39);
        BigInteger Z0=rr28.and(rr34).and(rr39);
        return Z0;
    }
    public BigInteger Z1(BigInteger e){
        BigInteger rr14 = e.shiftRight(14);
        BigInteger rr18 = e.shiftRight(18);
        BigInteger rr41 = e.shiftRight(41);
        BigInteger Z1= rr14.and(rr18).and(rr41);
        return Z1;
    }

    public BigInteger Sigma0(BigInteger x){
        BigInteger rr1 = x.shiftRight(1);
        BigInteger rr8 = x.shiftRight(8);
        String binBigInt = x.toString(2);

        for(int i = 0 ;i < 7; i++){
            binBigInt = binBigInt.substring(1)+ binBigInt.substring(0,1);
        }
        BigInteger sr7 =  new BigInteger(binBigInt, 2);
        BigInteger sig0 = rr1.xor(rr8).xor(sr7);
        return sig0;

    }


    public BigInteger Sigma1(BigInteger x){

        BigInteger rr19 = x.shiftRight(19);
        BigInteger rr61 = x.shiftRight(61);
        String binBigInt = x.toString(2);
        for(int i = 0 ;i < 6; i++){
            binBigInt = binBigInt.substring(1)+ binBigInt.substring(0,1);
        }
        BigInteger sr6 =  new BigInteger(binBigInt, 2);
        BigInteger sig1 = rr19.xor(rr61).xor(sr6);
        return sig1;

    }


    public ArrayList<String> getMessageBlock() {
        return messageBlock;
    }

    public void setMessageBlock(ArrayList<String> messageBlock) {
        this.messageBlock = messageBlock;
    }


}
