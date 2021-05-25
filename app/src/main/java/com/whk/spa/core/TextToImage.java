package com.whk.spa.core;

import android.graphics.Bitmap;

/**
 * created by Naylay on 12/10/2017.
 */

public class TextToImage {
 int i;
    public TextToImage(int keystate)
    {
      this.i=keystate;
    }

    public  Bitmap embedMessage(Bitmap img, String message) {
        int messageLength = message.length();
        int j=33;
        int imageWidth = img.getWidth(), imageHeight = img.getHeight(), imageSize = imageWidth
                * imageHeight;

        embedInteger(img, messageLength, 0, 0);
       // char str='1';
       // byte iskey=(byte) str;
       // System.out.println("input key vlaue is "+iskey);
       // embedByte(img,iskey,32,0);
       // int rgb=img.getPixel(32,32);

        //rgb=setBitValue(rgb,0,1 i);

         //  img.setPixel(32,32,setBitValue(img.getPixel(32,32),0,i));
            //else img.setPixel(32,32,setBitValue(img.getPixel(32,32),0,0));
      //  img.setPixel(32,32);
        byte b[] = message.getBytes();
        for (int i = 0; i < b.length; i++)
        {

           // if(i>0)j=32;

            embedByte(img, b[i], i * 8 + 32, 0);

        }



        return img;
    }






    ///Start of embed integer
    private void embedInteger(Bitmap img, int n, int start,
                              int storageBit) {
        int maxX = img.getWidth(), maxY = img.getHeight(), startX = start
                / maxY, startY = start - startX * maxY, count = 0;
        for (int i = startX; i < maxX && count < 32; i++) {
            for (int j = startY; j < maxY && count < 32; j++) {

                int rgb = img.getPixel(i, j), bit = getBitValue(n, count);
                rgb = setBitValue(rgb, storageBit, bit);
                img.setPixel(i, j, rgb);
                count++;
            }
        }
    }

    ///end of embed integer


    //embed byte


    private void embedByte(Bitmap img, byte b, int start, int storageBit) {

        int maxX = img.getWidth(), maxY = img.getHeight(), startX = start
                / maxY, startY = start - startX * maxY, count = 0;
        for (int i = startX; i < maxX && count < 8; i++) {
            for (int j = startY; j < maxY && count < 8; j++) {

               int rgb = img.getPixel(i, j), bit = getBitValue(b, count);

                rgb = setBitValue(rgb, storageBit, bit);


                img.setPixel(i, j, rgb);
                count++;
            }
        }
    }

    ///embed byte

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



}
