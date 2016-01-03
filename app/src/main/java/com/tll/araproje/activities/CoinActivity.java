package com.tll.araproje.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tll.araproje.R;
import com.tll.araproje.constants.InternalConstants;
import com.tll.araproje.service.Urls;
import com.tll.araproje.utils.SharedPreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by abdullahtellioglu on 01/01/16.
 */
public class CoinActivity extends Activity implements View.OnClickListener {
    private static final int INCREASE_VALUE_PER_SELECT = 5;
    private TextView txtWord,txtCoin;
    private Button[] buttons;
    private int totalCoin;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);
        requestQueue = Volley.newRequestQueue(this);
        buttons = new Button[3];
        for(int i =0;i<buttons.length;i++){
            buttons[i].setOnClickListener(this);
        }
        fetchData();
    }
    private void fetchData(){
        JsonArrayRequest req1 = new JsonArrayRequest(Request.Method.GET, Urls.getInstance().getRandomWordUrl(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray arr) {
                try {
                    JSONObject obj1 = arr.getJSONObject(0);
                    JSONObject w1 = obj1.getJSONObject("word1");
                    String w1Str = w1.getString("word");
                    txtWord.setText(w1Str);
                    String[] words = new String[arr.length()];
                    for(int i =0;i<arr.length();i++){
                        JSONObject wordFreq = arr.getJSONObject(i);
                        JSONObject word2 = wordFreq.getJSONObject("word2");
                        String wordStr = word2.getString("word");
                        words[i] = wordStr;
                    }
                    for(int i =0;i<3;i++){
                        int rnd = new Random().nextInt(words.length);
                        while(words[rnd]==null){
                            rnd =new Random().nextInt(words.length);
                        }
                        buttons[i].setText(words[rnd]);
                        words[rnd] = null;
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(req1);
    }
    public int addCoin(int total,int extra){
        total += extra;
        SharedPreferenceUtils.saveInteger(this, InternalConstants.COIN_COUNT, total);
        return total;
    }

    @Override
    public void onClick(View view) {

    }
}
