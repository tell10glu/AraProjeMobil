package com.tll.araproje.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tll.araproje.R;
import com.tll.araproje.service.Urls;

/**
 * Created by abdullahtellioglu on 01/01/16.
 */
public class ServerIpDialog extends Dialog {
    private Button confirm;
    private EditText editText;
    public ServerIpDialog(Context context) {
        super(context);
        setCancelable(false);
        setTitle("Enter Server URL!");
        setContentView(R.layout.dialog_server_ip);
        confirm = (Button)findViewById(R.id.dialog_server_button);
        editText = (EditText)findViewById(R.id.dialog_server_ip_text);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Urls.getInstance(editText.getText().toString());
                dismiss();
            }
        });

    }

}
