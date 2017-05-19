package com.fyusuf.aybuapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;


public class ThreeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> urlList;
    private ArrayList<String> news;
    private ProgressDialog progressDialog;
    private static String URL = "http://ybu.edu.tr/muhendislik/bilgisayar/";
    public ThreeFragment() {
        // Required empty public constructor
    }

    public void onItemClick(AdapterView<?> parent, View view, int index, long id) {

        //Toast.makeText(getActivity(), urlList.get(index),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlList.get(index)));
        startActivity(intent);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_three, container, false);
        list = (ListView) view.findViewById(R.id.listView2);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        if(isOnline())
            new Announcement().execute();
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Can't connect to the Internet!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private class Announcement extends AsyncTask<Void, Void, Void> {


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
                news = new ArrayList<>();
                urlList = new ArrayList<>();
                Document doc = Jsoup.connect(URL).get();
                Elements announcement = doc.select("div.contentNews div.cncItem");
                Elements links = doc.select("div.contentNews div.cncItem a[href]");
                for(int i = 0; i < announcement.size();i++) {
                    news.add(announcement.get(i).text());
                }
                news.add("TÜM HABERLER");
                for (Element link : links) {
                    String url = "http://ybu.edu.tr/muhendislik/bilgisayar/" + link.attr("href");
                    urlList.add(url);
                    //System.out.println(url);
                }
                urlList.add("http://www.ybu.edu.tr/muhendislik/bilgisayar/content_list-314-haberler.html");

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ArrayAdapter<>(getActivity(), R.layout.list_layout,R.id.content, news);
            list.setAdapter(adapter);
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
