package com.whk.spa;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import com.chibde.visualizer.LineVisualizer;
import com.ohoussein.playpause.PlayPauseView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link encrypt_audio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link encrypt_audio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class encrypt_audio extends Fragment {

    public static LineVisualizer vitualizer;
    public static PlayPauseView playpause;
     static Button record,loadfile,execute,clear;
    public static CheckBox securitycheckbox;
    public static SeekBar skbar;
    public static EditText password;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public encrypt_audio() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static encrypt_audio newInstance(String param1, String param2) {
        encrypt_audio fragment = new encrypt_audio();
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
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_encrypt_audio, container, false);
        playpause=(PlayPauseView)v.findViewById(R.id.audioencplay);
        securitycheckbox=(CheckBox)v.findViewById(R.id.audioenccheckbox);
        record=(Button)v.findViewById(R.id.audioencrecord);
        loadfile=(Button)v.findViewById(R.id.audioencload);
        execute=(Button)v.findViewById(R.id.audioencexecute);
        clear=(Button)v.findViewById(R.id.audioencclear);
        skbar=(SeekBar)v.findViewById(R.id.seekbar1);
        vitualizer=(LineVisualizer)v.findViewById(R.id.audioencVirtuallizer);
        password=(EditText)v.findViewById(R.id.audioencpassword);
       // final PlayPauseView button = (PlayPauseView) v.findViewById(R.id.play);
        //ineVisualizer lineVisualizer=(LineVisualizer)v.findViewById(R.id.visualizer);
      //  final  MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);
      /*  button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.toggle();
                if (button.isPlay()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            }
        });*/
// set custom color to the line.
     vitualizer.setColor(ContextCompat.getColor(getActivity(), R.color.red));

// set the line with for the visualizer between 1-10 default 1.
       vitualizer.setStrokeWidth(3);

// Set you media player to the visualizer.
       // lineVisualizer.setPlayer(mediaPlayer);
      //  mediaPlayer.start();

        return v;
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
