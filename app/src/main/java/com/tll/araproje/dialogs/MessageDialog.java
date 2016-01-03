package com.tll.araproje.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tll.araproje.R;

/**
 * Created by abdullahtellioglu on 02/01/16.
 */
public class MessageDialog  extends Dialog implements View.OnClickListener {
    private String message;
    private TextView dismissView,messageView;


    public MessageDialog(Context context,String message,View.OnClickListener clickListener) {
        super(context);
        this.message = message;
        init();
        messageView.setOnClickListener(clickListener);
        dismissView.setOnClickListener(this);
    }
    public MessageDialog(Context context,String message) {
        super(context);
        this.message = message;
        init();
        messageView.setOnClickListener(this);
        dismissView.setOnClickListener(this);

    }
    private void init(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_message);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        messageView = (TextView)findViewById(R.id.dialog_message_text);
        dismissView = (TextView)findViewById(R.id.dialog_message_dismiss);
        messageView.setText(message);
        setCancelable(true);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
