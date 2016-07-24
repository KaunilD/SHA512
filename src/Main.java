import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String message;
        int mLength, padLength;
        String hexMessage = new String();
        String binMessage = new String();
        String prepMessage = new String();
        ArrayList<String> words16 = new ArrayList<>();

        SHA512Utils sha512Utils = new SHA512Utils(args[0]);

        String messageHash = null;

        ArrayList<ArrayList<String>> messageBlocks = new ArrayList();
        Scanner sc = new Scanner(System.in);
        System.out.print("String : ");
        message = sc.nextLine();

        mLength = message.length();
        System.out.println("String : " + message + " with length : " + mLength);
        for(int i = 0; i < mLength; i++){
            hexMessage+= Integer.toHexString((int)message.charAt(i));
            String binaryString = Integer.toBinaryString((int)message.charAt(i));
            //pad the binary string here
            for(int j = 0; j < 8-binaryString.length(); j++){
                binMessage+="0";
            //    System.out.println("Padded bin count"+ (8-binaryString.length()));
            }
            binMessage+= Integer.toBinaryString((int)message.charAt(i));
        }

        System.out.println("String HEX : " + hexMessage + " with length : " + hexMessage.length());
        System.out.println("String BIN : " + binMessage + " with length : " + binMessage.length());
        prepMessage = binMessage;
        prepMessage+="1";
        padLength = (1024-128) - (binMessage.length())%1024;
        System.out.println("Padding length : " + padLength + " bits.");
        for(int i = 1; i < padLength; i++){
            prepMessage+="0";
        }

        for(int i = 0; i < 128-(Integer.toBinaryString(message.length()).length()); i++){
            prepMessage+="0";
        }
        System.out.println("Pre-Processing String for Digester..");
        prepMessage+=Integer.toBinaryString(message.length());
        System.out.println("Pre-Processed String Length: " + prepMessage.length() + " bits.");
        //-------------------------------------------------------------//
        //here convert each message block in to 16 64bit sub blocks
        //
        for(int i = 0; i < 1024; i=i+64){

            String bin64 = prepMessage.substring(i, i+63);
            String hex64 = new BigInteger(bin64, 2).toString(16);
            words16.add(hex64);
        }
        messageBlocks.add(words16);
        //now we have 16 64 bit message blocks of each 1024 bit block of the original message
        //perform hash computation for the N 1024 bit blocks of the original message

        for(int i = 0; i < messageBlocks.size(); i++){
            System.out.println("Digesting Message  Block : " + (i+1) + " of " + messageBlocks.size());
            sha512Utils.setMessageBlock(messageBlocks.get(i));
            //initialize expanded message blocks
            for (int a = 0; a < 16; a++){

                sha512Utils.W.add(a, new BigInteger((messageBlocks.get(i).get(a)), 16));
                //System.out.println(""+sha512Utils.W.get(a).toString(16));
            }
            for(int a = 16; a < 80; a++){
                System.out.println("Expanding sub-block : " + (a+1) + " of " + 80);
                sha512Utils.W.add(a,
                        (sha512Utils.Sigma1(sha512Utils.W.get(a-2)).add(
                    sha512Utils.W.get(a-7)).add(
                    sha512Utils.Sigma0(sha512Utils.W.get(a-15))).add(
                    sha512Utils.W.get(a-16))).mod(new BigInteger("2").pow(64))
                );
                //System.out.println(""+sha512Utils.W.get(a).toString(16));

            }
            System.out.println("Expansion Complete.");


            sha512Utils.setMessageBlock(messageBlocks.get(i));
            sha512Utils.a = sha512Utils.hashValues.get(i).h1;
            sha512Utils.b = sha512Utils.hashValues.get(i).h2;
            sha512Utils.c = sha512Utils.hashValues.get(i).h3;
            sha512Utils.d = sha512Utils.hashValues.get(i).h4;
            sha512Utils.e = sha512Utils.hashValues.get(i).h5;
            sha512Utils.f = sha512Utils.hashValues.get(i).h6;
            sha512Utils.g = sha512Utils.hashValues.get(i).h7;
            sha512Utils.h = sha512Utils.hashValues.get(i).h8;
            System.out.format("%8s  %8s     %16s    %16s    %16s    %16s     %16s    %16s    %16s\n"," ", "a", "b", "c", "d", "e", "f", "g", "h");

            for(int j = 0; j < 80; j++) {


                BigInteger T1 = sha512Utils.h.add(
                        sha512Utils.Ch(
                                sha512Utils.e,
                                sha512Utils.f,
                                sha512Utils.g
                        )).add(sha512Utils.constantWords.get(j)).add(
                        sha512Utils.W.get(j)).mod(new BigInteger("2").pow(64));

                BigInteger T2 = sha512Utils.Z0(sha512Utils.a).add(
                        sha512Utils.Maj(
                                sha512Utils.a,
                                sha512Utils.b,
                                sha512Utils.c
                        )).mod(new BigInteger("2").pow(64));

                System.out.format("%8s  %16s     %16s    %16s    %16s    %16s     %16s    %16s    %16s\n","iter = "+j, sha512Utils.a.toString(16), sha512Utils.b.toString(16), sha512Utils.c.toString(16), sha512Utils.d.toString(16), sha512Utils.e.toString(16), sha512Utils.f.toString(16), sha512Utils.g.toString(16), sha512Utils.h.toString(16));

                sha512Utils.h = sha512Utils.g;
                sha512Utils.g = sha512Utils.f;
                sha512Utils.f = sha512Utils.e;
                sha512Utils.e = sha512Utils.d.add(T1).mod(new BigInteger("2").pow(64));
                sha512Utils.d = sha512Utils.c;
                sha512Utils.c = sha512Utils.b;
                sha512Utils.b = sha512Utils.a;
                sha512Utils.a = T1.add(T2).mod(new BigInteger("2").pow(64));


            }
            sha512Utils.hashValues.add(new HashValues(
                    sha512Utils.a.add(sha512Utils.hashValues.get(i).getH1()),
                    sha512Utils.b.add(sha512Utils.hashValues.get(i).getH1()),
                    sha512Utils.c.add(sha512Utils.hashValues.get(i).getH1()),
                    sha512Utils.d.add(sha512Utils.hashValues.get(i).getH1()),
                    sha512Utils.e.add(sha512Utils.hashValues.get(i).getH1()),
                    sha512Utils.f.add(sha512Utils.hashValues.get(i).getH1()),
                    sha512Utils.g.add(sha512Utils.hashValues.get(i).getH1()),
                    sha512Utils.h.add(sha512Utils.hashValues.get(i).getH1())
            ));

        }
        //System.out.println("SHA512 sum of the input : ");
        messageHash = sha512Utils.hashValues.get(sha512Utils.hashValues.size()-1).getH1().toString(16)+" "+
                sha512Utils.hashValues.get(sha512Utils.hashValues.size()-1).getH2().toString(16)+" "+
                sha512Utils.hashValues.get(sha512Utils.hashValues.size()-1).getH3().toString(16)+" "+
                sha512Utils.hashValues.get(sha512Utils.hashValues.size()-1).getH4().toString(16)+" "+
                sha512Utils.hashValues.get(sha512Utils.hashValues.size()-1).getH5().toString(16)+" "+
                sha512Utils.hashValues.get(sha512Utils.hashValues.size()-1).getH6().toString(16)+" "+
                sha512Utils.hashValues.get(sha512Utils.hashValues.size()-1).getH7().toString(16)+" "+
                sha512Utils.hashValues.get(sha512Utils.hashValues.size()-1).getH8().toString(16);

        System.out.println("String Hash : \n" + messageHash);



    }
}
