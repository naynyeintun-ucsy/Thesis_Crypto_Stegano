package com.whk.spa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.whk.spa.core.AES;
import com.whk.spa.core.EncDecBS;


public class aes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
      static   EditText messagebox;
    EditText password;
    Button encrypt,decrypt,share;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public aes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment aes.
     */
    // TODO: Rename and change types and number of parameters
    public static aes newInstance(String param1, String param2) {
        aes fragment = new aes();
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

        View v= inflater.inflate(R.layout.fragment_aes, container, false);
        messagebox=(EditText)v.findViewById(R.id.edittextmsgboxaes);
        password=(EditText)v.findViewById(R.id.editTextPasswordaes);
        encrypt=(Button)v.findViewById(R.id.encryptaes);
        decrypt=(Button)v.findViewById(R.id.decryptaes);
        share=(Button)v.findViewById(R.id.shareaes);


        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass=password.getText().toString();
                if (pass.equals("") || pass.equals(null))
                {
                    Toast.makeText(getActivity(),"Enter your key to encrypt", Toast.LENGTH_SHORT).show();
                }
                else {
                    AES aes=new AES(pass);
                    EncDecBS eanddcoder=new EncDecBS();

                    try
                    {
                        byte[] output=aes.encrypt(messagebox.getText().toString().getBytes());
                        messagebox.setText(eanddcoder.encode(output));

                        Toast.makeText(getActivity(),"AES Encryption Process is successfully Complete",Toast.LENGTH_LONG).show();
                    }catch (Exception e)
                    {
                        Log.e("eroor in encode -->", String.valueOf(e));
                    }
                }

            }
        });


        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pass=password.getText().toString();
                String message=messagebox.getText().toString();

                if (message.equals("")|| message.equals(null))
                {
                    Toast.makeText(getActivity(),"Enter your message to decrrypt", Toast.LENGTH_SHORT).show();
                }
                else if (pass.equals("") || pass.equals(null))
                {
                    Toast.makeText(getActivity(),"Enter your key to decrypt", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    AES aesalgorithm=new AES(pass);
                    EncDecBS eanddcoder=new EncDecBS();

                    try
                    {
                        byte[] output=aesalgorithm.decrypt(eanddcoder.decode(String.valueOf(messagebox.getText())));

                        messagebox.setText(new String(output));
                        Toast.makeText(getActivity(),"AES Decryption Process is successfully Complete",Toast.LENGTH_LONG).show();
                    }catch (Exception e)
                    {
                        Toast.makeText(getActivity(),"Wrong Password TryAgain!!!",Toast.LENGTH_SHORT).show();
                        Log.e("eroor in encode -->", String.valueOf(e));
                    }

                }

            }
        });



        return  v;
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
