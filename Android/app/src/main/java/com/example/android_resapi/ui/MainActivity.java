package com.example.android_resapi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android_resapi.R;


public class MainActivity extends AppCompatActivity {
    final static String TAG = "AndroidAPITest";
    String getLogsURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLogsURL = "https://z7oyy1oz62.execute-api.ap-northeast-2.amazonaws.com/trashCan/"; //API URL

        //디바이스 로그 조회 버튼 클릭 이벤트 조회
        Button listLogsBtn = findViewById(R.id.listLogsBtn);
        listLogsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                intent.putExtra("getLogsURL", getLogsURL);
                startActivity(intent);
            }
        });

        //차트 보기 버튼 클릭 이벤트 정의
        Button chartBtn = findViewById(R.id.listChart);
        chartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("getLogsURL", getLogsURL);
                startActivity(intent);
            }
        });

    }
}


