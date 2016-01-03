package com.tll.araproje.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tll.araproje.R;
import com.tll.araproje.constants.InternalConstants;
import com.tll.araproje.dialogs.MessageDialog;
import com.tll.araproje.dialogs.ServerIpDialog;
import com.tll.araproje.utils.SharedPreferenceUtils;


public class MainActivity extends Activity {
    public static final boolean isDebugMode = true;
    public static final int DEFAULT_COIN_COUNT = 100;
    private TextView txtViewNewGame,txtViewCollectCoin,txtViewExit,txtTotalCoin;
    int currentCoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtViewNewGame = (TextView)findViewById(R.id.main_new_game);
        txtViewNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentCoin<=0){
                    MessageDialog dialog = new MessageDialog(MainActivity.this,getString(R.string.collect_coin_message));
                    dialog.show();
                }else{
                    startActivity(new Intent(MainActivity.this,GameActivity.class));
                }

            }
        });
        txtViewCollectCoin = (TextView)findViewById(R.id.main_collect_coin);
        txtViewCollectCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CoinActivity.class));
            }
        });
        txtViewExit = (TextView)findViewById(R.id.main_exit);
        txtViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
        txtTotalCoin = (TextView)findViewById(R.id.main_total_coin);
        txtTotalCoin.setText(String.valueOf(SharedPreferenceUtils.readInteger(this, InternalConstants.COIN_COUNT)));
        if(isDebugMode){
            ServerIpDialog dialog = new ServerIpDialog(this);
            dialog.show();
        }
        if(SharedPreferenceUtils.readInteger(this, InternalConstants.COIN_COUNT)==-1){
            SharedPreferenceUtils.saveInteger(this,InternalConstants.COIN_COUNT,DEFAULT_COIN_COUNT);
        }else{
            txtTotalCoin.setText(String.valueOf(SharedPreferenceUtils.readInteger(this,InternalConstants.COIN_COUNT)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentCoin = SharedPreferenceUtils.readInteger(this,InternalConstants.COIN_COUNT);
        txtTotalCoin.setText(String.valueOf(currentCoin));
    }
}
