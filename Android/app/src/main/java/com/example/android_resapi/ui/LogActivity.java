package com.example.android_resapi.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android_resapi.R;
import com.example.android_resapi.ui.apicall.GetLog;

import java.util.Calendar;

public class LogActivity extends AppCompatActivity {
    //전역 변수 선언부
    String getLogsURL;
    private TextView textView_Date1;
    private TextView textView_Time1;
    private TextView textView_Date2;
    private TextView textView_Time2;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    final static String TAG = "AndroidAPITest";
    String tmpUrlStr;
    Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_log);

        //1. url 셋팅 -> 처음 화면은 trashCanA로 띄워주기로 함
        Intent intent = getIntent();
        getLogsURL = intent.getStringExtra("getLogsURL");
        tmpUrlStr = getLogsURL; //메인에서 온 url이 변형되는것을 방지하기 위해 tmp에 저장
        Log.i(TAG, "getLogsURL="+getLogsURL); //잘 전달되는지 test print
        getLogsURL = getLogsURL + "trashCanA/log";


        //2. 기간 및 시간 셋팅 -> 초기값은 1970 1월 1일 0시 0분 0초로 셋팅
        //현재 년도, 월, 일, 시간, 분을 정의함
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        //이전 날짜 ~ 현재 날짜 기록을 보는 것이므로 날짜 -1 (이틀 전)으로 셋팅
        int day_cur = calendar.get(Calendar.DATE);
        calendar.add(Calendar.DATE, -1);
        int day = calendar.get(Calendar.DATE);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        textView_Date1 = (TextView)findViewById(R.id.textView_date1);
        textView_Time1 = (TextView)findViewById(R.id.textView_time1);
        textView_Date2 = (TextView)findViewById(R.id.textView_date2);
        textView_Time2 = (TextView)findViewById(R.id.textView_time2);

        //기존 -> 사용자의 버튼을 입력 받으면 비동기식으로 db에서 가져오는거
        //수정 -> 입력 안해도 화면 전환과 동시에 값들 가져와서 바로 띄워주는거
        //GetLog로 넘겨줄 때, 액티비티 자체의 주솟값을 넘겨줌 -> 텍스트 뷰 등의 값들이 같이 넘어감.
        textView_Date1.setText(String.format("%d-%d-%d ", 1970 ,1, 1));
        textView_Time1.setText(String.format("%d:%d", 12, 0));
        textView_Date2.setText(String.format("%d-%d-%d ", year ,month+1, day_cur));
        textView_Time2.setText(String.format("%d:%d", hourOfDay, minute));

        //화면 전환하자마자 바로 api 호출하여 로그 띄워줌
        new GetLog(LogActivity.this,getLogsURL).execute();

        Button kiosk_A_btn = findViewById(R.id.kiosk_A_btn);
        kiosk_A_btn.setEnabled(true);
        Button kiosk_B_btn = findViewById(R.id.kiosk_B_btn);
        kiosk_B_btn.setEnabled(true);

        kiosk_A_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLogsURL = tmpUrlStr + "trashCanA/log"; //a를 누르고 b를 눌렀을 경우, 쿼리문이 계속 쌓이는 것을 방지하기 위해
            }
        });

        kiosk_B_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLogsURL = tmpUrlStr + "trashCanB/log";
            }
        });

        //시작 날짜 정하는 버튼
        Button startDateBtn = findViewById(R.id.start_date_button);


        //자동으로 현재 날짜 -1로 셋팅되어있게 코드 수정
        //달은 특이하게도 인덱스 0부터 시작함. 따라서 1을 반드시 더해줘야
        textView_Date1.setText(String.format("%d-%d-%d ", year ,month+1, day));

        //사용자가 조회 시작 날짜 선택 버튼을 누르면은 수정가능하게끔
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackMethod = new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        textView_Date1.setText(String.format("%d-%d-%d ", year ,monthOfYear+1,dayOfMonth));
                    }
                };
                //day picker를 현재 년도, 월, 일-2(이전 로그까지 보려고)로 초기화함.
                DatePickerDialog dialog = new DatePickerDialog(LogActivity.this, callbackMethod, year, month, day);
                dialog.show();
            }
        });

        Button startTimeBtn = findViewById(R.id.start_time_button);
        //사용자가 바꾸지 않으면 초기화되는 값들 셋팅
        textView_Time1.setText(String.format("%d:%d", hourOfDay, minute));

        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textView_Time1.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                };

                TimePickerDialog dialog = new TimePickerDialog(LogActivity.this, listener, hourOfDay, minute, true);
                dialog.show();

            }
        });


        Button endDateBtn = findViewById(R.id.end_date_button);
        textView_Date2.setText(String.format("%d-%d-%d ", year ,month+1, day_cur));
        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackMethod = new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        textView_Date2.setText(String.format("%d-%d-%d ", year ,monthOfYear+1,dayOfMonth));
                    }
                };
                //day picker를 현재 년도, 월, 일로 초기화함.
                DatePickerDialog dialog = new DatePickerDialog(LogActivity.this, callbackMethod, year, month, day_cur);

                dialog.show();


            }
        });

        //종료 시간 세팅
        Button endTimeBtn = findViewById(R.id.end_time_button);
        //사용자 바꾸기 전에 사전 세팅
        textView_Time2.setText(String.format("%d:%d", hourOfDay, minute));
        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textView_Time2.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                };
                TimePickerDialog dialog = new TimePickerDialog(LogActivity.this, listener, hourOfDay, minute, true);
                dialog.show();

            }
        });

        Button start = findViewById(R.id.log_start_button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetLog(LogActivity.this,getLogsURL).execute();
            }
        });
    }
}
