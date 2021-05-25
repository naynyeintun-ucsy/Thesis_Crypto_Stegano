package com.whk.spa.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SaveImageAsync extends AsyncTask<Void, String, Void> {

    private Context mContext;
    private int imageResourceID;
    String filename;
    Bitmap imageresource;

    private ProgressDialog mProgressDialog;

    public SaveImageAsync(Context context, int image) {
        mContext = context;
        imageResourceID = image;
    }
    public SaveImageAsync(Context context,Bitmap imageResource, String fname)
    {
        this.mContext=context;
        this.imageresource=imageResource;
        this.filename=fname;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Saving Image to your device");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... filePath) {


            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/DCIM/saved_images");
            myDir.mkdirs();
            File file = new File (myDir, filename+".png");
           try
           {
               FileOutputStream out = new FileOutputStream(file);
               imageresource.compress(Bitmap.CompressFormat.PNG,50, out);
               publishProgress(Integer.toString(10));
               out.flush();
               out.close();


              //Toast.makeText(g,"file saved in  " +root + " /saved_image/   ", Toast.LENGTH_SHORT ).show();
           }catch (Exception e)
           {
               e.printStackTrace();

           }





        return null;

    }

    protected void onProgressUpdate(String... progress) {
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(Void filename) {
        Toast.makeText(mContext.getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }
}