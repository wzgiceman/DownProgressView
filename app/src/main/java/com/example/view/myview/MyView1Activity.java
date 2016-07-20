package com.example.view.myview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.view.myview.view.DownProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyView1Activity extends AppCompatActivity implements DownProgressView.StateProgressListner {
    @BindView(R.id.view1)
    DownProgressView view1;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_view1);
        ButterKnife.bind(this);
        view1.setListner(this);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            view1.setmProgress(++progress);
            handler.sendEmptyMessageDelayed(1, 20);
        }

    };

    @Override
    public void onstart() {
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onstop() {
        handler.removeMessages(1);
    }

    @Override
    public void onfinish() {
        progress = 0;
        Toast.makeText(this, "下载完成了!继续开始", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
        view1.onDestory();
    }
}
