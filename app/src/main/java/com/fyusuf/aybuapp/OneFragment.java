package com.fyusuf.aybuapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OneFragment extends Fragment{
    private TextView tarih,yemek1,yemek2,yemek3,yemek4;
    private ProgressDialog progressDialog;
    private static String URL = "http://ybu.edu.tr/sks/";

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        tarih = (TextView) view.findViewById(R.id.tarih);
        yemek1 = (TextView) view.findViewById(R.id.yemek1);
        yemek2 = (TextView) view.findViewById(R.id.yemek2);
        yemek3 = (TextView) view.findViewById(R.id.yemek3);
        yemek4 = (TextView) view.findViewById(R.id.yemek4);
        if(isOnline())
            new Yemek().execute();
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Can't connect to the Internet!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private class Yemek extends AsyncTask<Void, Void, Void> {
        String trh,ymk1,ymk2,ymk3,ymk4;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("AybuApp");
            progressDialog.setMessage("Veri Çekiliyor...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Document doc  = Jsoup.connect(URL).get();
                Element table = doc.select("table").get(0);
                Elements rows = table.select("td");
                if(rows.size() > 0){
                    trh = rows.get(2).text();
                    ymk1 = rows.get(3).text();
                    ymk2 = rows.get(4).text();
                    ymk3 = rows.get(5).text();
                    ymk4 = rows.get(6).text();
                }
                else{
                    trh = "tarih";
                    ymk1 = "yemek bulunamadı";
                    ymk2 = "yemek bulunamadı";
                    ymk3 = "yemek bulunamadı";
                    ymk4 = "yemek bulunamadı";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tarih.setText(trh);
            yemek1.setText(ymk1);
            yemek2.setText(ymk2);
            yemek3.setText(ymk3);
            yemek4.setText(ymk4);
            progressDialog.dismiss();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}


