package com.whk.spa.core;

import android.graphics.Bitmap;

/**
 * Created by Naylay on 12/12/2017.
 */

public class AudioToImage {


     int keystate=0;
    public AudioToImage(){

    }


   public AudioToImage(int state)
   {
       this.keystate=state;
   }


    public Bitmap embedMessage(Bitmap img, byte[] message) {
        int messageLength = message.length;

        int imageWidth = img.getWidth(), imageHeight = img.getHeight(), imageSize = imageWidth
                * imageHeight;

        embedInteger(img, messageLength, 0, 0);
    //    img.setPixel(32,32,setBitValue(img.getPixel(32,32),0,keystate));

        byte b[] = message;
        for (int i = 0; i < b.length; i++)
            embedByte(img, b[i], i * 8 + 32, 0);


        return img;

    }




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



}
