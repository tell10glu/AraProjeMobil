package com.tll.araproje.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tll.araproje.R;
import com.tll.araproje.constants.InternalConstants;
import com.tll.araproje.dialogs.MessageDialog;
import com.tll.araproje.service.Urls;
import com.tll.araproje.utils.SharedPreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by abdullahtellioglu on 01/01/16.
 */
public class GameActivity extends Activity {
    private static final int COIN_DECREASE_PER_CLICK = 2;
    private static final int WINNING_PRIZE = 250;
    private RequestQueue requestQueue;
    private TextView coinTextView,firstWord,finalWord,currentWord;
    private GameActivityArrayAdapter adapter ;
    private ListView listView;
    private ArrayList<String> listSubWords;
    int totalCoin;
    private  MessageDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        requestQueue = Volley.newRequestQueue(this);
        coinTextView = (TextView)findViewById(R.id.game_activity_total_coin);
        listView = (ListView)findViewById(R.id.game_activity_words);
        firstWord = (TextView)findViewById(R.id.game_activity_first_word);
        finalWord = (TextView)findViewById(R.id.game_activity_final_word);
        currentWord = (TextView)findViewById(R.id.game_activity_current_word);
        coinTextView = (TextView)findViewById(R.id.game_activity_total_coin);
        listSubWords = new ArrayList<>();
        adapter = new GameActivityArrayAdapter(this,R.layout.row_game_activity_list,listSubWords);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (totalCoin > 0) {
                    totalCoin = addCoin(totalCoin, -COIN_DECREASE_PER_CLICK);
                    coinTextView.setText(String.valueOf(totalCoin));
                    if (isGameEnded(listSubWords.get(i), finalWord.getText().toString())) {
                        dialog = new MessageDialog(GameActivity.this, getString(R.string.win_message), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                startGame();

                            }
                        });
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                dialog.dismiss();
                                GameActivity.this.finish();
                            }
                        });
                        dialog.show();
                        coinTextView.setText(String.valueOf(addCoin(totalCoin, WINNING_PRIZE)));
                        finish();
                    } else {
                        currentWord.setText(new String(listSubWords.get(i)));
                        fetchSubWords(currentWord.getText().toString());
                    }
                } else {
                    dialog = new MessageDialog(GameActivity.this, getString(R.string.lose_message), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            GameActivity.this.finish();
                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            GameActivity.this.finish();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                adapter.notifyDataSetChanged();
            }
        });
        setCoin();
        startGame();
        showTutorialDialogIfNecessary();
    }
    public int addCoin(int total,int extra){
        total += extra;
        SharedPreferenceUtils.saveInteger(this,InternalConstants.COIN_COUNT,total);
        return total;
    }
    private boolean isGameEnded(String word,String finalWord){
        if(word.equals(finalWord))
            return true;
        return false;
    }
    private void setCoin(){
        totalCoin = SharedPreferenceUtils.readInteger(this, InternalConstants.COIN_COUNT);
        if(totalCoin==-1)
            totalCoin = 100;
        coinTextView.setText(String.valueOf(totalCoin));
    }
    private void startGame(){
        setCoin();
        JsonArrayRequest req1 = new JsonArrayRequest(Request.Method.GET, Urls.getInstance().getRandomWordUrl(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray arr) {
                try {
                    JSONObject obj1 = arr.getJSONObject(0);
                    JSONObject w1 = obj1.getJSONObject("word1");
                    String w1Str = w1.getString("word");
                    firstWord.setText(w1Str);
                    currentWord.setText(w1Str);
                    for(int i =0;i<arr.length();i++){
                        JSONObject wordFreq = arr.getJSONObject(i);
                        JSONObject word2 = wordFreq.getJSONObject("word2");
                        String wordStr = word2.getString("word");
                        listSubWords.add(wordStr);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // error.printStackTrace();
                firstWord.setText(UUID.randomUUID().toString().substring(0,6));
                currentWord.setText(UUID.randomUUID().toString().substring(0,6));
                for(int i =0;i<15;i++){
                    listSubWords.add(UUID.randomUUID().toString().substring(0,6));
                }
                adapter.notifyDataSetChanged();
            }
        });
        JsonArrayRequest req2 = new JsonArrayRequest(Request.Method.GET, Urls.getInstance().getRandomWordUrl(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray arr) {
                try {
                    JSONObject obj1 = arr.getJSONObject(0);
                    JSONObject w1 = obj1.getJSONObject("word1");
                    String w1Str = w1.getString("word");
                    finalWord.setText(w1Str);
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error.printStackTrace();
                finalWord.setText(UUID.randomUUID().toString().substring(0,6));
            }
        });
        requestQueue.add(req1);
        requestQueue.add(req2);
    }
    private void fetchSubWords(String word){
        listSubWords.clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Urls.getInstance().getWordUrl(word), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray arr) {
                for(int i  =0;i<arr.length();i++){
                    try {
                        JSONObject wordFreq = arr.getJSONObject(i);
                        JSONObject word2 = wordFreq.getJSONObject("word2");
                        String wordStr = word2.getString("word");
                        listSubWords.add(wordStr);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Random random = new Random();
                for(int i =0;i<random.nextInt(15)+5;i++){
                    listSubWords.add(UUID.randomUUID().toString());
                }
                adapter.notifyDataSetChanged();
            }
        });
        requestQueue.add(request);
    }
    private void showTutorialDialogIfNecessary(){
        int gameOpenCount = SharedPreferenceUtils.readInteger(this,InternalConstants.GAME_COUNT);
        if(gameOpenCount<3){
            SharedPreferenceUtils.saveInteger(this,InternalConstants.GAME_COUNT,++gameOpenCount);
            MessageDialog dialog1 = new MessageDialog(this,getString(R.string.game_tutorial));
            dialog1.show();
        }
    }
    private static class ViewHolder{
        TextView btn;
    }
    private class GameActivityArrayAdapter extends ArrayAdapter<String>{
        private List<String> listObjects;
        public GameActivityArrayAdapter(Context context, int resource,List<String> listObjects) {
            super(context, resource,listObjects);
            this.listObjects = listObjects;
        }
        @Override
        public View getView(int i, View v, ViewGroup parent) {
            if(v==null){
                v = LayoutInflater.from(GameActivity.this).inflate(R.layout.row_game_activity_list, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.btn = (TextView)v.findViewById(R.id.row_game_list_button);
                v.setTag(holder);
            }
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.btn.setText(listObjects.get(i));
            return v;
        }
    }

}
