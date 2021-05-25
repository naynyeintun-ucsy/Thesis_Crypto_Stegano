package com.whk.spa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chibde.visualizer.LineVisualizer;
import com.ohoussein.playpause.PlayPauseView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link decrypt_audio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link decrypt_audio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class decrypt_audio extends Fragment {

    public static LineVisualizer vitualizer;
    public static PlayPauseView playpause;
    static Button execute,clear;
    public static TextView securitycheckbox;
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

    public decrypt_audio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment decrypt_audio.
     */
    // TODO: Rename and change types and number of parameters
    public static decrypt_audio newInstance(String param1, String param2) {
        decrypt_audio fragment = new decrypt_audio();
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
        View v= inflater.inflate(R.layout.fragment_decrypt_audio, container, false);

        playpause=(PlayPauseView)v.findViewById(R.id.audiodecplay);
        securitycheckbox=(TextView)v.findViewById(R.id.audiodeccheckbox);
        //record=(Button)v.findViewById(R.id.audioencrecord);
       // loadfile=(Button)v.findViewById(R.id.audioencload);
        execute=(Button)v.findViewById(R.id.audiodecexecute);
        clear=(Button)v.findViewById(R.id.audiodecclear);
        skbar=(SeekBar)v.findViewById(R.id.seekbardec);
        vitualizer=(LineVisualizer)v.findViewById(R.id.audiodecVirtuallizer);
        password=(EditText)v.findViewById(R.id.audiodecpassword);





        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
