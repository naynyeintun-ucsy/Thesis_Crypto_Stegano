package com.whk.spa.core;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Naylay on 12/10/2017.
 */

public class ImageToAudio{

    public ImageToAudio(){

    }


////decode message function


    public byte [] decodeMessage(Bitmap image, int startX , int startY) {
        int len = extractInteger(image, startX, startY);
        Log.i("len audiobyte ------>", " "+len);
        byte b[] = new byte[len];
        for(int i=0; i<len; i++)
            b[i] = extractByte(image, i*8+32, 0);
        return b;
       /* Log.i("Input integer size ", len+"");
        return "tesing string";*/
    }


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
