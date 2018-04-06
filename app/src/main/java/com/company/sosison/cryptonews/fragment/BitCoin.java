package com.company.sosison.cryptonews.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.sosison.cryptonews.R;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BitCoin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BitCoin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BitCoin extends Fragment {
    static float price= 0.0f;
    SharedPreferences sp;
    final static String KEY_1 = "price_bit";
    final static String KEY_2 = "change_bit";
    final static String KEY_3 = "color_bit";
    static final String URL1 = "https://newsapi.org/v2/everything?q=bitcoin&sortBy=publishedAt&apiKey=";
    final static String API = "6a6670fe63144d08b80f42b521f83ecb";
    public static final String URLY = "https://yobit.net/api/2/btc_usd/ticker";
    String author;
    String description;
    String title;
    String imageUrl;
    String postUrl;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public BitCoin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BitCoin.
     */
    // TODO: Rename and change types and number of parameters
    public static BitCoin newInstance(String param1, String param2) {
        BitCoin fragment = new BitCoin();
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
        return inflater.inflate(R.layout.fragment_bit_coin, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    @Override
    public void onStart() {
        super.onStart();
        final View v = getView();
        TextView bit_price = v.findViewById(R.id.bit_price_view);
        TextView change = v.findViewById(R.id.bit_change_view);
        change.setText(loadS());
        change.setTextColor(loadC());
        bit_price.setText(loadF());
        FloatingActionButton fb = v.findViewById(R.id.fb_bit);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CircleImageView bit = v.findViewById(R.id.bit_con_view);
                Animation rotate = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
                bit.setAnimation(rotate);
                new Request().execute();
            }
        });
    }

    private class Request extends AsyncTask<String,String,String>{

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            View view = getView();
            TextView text = view.findViewById(R.id.bit_price_view);
            TextView pc = view.findViewById(R.id.bit_change_view);

            if (text.getText()!=null && !text.getText().toString().equals("Push the button")){
                float current = Float.valueOf(s);
                float last = Float.valueOf(text.getText().toString());
                float percent = current-last;
                int color;
                if (current>last){
                    pc.setText("+"+String.valueOf(percent));
                    pc.setTextColor(getResources().getColor(R.color.up));
                    color = getResources().getColor(R.color.up);
                }
                else if (current==last){
                    pc.setText("+0");
                    pc.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    color = getResources().getColor(R.color.colorPrimaryDark);
                }
                else{
                    pc.setText(String.valueOf(percent));
                    pc.setTextColor(getResources().getColor(R.color.down));
                    color = getResources().getColor(R.color.down);
                }

                save(s,pc.getText().toString(),color);
                }

            text.setText(s);
            }




        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://yobit.net/api/2/btc_usd/ticker");
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line=in.readLine())!=null){
                    sb.append(line);
                }
                String json = sb.toString();
                JSONParser parser = new JSONParser();
                org.json.simple.JSONObject obj = (org.json.simple.JSONObject)parser.parse(json);
                org.json.simple.JSONObject ticker = (org.json.simple.JSONObject) obj.get("ticker");
                return ticker.get("high").toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }
    }



    private void save(String str,String str2,int color){
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(KEY_1,str);
        ed.putString(KEY_2,str2);
        ed.putInt(KEY_3,color);
        ed.commit();
    }

    private String loadF(){
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sp.getString(KEY_1,"Push the button");
    }

    private String loadS(){
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        return  sp.getString(KEY_2,"Emp");
    }

    private int loadC(){
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sp.getInt(KEY_3,R.color.up);
    }


}
