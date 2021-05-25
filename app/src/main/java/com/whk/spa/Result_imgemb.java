package com.whk.spa;

/**
 * Created by Naylay on 01/26/18.
 */



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Result_imgemb extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static ImageView outputimg;
    static  Button share,save,back;
    byte[] byteArray;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Result_imgemb() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static encrypt_text newInstance(String param1, String param2) {
        encrypt_text fragment = new encrypt_text();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            byteArray=getArguments().getByteArray("image");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.activity_result, container, false);
        save=(Button)view.findViewById(R.id.saveToI);
        share=(Button)view.findViewById(R.id.share);
        back=(Button)view.findViewById(R.id.backTtoI);
        outputimg=(ImageView)view.findViewById(R.id.outputImageView);

        //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        outputimg.setImageBitmap(Bitmap.createScaledBitmap(MainActivity.embeddedImage, MainActivity.inputimgwidth, MainActivity.inputimgheight, false));
        return  view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
