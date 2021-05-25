package com.whk.spa;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Virtualizer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.print.PrintAttributes;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whk.spa.core.AES;
import com.whk.spa.core.AudioToImage;
import com.whk.spa.core.EncDecBS;
import com.whk.spa.core.ImageToAudio;
import com.whk.spa.core.ImageToText;
import com.whk.spa.core.TextToImage;
import com.whk.spa.util.FileUtils;
import com.whk.spa.util.PathResolver;
import com.whk.spa.util.SaveImageAsync;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    FrameLayout process;
    private static int RESULT_LOAD_AUDIO = 1;
    private static int RESULT_LOAD_IMAGE= 2;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final int FILE_SELECT_CODE = 0;
    public byte audiobytedata[];
    private MediaRecorder mediaRecorder;
    RadioGroup ed, ta;
    long audiosize;
    Button aes;
    boolean isloadedcoverimgstate=false;

    static final String AB = "abcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();
    byte audiobyte[];
    MediaPlayer mp ;
    static int maxsizeprocess = 0;
    TextView dialogtitle;
    static Bitmap embeddedImage = null;
    String password = "";
    Context c = this;
    boolean onestoprelease=false;
    int encpasswordstate = 0;
    //ImageView inputimg,outputimg;
    static int inputimgwidth;
    int keychecker;
    static int inputimgheight;
    int backfrom=1;
    Bitmap bMap;
    RadioButton rbtn;
    public static String voiceStoragePath="";
    Handler han=new Handler();
    String fname;
    private String filepath = "";
    ImageView imageView;
    public static boolean recordingstate=false;
    String audiopath="";
    boolean playpausecondition=false;
    Boolean encrypt = true, decrypt = false, text = true, audio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id_connect();
        SharedPreferences editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        //"No name defined" is the default value.

        maxsizeprocess = editor.getInt("idchoice", 0); //0 is the default value.

        imageView = (ImageView) findViewById(R.id.mainImgView);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //  Toast.makeText(getApplicationContext(),"You make long click",Toast.LENGTH_LONG).show();
                loadChoiceDiaLog();
                return true;
            }
        });


        aes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadfragment();
            }
        });

        ed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int check_id) {
                if (check_id == R.id.encrypt) {
                    encrypt = true;
                    decrypt = false;
                    call_process_frag(encrypt, decrypt, text, audio);
                } else if (check_id == R.id.decrypt) {
                    encrypt = false;
                    decrypt = true;
                    call_process_frag(encrypt, decrypt, text, audio);

                }
            }
        });

        ta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int check_id) {
                if (check_id == R.id.text) {
                    text = true;
                    audio = false;
                    call_process_frag(encrypt, decrypt, text, audio);

                } else if (check_id == R.id.audio) {
                    text = false;
                    audio = true;
                    call_process_frag(encrypt, decrypt, text, audio);
                }
            }
        });

    }

    private void loadChoiceDiaLog() {

        SharedPreferences editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        //"No name defined" is the default value.

        maxsizeprocess = editor.getInt("idchoice", 0); //0 is the default value.
        Log.i("output user choice", maxsizeprocess + "");
        final Dialog dialog = new Dialog(this, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        final List<String> stringList = new ArrayList<>();  // here is list
        for (int i = 0; i < 9; i++) {
            stringList.add("pixel width " + (200 + i * 100) + "px");
        }
        stringList.add("Preferable Size ");

        RadioGroup rg = null;
        rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
        rg.removeAllViews();
//        rg.releasePointerCapture();
        Resources idd;


        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rb.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            if (i % 2 == 0) rb.setBackgroundColor(Color.parseColor("#e4e2e2"));
            else rb.setBackgroundColor(Color.parseColor("#ffffff"));
            rb.setPadding(2, 2, 2, 2);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 2, 0, 1);
            rb.setLayoutParams(lp);
            rb.setId(i);
            rb.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            rg.addView(rb, i);


        }
        if (maxsizeprocess == 0) {
            ((RadioButton) rg.getChildAt(9)).setChecked(true);
        } else {
            int index = maxsizeprocess / 100 - 2;
            ((RadioButton) rg.getChildAt(index)).setChecked(true);

        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int kk = radioGroup.getCheckedRadioButtonId();
                RadioButton rbb = (RadioButton) radioGroup.getChildAt(kk);

                String str = rbb.getText().toString();
                str = str.replaceAll("\\D+", "");
                int userchoice;
                if (str.equals("") || str.equals(null)) userchoice = 0;
                else userchoice = Integer.parseInt(str);
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                Log.i("input user choicde", userchoice + "");
                editor.putInt("idchoice", userchoice);
                editor.apply();

                // Toast.makeText(getApplicationContext(),"check chage" + str,Toast.LENGTH_LONG).show();
            }
        });


        SharedPreferences editorr = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        //"No name defined" is the default value.

        maxsizeprocess = editorr.getInt("idchoice", 0); //0 is the default value.

        dialog.show();


    }


    public void audioSecurityCheck(View view)
    {
        if (encrypt_audio.securitycheckbox.isChecked() == true) {
            encrypt_audio.password.setEnabled(true);
            encrypt_audio.password.setHint("Key");
        } else {
            encrypt_audio.password.setText("");
            encrypt_audio.password.setEnabled(false);

            encrypt_audio.password.setHint("");

        }

    }

    private void call_process_frag(Boolean encrypt, Boolean decrypt, Boolean text, Boolean audio) {
        if (encrypt == true && text == true) {
            imageView.setImageResource(R.drawable.mvv);
            password="";
            if(mp!=null)mp.release();
            fname="";
            backfrom=1;
            isloadedcoverimgstate=false;
            mp=null;
            playpausecondition=false;


            audiopath="";
            audiobytedata=null;
            audiobyte=null;

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.process, new encrypt_text());
            ft.commit();
        } else if (encrypt == true && text == false) {
            password="";
            fname="";
            if(mp!=null)mp.release();
            mp=null;
            isloadedcoverimgstate=false;
            playpausecondition=false;
            backfrom=2;


            audiopath="";
            audiobytedata=null;
            audiobyte=null;

            imageView.setImageResource(R.drawable.mvv);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.process, new encrypt_audio());
            ft.commit();
        } else if (encrypt == false && text == true) {
            password="";
            fname="";
            if(mp!=null)mp.release();
            mp=null;
            isloadedcoverimgstate=false;
            playpausecondition=false;
            backfrom=3;

            audiopath="";
            audiobytedata=null;
            audiobyte=null;

            imageView.setImageResource(R.drawable.mvv);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.process, new decrypt_text());
            ft.commit();
        } else if (encrypt == false && text == false) {
            password="";
            fname="";
            if(mp!=null)mp.release();
            mp=null;

            isloadedcoverimgstate=false;
            backfrom=4;


            audiopath="";
            audiobytedata=null;
            audiobyte=null;

            playpausecondition=false;
            imageView.setImageResource(R.drawable.mvv);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.process, new decrypt_audio());
            ft.commit();
        }

    }


    private void loadfragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.process, new aes());
        ft.commit();

    }

    public void onCheckBoxAction(View view) {
        if (encrypt_text.check.isChecked() == true) {
            encrypt_text.password.setEnabled(true);
            encrypt_text.password.setHint("Key");
        } else {
            encrypt_text.password.setEnabled(false);
            encrypt_text.password.setHint("");

        }


    }



    public void onExecuteProcess(View view) {

        if(isloadedcoverimgstate)
        {

            password = encrypt_text.password.getText().toString();
            if (password.equals("null") || password.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setMessage("Are you sure embbed without password")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                encpasswordstate = 0;
                                embedFunction();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                encpasswordstate = 1;
                embedFunction();


        }







            }


        else
        {
            Toast.makeText(getApplicationContext(),"You need to load cover image first",Toast.LENGTH_SHORT).show();

        }

            // password = userInputDialogEditText.getText().toString();
            // Toast.makeText(c.getApplicationContext(),"Your password is "+userInputDialogEditText.getText().toString() ,Toast.LENGTH_SHORT).show();



        /// Toast.makeText(getApplicationContext(),"This is on execution action", Toast.LENGTH_SHORT).show();
    }

    public void loadCoverImage(View view) {


        //  Toast.makeText(getApplicationContext(),"on click message",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void id_connect() {
        process = (FrameLayout) findViewById(R.id.process);
        aes = (Button) findViewById(R.id.aes);
        ed = (RadioGroup) findViewById(R.id.ed);
        ta = (RadioGroup) findViewById(R.id.ta);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.process, new encrypt_text());
        ft.commit();
    }


    private void loadImage() {


        bMap = BitmapFactory.decodeFile(filepath);

        // bMap.getWidth()
       /* Toast.makeText(getApplicationContext(),"This is file size width"+getWindowManager().getDefaultDisplay().getWidth()+" height "
                +getWindowManager().getDefaultDisplay().getHeight()+"image width "+bMap.getWidth()+" height "+bMap.getHeight(),Toast.LENGTH_LONG).show();*/
        //  inputimg.setBackgroundColor(Color.RED);
        int allowwidth = getWindowManager().getDefaultDisplay().getWidth();
        int allowheith = getWindowManager().getDefaultDisplay().getHeight() / 4;
        inputimgwidth = bMap.getWidth();
        inputimgheight = bMap.getHeight();
        int no_of_time = 0;
        if (inputimgheight > allowheith) {
            no_of_time = ((1 * inputimgheight) / allowheith);

            inputimgheight = inputimgheight / no_of_time;
            inputimgwidth = inputimgwidth / (no_of_time);
            Log.i("outinfo", no_of_time + " " + inputimgwidth + " " + inputimgheight);
            //  System.out.print(no_of_time +" "+inputimgwidth+" "+inputimgheight);

        }

        audiosize=(bMap.getWidth()*bMap.getHeight()-32)/8;
        if(backfrom==1 || backfrom==2)
        Toast.makeText(getApplicationContext(),"According to your cover image,you can hide " +(audiosize/1000.00)+"KB",Toast.LENGTH_LONG
        ).show();

        imageView.setImageBitmap(Bitmap.createScaledBitmap(bMap, inputimgwidth, inputimgheight, false));
        isloadedcoverimgstate=true;

        if (encrypt == false && text == true) {
            ImageToText imgtot = new ImageToText();
           /* keychecker = imgtot.getKeyBitValue(bMap);
            if (keychecker == 1) {
                decrypt_text.keycheck.setVisibility(View.VISIBLE);
                decrypt_text.password.setVisibility(View.VISIBLE);
                ;
                encrypt_text.password.setEnabled(true);

            } else {
                decrypt_text.keycheck.setVisibility(View.INVISIBLE);
                decrypt_text.password.setVisibility(View.INVISIBLE);
            }*/





            System.out.println("key checker value is " + keychecker);

        }


       // if (encrypt==false && audio==true)



        //  inputimg.setImageURI(Uri.fromFile(new File(filepath)));
    }

    public void onClearAction(View view) {
        encrypt_text.messagebox.setText("");
    }


    public void embedFunction() {
        String mess = encrypt_text.messagebox.getText().toString();
        try {
            AES aesalgorithm = new AES(password);
            EncDecBS eanddcoder = new EncDecBS();

            byte[] output = aesalgorithm.encrypt(mess.getBytes());
            mess = eanddcoder.encode(output);
            Log.i(mess, "input is here");
        } catch (Exception e) {
            Log.e("error exc", e.toString());
        }

        //  mess=aes.encrypt(mess.getBytes());
        if (maxsizeprocess == 0) {
            embeddedImage = bMap.copy(Bitmap.Config.ARGB_8888, true);

        } else {
            Bitmap scaledBitmap = scaleDown(bMap, maxsizeprocess, true);
            embeddedImage = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true);
        }

        TextToImage textoimagembedder = new TextToImage(encpasswordstate);
        int messageLength = mess.getBytes().length;

        int imageWidth = embeddedImage.getWidth(), imageHeight = embeddedImage.getHeight(), imageSize = imageWidth
                * imageHeight;
        if (messageLength * 8 + 32 > imageSize) {
            Toast.makeText(getApplicationContext(),
                    "Message is too long for the chosen image" +
                            "Message too long!", Toast.LENGTH_SHORT).show();
            return;
        }
        embeddedImage = textoimagembedder.embedMessage(embeddedImage, mess);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //  bMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        //  byte[] byteArray = stream.toByteArray();

        //  Bundle b = new Bundle();
        // b.putByteArray("image",byteArray);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Result_imgemb resfrg = new Result_imgemb();


        ft.replace(R.id.process, resfrg);
        ft.commit();
        // Result_imgemb.outputimg.setImageResource(R.drawable.about_icon);
        //  Result_imgemb.outputimg.setImageBitmap(Bitmap.createScaledBitmap(embeddedImage, inputimgwidth, inputimgheight, false));
    }


    public void saveforTtoI(View view) {


        loadSaveDialog();

    }


    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }



    public static boolean isAudioFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("audio");
    }

    private void loadSaveDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        dialogtitle = (TextView) mView.findViewById(R.id.dialogTitle);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        fname = userInputDialogEditText.getText().toString();
                        if (fname.equals(null) || fname.equals("")) {
                            loadSaveDialog();

                        } else {
                            loadSaveClass();
                            dialogBox.cancel();


                            //SaveImage(embeddedImage,fname);

                        }

                        // password = userInputDialogEditText.getText().toString();
                        // Toast.makeText(c.getApplicationContext(),"Your password is "+userInputDialogEditText.getText().toString() ,Toast.LENGTH_SHORT).show();
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                Toast.makeText(getApplicationContext(), "You need to Enter Image Name ", Toast.LENGTH_SHORT).show();
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

        alertDialogAndroid.show();

    }

    private void loadSaveClass() {

        Log.i("LoadClass method", "Loading");
        new SaveImageAsync(this, embeddedImage, fname).execute();
        Result_imgemb.outputimg=null;
    }

    public void shareforTtoI(View view) {

        shareImage();
    }

    public void backforTtoI(View view) {
        if(backfrom==1){
            fname="";
            password="";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.process, new encrypt_text());
            ft.commit();
        }

       else {   fname="";
            password="";
            onestoprelease=true;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.process, new encrypt_audio());
            ft.commit();

        }

    }


    private void SaveImage(Bitmap finalBitmap, String filename) {

        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();


        File file = new File(myDir, filename + ".jpg");
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);

            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(getApplicationContext(), "file saved in  " + root + " /saved_image/   ", Toast.LENGTH_SHORT).show();
            //finalBitmap.compress(Bitmap.CompressFormat.PNG)
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onClearActiondec(View view) {
        decrypt_text.outputmessageBox.setText("");
    }

    public void onExecuteProcessDec(View view) {
         if(bMap==null){
             Toast.makeText(getApplicationContext(),"You need to load image first",Toast.LENGTH_SHORT).show();
             return;
         }
        ImageToText imagetotextdecoder = new ImageToText(this);
        Bitmap embb = bMap.copy(Bitmap.Config.ARGB_8888, true);
        String output_stirng;
try{
      output_stirng = imagetotextdecoder.decodeMessage(embb, 0, 0);
    if(output_stirng==null)
    {
        Toast.makeText(getApplicationContext(),"Invalid cover object. please try with correct one !!",Toast.LENGTH_LONG).show();
        return;
    }
}catch (Exception e)
{
    Toast.makeText(getApplicationContext(),"Invalid cover object. please try with correct one !!",Toast.LENGTH_LONG).show();
    return;
}
        AES aesalgorithm;
      //   keychecker = imagetotextdecoder.getKeyBitValue(embb);
        if (decrypt_text.password.getText().toString().equals(null) || decrypt_text.password.getText().toString().equals("")) {
            aesalgorithm = new AES("");
        } else {
            aesalgorithm = new AES(decrypt_text.password.getText().toString());


        }
        EncDecBS eanddcoder = new EncDecBS();
        try {
            Log.i(output_stirng, "output is here");
            byte[] output = aesalgorithm.decrypt(eanddcoder.decode(output_stirng));
            decrypt_text.outputmessageBox.setText(new String(output));
        } catch (Exception e) {
            Log.e(" error is here", e.toString());
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
            Toast.makeText(getApplicationContext(),"Fail to Extract try again!!!",Toast.LENGTH_SHORT).show();
        }


    }

    private void shareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory
        String imagePath = Environment.getExternalStorageDirectory()
                + "/DCIM/saved_images/" + fname+".png";

        File imageFileToShare = new File(imagePath);

        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(share, "Share Image!"));
    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    private boolean isfromExternal(Uri selectaudio) {

        String str = selectaudio.toString();
        if (str.contains("file://")) return true;


        return false;
    }


    public void audioPlayer(String path) {
        //set up MediaPlayer
        encrypt_audio.playpause.setEnabled(true);
        mp = new MediaPlayer();


        System.out.println("Audio path : " + path);

        try {

            mp.setDataSource(path);
            mp.prepare();
            //    mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOAD_AUDIO && resultCode == RESULT_OK && null != data) {
            Uri selectaudio = data.getData();
            if (isfromExternal(selectaudio)) {
                audiopath = selectaudio.toString();
                audiopath = audiopath.substring(5);
            } else {

                String[] filePathColumn = {MediaStore.Audio.Media.DATA};
                Log.i("selected audio  ", selectaudio.toString());
                Cursor cursor = getContentResolver().query(selectaudio,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                audiopath = cursor.getString(columnIndex);
                if(audiopath==null)
                {
                    audiopath=PathResolver.getRealPathFromURI(this,selectaudio);


                }
                if(audiopath==null || audiopath.equals(null) || audiopath.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"System can't load this audio file. Try another file !!!",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!isAudioFile(audiopath))
                {
                    Toast.makeText(getApplicationContext(),"Your select file is not support to embed in cover image. Try using audio file !!",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("selected audio ext  ", audiopath);
                cursor.close();
            }
            /// this is the image decoder path


            ///newely add function


            FileInputStream fis = null;
            try {
                fis = new FileInputStream(audiopath);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];

                for (int readNum; (readNum = fis.read(b)) != -1; ) {
                    bos.write(b, 0, readNum);
                }

                byte[] bytes = bos.toByteArray();
                if(bytes.length!=0)
                {
                    audiobytedata=bytes;
                    encrypt_audio.playpause.setVisibility(View.VISIBLE);
                    audioPlayer(audiopath);
                }
                Toast.makeText(getApplicationContext(), "This is audio byte lenght " + bytes.length/1000.00, Toast.LENGTH_SHORT).show();
            } catch (IOException ee) {
                ee.printStackTrace();
            }

        }

        //result load image




else {


    switch (requestCode) {
        case FILE_SELECT_CODE:
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                Log.i("output uri is" ,uri.toString());
                Log.d(TAG, "File Uri: " + uri.toString());
                // Get the path
                String path = null;
                try {
                    filepath = FileUtils.getPath(this, uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                if (filepath ==null)
                {
                    filepath=PathResolver.getRealPathFromURI(this,uri);
                    System.out.print("resolver file path" +filepath);

                }
                Log.d(TAG, "File Path: " + filepath);
                if (filepath.length() != 0)
                {
                    if (isImageFile(filepath))
                    loadImage();
                    else {
                        Toast.makeText(getApplicationContext(),"Your selected file is not support to use as cover image. Try with another !!!  ",Toast.LENGTH_LONG).show();
                    }
                }


            }
            break;
    }
}
        super.onActivityResult(requestCode, resultCode, data);
    }


    /////                 Audio Path                ///

    public void loadAudio(View view)
    {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    RESULT_LOAD_AUDIO);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

          /*  Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_AUDIO);*/
            //  buttonLoadImage.setEnabled(false);
            //    buttonLoadImage.setVisibility(View.INVISIBLE);
           // encrypt_audio.record.setEnabled(false);
         //   encrypt_audio.record.setVisibility(View.INVISIBLE);
            encrypt_audio.skbar.setVisibility(View.VISIBLE);

    }






    Runnable run=new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
             SeekUpdation();
        }

    };
    public void SeekUpdation() {
        // TODO Auto-generated method stub
        if(mp!=null)
        {
            int mpos = mp.getCurrentPosition();
            int mdur= mp.getDuration();
            if(backfrom==2)
            {
                encrypt_audio.skbar.setMax(mdur);
                encrypt_audio.skbar.setProgress(mp.getCurrentPosition());
            }
            else
            {
                decrypt_audio.skbar.setMax(mdur);
              decrypt_audio.skbar.setProgress(mp.getCurrentPosition());
            }

            han.postDelayed(run, 100);

        }

    }




    public void embedAudioFunction()
    {

        if(audiobytedata==null){
            Toast.makeText(getApplicationContext(),"Please load a audio file or record audio ",Toast.LENGTH_SHORT).show();
        }

        else {

            // embeddedImage = bMap.copy(Bitmap.Config.ARGB_8888, true);
            // AudioToImage audiotoimageembedder=new AudioToImage();
            //  int messageLength = mess.length();
            if (maxsizeprocess == 0) {
                embeddedImage = bMap.copy(Bitmap.Config.ARGB_8888, true);

            } else {
                Bitmap scaledBitmap = scaleDown(bMap, maxsizeprocess, true);
                embeddedImage = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }

            // AudioToImage audiotoimge = new AudioToImage(encpasswordstate)


            //// start copy embedfun


            // String mess = encrypt_text.messagebox.getText().toString();
            try {
                Log.i("incoming password", password);
                password = encrypt_audio.password.getText().toString();
                if (!(password.equals("") || password.equals(null))) {
                    AES aesalgorithm = new AES(password);
                    audiobytedata = aesalgorithm.encrypt(audiobytedata);
                }

                // EncDecBS eanddcoder = new EncDecBS();


                // mess = eanddcoder.encode(output);
                // Log.i(mess, "input is here");
            } catch (Exception e) {
                Log.e("error exc", e.toString());
            }

            //  mess=aes.encrypt(mess.getBytes());
            Log.i("input key state", encpasswordstate + "");


            int imageWidth = embeddedImage.getWidth(), imageHeight = embeddedImage.getHeight(), imageSize = imageWidth
                    * imageHeight;
            Log.i("image size" + imageSize, "Audio Size" + audiobytedata.length * 8);
            if (audiobytedata.length * 8 + 32 > imageSize) {
                Toast.makeText(getApplicationContext(),
                        "Message is too long for the chosen image" +
                                "Message too long!", Toast.LENGTH_SHORT).show();
                return;
            }

            AudioToImage audiotoimageembedder = new AudioToImage();

            embeddedImage = audiotoimageembedder.embedMessage(embeddedImage, audiobytedata);


            // ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //  bMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            // byte[] byteArray = stream.toByteArray();


            //  Bundle b = new Bundle();
            // b.putByteArray("image",byteArray);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Result_imgemb resfrg = new Result_imgemb();


            ft.replace(R.id.process, resfrg);
            ft.commit();


            ///end copy embedfun


            embeddedImage = audiotoimageembedder.embedMessage(embeddedImage, audiobytedata);
            //  .setImageBitmap(Bitmap.createScaledBitmap(embeddedImage, inputimgwidth, inputimgheight, false));
        }
    }

    public void embedAudio(View view){


        if(isloadedcoverimgstate){
            password = encrypt_audio.password.getText().toString();
            if (password.equals("null") || password.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setMessage("Are you sure embbed without password")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                encpasswordstate = 0;
                                embedAudioFunction();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                encpasswordstate = 1;
                embedAudioFunction();
            }


        }
        else
        {
            Toast.makeText(getApplicationContext(),"You need to load cover image first",Toast.LENGTH_SHORT).show();

        }







    }

    public void playAudio(View view)
    {
        encrypt_audio.playpause.toggle();
        SeekUpdation();

        //  Log.i("playpause test ----->", "playpause outer if"+String.valueOf(playpausecondition));
        if(!playpausecondition){

            try {
                audioPlayer(audiopath);
                encrypt_audio.vitualizer.setColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                encrypt_audio.vitualizer.setStrokeWidth(3);
                encrypt_audio.vitualizer.setPlayer(mp);
                mp.start();

              //  playpausebutton.setImageResource(R.drawable.pausebtn);
                playpausecondition=true;

                //  Log.i("playpause test ----->", "playpause inner  if"+String.valueOf(playpausecondition));
            } catch (Exception e) {
                e.printStackTrace();
                //  Log.i("playpause test ----->", "playpause in the catch of if"+String.valueOf(playpausecondition));
            }}
        else
        {
            mp.pause();
           // playpausebutton.setImageResource(R.drawable.playbtn);
            playpausecondition=false;
            if(onestoprelease)onestoprelease=false;
          else  encrypt_audio.vitualizer.release();
            //   Log.i("playpause test ----->", "playpause inner esle of if"+String.valueOf(playpausecondition));
        }
    }


    public void playDecodedAudio(View view)
    {
        decrypt_audio.playpause.toggle();
        SeekUpdation();

        //  Log.i("playpause test ----->", "playpause outer if"+String.valueOf(playpausecondition));
        if(!playpausecondition){

            try {
               // audioPlayer(audiopath);
                decrypt_audio.vitualizer.setColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                decrypt_audio.vitualizer.setStrokeWidth(3);
                decrypt_audio.vitualizer.setPlayer(mp);
                mp.start();

                //  playpausebutton.setImageResource(R.drawable.pausebtn);
                playpausecondition=true;

                  Log.i("playpause test ----->", "playpause inner  if"+String.valueOf(playpausecondition));
            } catch (Exception e) {
                e.printStackTrace();
                 Log.i("playpause test ----->", "playpause in the catch of if"+String.valueOf(playpausecondition));
            }}
        else
        {
            mp.pause();
            // playpausebutton.setImageResource(R.drawable.playbtn);
            playpausecondition=false;
            decrypt_audio.vitualizer.release();
              Log.i("playpause test ----->", "playpause inner esle of if"+String.valueOf(playpausecondition));
        }
    }
    public void extractAudio(View view)
    {

        if(bMap==null)
        {
            Toast.makeText(getApplicationContext(),"You need to load cover image first",Toast.LENGTH_SHORT).show();
            return;

        }
        ImageToAudio imagetoaudiodecoder=new ImageToAudio();
        Bitmap embb=bMap.copy(Bitmap.Config.ARGB_8888, true);
        audiobyte=imagetoaudiodecoder.decodeMessage(embb,0,0);
        if(!(audiobyte.equals(null) || audiobyte==null ))decrypt_audio.playpause.setEnabled(true);
        AES aesalgorithm;
        //keychecker = imagetoaudiodecoder.getKeyBitValue(embb);
        if (!(decrypt_audio.password.getText().toString().equals("") || decrypt_audio.password.getText().toString().equals(null))) {
            Log.i("REal time password", decrypt_audio.password.getText().toString());
            aesalgorithm = new AES(decrypt_audio.password.getText().toString());
            try {
                audiobyte = aesalgorithm.decrypt(audiobyte);
            } catch (Exception e) {
                e.printStackTrace();
            }
          //  aesalgorithm = new AES(decrypt_audio.password.getText().toString());
        }

        if(audiobyte!=null){
            decrypt_audio.playpause.setVisibility(View.VISIBLE);
            decrypt_audio.skbar.setVisibility(View.VISIBLE);

        }
        playMp3(audiobyte);
        Toast.makeText(getApplicationContext(),"Audio Length is  "+audiobyte.length , Toast.LENGTH_SHORT ).show();
    }


    public void recordingAudio(View view)
    {
        if(!recordingstate)
        {

            voiceStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File audioVoice = new File(voiceStoragePath + File.separator + "voices");
            if(!audioVoice.exists()){
                audioVoice.mkdir();
            }
            voiceStoragePath = voiceStoragePath + File.separator + "voices/" + generateVoiceFilename(6) + ".mp3";
            // System.out.println("Audio path : " + voiceStoragePath);
            audiopath=voiceStoragePath;
           // recordingButton.setImageResource(R.drawable.recorderimagered);
           // buttonLoadImage.setVisibility(View.INVISIBLE);
            //messagertxt.setVisibility(View.INVISIBLE);
            encrypt_audio.skbar.setVisibility(View.VISIBLE);
            if(mediaRecorder == null){
                initializeMediaRecord();
            }
            startAudioRecording();


            encrypt_audio.record.setText("Stop");
            encrypt_audio.record.setTextColor(Color.RED);
            recordingstate=true;
        }
        else
        {
            encrypt_audio.record.setText("Rec");
            encrypt_audio.record.setTextColor(Color.parseColor("#16C6F1"));
            recordingstate=false;
          //  encrypt_audio.record.setImageResource(R.drawable.reocorderiamge);
            stopAudioRecording();

            audiopath=voiceStoragePath;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(audiopath);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];

                for (int readNum; (readNum = fis.read(b)) != -1; ) {
                    bos.write(b, 0, readNum);
                }

                byte[] bytes = bos.toByteArray();
                if(bytes.length!=0)
                {
                    audiobytedata=bytes;
                    encrypt_audio.playpause.setVisibility(View.VISIBLE);

                }

                audioPlayer(audiopath);

               // Toast.makeText(getApplicationContext(), "This is audio byte lenght " + bytes.length, Toast.LENGTH_SHORT).show();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"The size of recording is "+ (audiobytedata.length/1000.00)+" kB",Toast.LENGTH_LONG).show();
            encrypt_audio.playpause.setVisibility(View.VISIBLE);

        }


    }
    private void playMp3(byte[] mp3SoundByteArray) {
//        encrypt_audio.playpause.setEnabled(true);
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("Test", "mp3", getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // resetting mediaplayer instance to evade problems
            mp = new MediaPlayer();
            mp.reset();

            // In case you run into issues with threading consider new instance like:
            // MediaPlayer mediaPlayer = new MediaPlayer();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mp.setDataSource(fis.getFD());

            mp.prepare();

        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
            Log.i("error is playmp3" ,s);
        }
    }


    private String generateVoiceFilename( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }


    private void initializeMediaRecord(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setMaxFileSize(audiosize);
        mediaRecorder.setOutputFile(voiceStoragePath);
        if(mp!=null)
        {
            mp.stop();
            han.removeCallbacks(run);
            encrypt_audio.playpause.toggle();
            mp.release();
            playpausecondition=false;

            encrypt_audio.vitualizer.release();


        }

    }
    private void startAudioRecording(){
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //recordingButton.setEnabled(false);
        //  stopButton.setEnabled(true);
    }

    private void stopAudioRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            // mediaRecorder.reset();
            mediaRecorder = null;

        }
    }
        // stopButton.setEnabled(false);


    public void onSentAction(View view)
    {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = com.whk.spa.aes.messagebox.getText().toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }


    public void clearAudioEnc(View view)
    { if(mp!=null)mp.release();

        mp=null;
        audiopath="";
        audiobytedata=null;
        audiobyte=null;
        encrypt_audio.playpause.setEnabled(false);

    }

    public void clearAudioDec(View view)
    {
//        mp.release();
        if(mp!=null)mp.release();
        mp=null;
        audiopath="";
        audiobytedata=null;
        audiobyte=null;
        decrypt_audio.playpause.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cover_image_menu:
                Toast.makeText(this, "This is teh option help", Toast.LENGTH_LONG).show();
                break;
            case R.id.cover_audio_menu:
                Toast.makeText(this, "This is teh option help", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }

}
