package com.whk.spa.core;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Naylay on 1/9/2018.
 */
public class Hashgen
{
    static	char keymap[][]=new char[16][16];
    static	char keypair1[]=new char[16];
    static char keypair2[]=new char[16];
    static	char keypair3[]=new char[16];
    static	char keypair4[]=new char[16];
    static int keymapindex_x[]=new int [16];
    static int keymapindex_y[]=new int [16];
    static byte finalgenarationkey[]=new byte[16];

    public Hashgen() {

    }
    public byte[] keyGeneartor(String password) throws NoSuchAlgorithmException
    {
        long starttime=System.currentTimeMillis();


        Log.i("Your input password is " ,password);



        MessageDigest md = MessageDigest.getInstance("SHA-512");

        md.update(password.getBytes());

        byte byteData[] = md.digest();
       Log.i ("byte length is --->", String.valueOf(byteData.length));


        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<32;i++)sb.append(Integer.toString((byteData[i]& 0xff) + 0x100, 16).substring(1));

     /* for (int i = 0; i < byteData.length; i++) {
       sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
      }*/

        //System.out.println("Hex format : " + sb.toString());
        char arr[]=new char[sb.length()];
        for(int i=0;i<sb.length();i++)arr[i]=sb.charAt(i);
        //  System.out.println(sb.length());

        Genearte_keymap();



        return keycompiler(sb);
    }

    private static byte[] keycompiler(StringBuffer sb) {

        for(int i=1;i<=sb.length();i++)
        {
            //  System.out.print(i+"   ");
            if(sb.length()-i>=48)keypair1[i-1]=sb.charAt(i-1);
            else if(sb.length()-i>=32)keypair2[i-17]=sb.charAt(i-1);
            else if(sb.length()-i>=16)keypair3[i-33]=sb.charAt(i-1);
            else keypair4[i-49]=sb.charAt(i-1);
        }





        for(int i=0;i<16;i++)
        {
           // System.out.print(((char)(keypair1[i]^keypair3[i]))+" ");
            keymapindex_x[i]=(keypair1[i]^keypair3[i])%16;
            keymapindex_y[i]=(keypair2[i]^keypair4[i])%16;

        }

        for(int i=0;i<16;i++)
        {
            char temp;
            finalgenarationkey[i]=(byte)(((temp=keymap[keymapindex_x[i]][keymapindex_y[i]])==11 | temp==13) ? '@' : temp );

        }


            Log.i("data ----->" , String.valueOf(finalgenarationkey));



        return finalgenarationkey;

    }

    public static void  Genearte_keymap()
    {
        int keygen=1;
        for(int i=0;i <16;i++)
        {
            for(int j=0;j<16;j++)
            {
                keymap[i][j]=(char)keygen++;

            }


        }

    }
    long endtime=System.nanoTime();

}
