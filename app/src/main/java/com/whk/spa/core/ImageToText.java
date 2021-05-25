package com.whk.spa.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.whk.spa.MainActivity;

/**
 * Created by Naylay on 12/10/2017.
 */

public class ImageToText {

    Context cc;
public ImageToText(){

}
public ImageToText(Context c)
{
    this.cc=c;
}


////decode message function


    public String decodeMessage(Bitmap image, int startX , int startY) {
        int len = extractInteger(image, startX, startY);
        int j=33;

        if(len>((image.getWidth()*image.getHeight())-32)*8)
        {
           // Toast.makeText(cc.getApplicationContext(),"Invalid cover object. please try with correct one !!",Toast.LENGTH_LONG).show();
            return null;

        }

    byte b[] = new byte[len];
    //  byte iskey=extractByte(image,32, 0);
    //  char str=(char)(iskey* -1);
    // int rgb=img.getPixel(32,32);
    //rgb=setBitValue(rgb,0,1 i);

    System.out.println("key value is" + getBitValue(image.getPixel(32, 32), 0));
    for (int i = 0; i < len; i++) {
        b[i] = extractByte(image, i * 8 + 32, 0);
        //if(i>0)j=32;

    }

    return new String(b);

       /* Log.i("Input integer size ", len+"");
        return "tesing string";*/

    }


 //   public int getKeyBitValue(Bitmap image)
    //{
       // return getBitValue( image.getPixel(32,32), 0);
   // }




    ///end of decode message function


    ////extract Integer

    private int extractInteger(Bitmap img, int start, int storageBit) {
        int maxX = img.getWidth(), maxY = img.getHeight(),
                startX = start/maxY, startY = start - startX*maxY, count=0;
        int length = 0;
        for(int i=startX; i<maxX && count<32; i++) {
            for(int j=startY; j<maxY && count<32; j++) {
                int rgb = img.getPixel(i, j), bit = getBitValue(rgb, storageBit);
                length = setBitValue(length, count, bit);
                count++;
            }
        }
        return length;
    }
    ////end extract Integer


    //extract byte value


    private byte extractByte(Bitmap img, int start, int storageBit) {
        int maxX = img.getWidth(), maxY = img.getHeight(),
                startX = start/maxY, startY = start - startX*maxY, count=0;
        byte b = 0;
        for(int i=startX; i<maxX && count<8; i++) {
            for(int j=startY; j<maxY && count<8; j++) {
                int rgb = img.getPixel(i, j), bit = getBitValue(rgb, storageBit);
                b = (byte)setBitValue(b, count, bit);
                count++;
            }
        }
        return b;
    }


    //end of extract byte value

    //get bit value
    private int getBitValue(int n, int location) {
        int v = n & (int) Math.round(Math.pow(2, location));

        return v == 0 ? 0 : 1;
    }

    ///get bit value



    ///set bit value

    ///set bit value

    private int setBitValue(int n, int location, int bit) {
        int toggle = (int) Math.pow(2, location), bv = getBitValue(n, location);

        if (bv == bit)
            return n;
        if (bv == 0 && bit == 1)
            n |= toggle;
        else if (bv == 1 && bit == 0)
            n ^= toggle;
        return n;
    }

    //set bit value
    ///end start bit value


}
