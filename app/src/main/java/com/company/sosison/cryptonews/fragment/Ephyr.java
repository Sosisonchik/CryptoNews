package com.company.sosison.cryptonews.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.company.sosison.cryptonews.R;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Ephyr.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Ephyr#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ephyr extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences sp;
    float price = 0.0f;
    int color;
    private static final String KEY_P = "price_eth";
    private static final String KEY_CH = "change_eth";
    private static final String KEY_C = "color_eth";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Ephyr() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ephyr.
     */
    // TODO: Rename and change types and number of parameters
    public static Ephyr newInstance(String param1, String param2) {
        Ephyr fragment = new Ephyr();
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
        return inflater.inflate(R.layout.fragment_ephyr, container, false);
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
        ((TextView)v.findViewById(R.id.eth_price_view)).setText(getPrice());
        ((TextView)v.findViewById(R.id.eth_change_view)).setText(getChange());
        ((TextView)v.findViewById(R.id.eth_change_view)).setTextColor(getColor());
        FloatingActionButton fb = v.findViewById(R.id.fb_eth);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Http().execute();
                Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
                ((CircleImageView)v.findViewById(R.id.eth_con_view)).startAnimation(animation);
            }
        });
    }

    private class Http extends AsyncTask<String,String,String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            View v = getView();
            TextView changeView = v.findViewById(R.id.eth_change_view);
            TextView priceView = v.findViewById(R.id.eth_price_view);
            if (!priceView.getText().toString().equals("Push the button")){
                float current = Float.valueOf(s);
                float last = Float.valueOf(priceView.getText().toString());
                float change = current-last;
                if (change>0){
                    color = getResources().getColor(R.color.up);
                    changeView.setTextColor(getResources().getColor(R.color.up));
                    changeView.setText("+"+String.valueOf(change));
                }else if (change==0.0){
                    color = getResources().getColor(R.color.colorPrimaryDark);
                    changeView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    changeView.setText("+0");
                }else {
                    color = getResources().getColor(R.color.down);
                    changeView.setTextColor(getResources().getColor(R.color.down));
                    changeView.setText(String.valueOf(change));
                }
            }
            save(s,changeView.getText().toString(),color);
            priceView.setText(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://yobit.net/api/2/eth_usd/ticker");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line=in.readLine())!=null){
                    sb.append(line);
                }
                String json = sb.toString();
                JSONParser parser = new JSONParser();
                org.json.simple.JSONObject obj = (org.json.simple.JSONObject)parser.parse(json);
                org.json.simple.JSONObject ticker = (org.json.simple.JSONObject)obj.get("ticker");
                price = Float.valueOf(ticker.get("high").toString());
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

    private void save(String price,String change,int color){
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(KEY_P,price);
        ed.putString(KEY_CH,change);
        ed.putInt(KEY_C,color);
        ed.commit();
    }

    public String getPrice() {
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sp.getString(KEY_P,"Push the button");

    }

    public int getColor() {
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sp.getInt(KEY_C,getResources().getColor(R.color.colorPrimaryDark));
    }

    public String getChange(){
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        return sp.getString(KEY_CH,"+0");
    }
}
