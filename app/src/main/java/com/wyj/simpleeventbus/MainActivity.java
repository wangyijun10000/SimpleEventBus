package com.wyj.simpleeventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wyj on 2018/10/25.
 */
public class MainActivity extends AppCompatActivity {
    private TextView mMessageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessageTv = findViewById(R.id.message_tv);
        EventBus.getDefault().register(this);
        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setText(System.currentTimeMillis() + "\n");
                EventBus.getDefault().post(message);
            }
        });
    }

    @Subscribe
    public void onReceive(Message message) {
        mMessageTv.append("接收到一条消息：" + message.getText());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
