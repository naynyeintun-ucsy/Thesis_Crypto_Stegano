package com.whk.spa.core;

import android.util.Log;

/**
 * Created by Naylay on 1/9/2018.
 */

public class EncDecBS {

    public EncDecBS()
    {

    }
    public String encode(byte[] byteArray) {
        Log.i("I am in ", "Upper Stringbuffer");
        StringBuilder buf = new StringBuilder();
        int intVal = 0;
        String frag = "";
        Log.i("I am In ","Under frag");
        for (byte b : byteArray) {
            Log.i("I am In ","start for loop");
            intVal = (int) (0xff & b);
            Log.i("I am In"," uner int val");
            frag = Integer.toHexString(intVal);
            Log.i("I am In "," under frag");
            if (1 == frag.length()) {
                Log.i("I am In"," if frag len");
                frag = "0" + frag;
            }
            buf.append(frag);
        }
        Log.i("I am In ","upper retrun");
        return buf.toString();
    }

    public  static byte[] decode(String textString) {
        byte[] byteArray = new byte[(textString.length() / 2)];
        int intVal = 0;
        String frag = "";
        int c1 = 0;
        for (int i = 0; i < byteArray.length; i++) {
            c1 = (i * 2);
            frag = textString.substring(c1, (c1 + 2));
            intVal = Integer.parseInt(frag, 16);
            byteArray[i] = (byte) (0xff & intVal);
        }
        return byteArray;
    }
}
