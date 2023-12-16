package com.example.android_resapi.ui.apicall;
import java.util.ArrayList;
import java.util.Collections;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import com.example.android_resapi.R;
import com.example.android_resapi.httpconnection.GetRequest;
import com.example.android_resapi.ui.ChartActivity;
import com.example.android_resapi.ui.LogActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


public class GetLog extends GetRequest {
    final static String TAG = "AndroidAPITest";
    String urlStr;

    public GetLog(Activity activity, String urlStr) {
        super(activity);
        this.urlStr = urlStr;
        this.activity = activity;
    }

    ArrayList<Tag> output = new ArrayList<Tag>();

    @Override
    protected void onPreExecute() {
        try {
            //사용자가 택한 dialog 날짜와 시간대로 뷰를 세팅
            TextView textView_Date1 = activity.findViewById(R.id.textView_date1);
            TextView textView_Time1 = activity.findViewById(R.id.textView_time1);
            TextView textView_Date2 = activity.findViewById(R.id.textView_date2);
            TextView textView_Time2 = activity.findViewById(R.id.textView_time2);

            //url 쿼리문 만들어줌 
            String params = String.format("?from=%s:00&to=%s:00",textView_Date1.getText().toString()+textView_Time1.getText().toString(),
                                                            textView_Date2.getText().toString()+textView_Time2.getText().toString());

            Log.i(TAG,"urlStr="+urlStr+params); //url 잘 만들어졌는지 테스트 프린트 
            url = new URL(urlStr+params);

        } catch (MalformedURLException e) { //url 예외처리 
            Toast.makeText(activity,"URL is invalid:"+urlStr, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        TextView message = activity.findViewById(R.id.message2);
        message.setText("조회중..."); //조회 시간 지연 고려한 텍스트 
    }

    @Override
    protected void onPostExecute(String jsonString) {
        TextView message = activity.findViewById(R.id.message2);
        if (jsonString == null) {
            message.setText("로그 없음");
            return;
        }
        message.setText("");
        ArrayList<Tag> arrayList = getArrayListFromJSONString(jsonString);

        //Log액티비티 에서 호출했다면 로그 띄움
        if(activity instanceof LogActivity)
        {
            ListView txtList = activity.findViewById(R.id.logList);
            // 최근 순서대로(리스트 역순으로)
            Collections.reverse(arrayList);

            final ArrayAdapter adapter = new ArrayAdapter(activity,
                    android.R.layout.simple_list_item_1,
                    arrayList.toArray());
            txtList.setAdapter(adapter);
            txtList.setDividerHeight(10);
        }

        //chart 액티비티에서 호출했다면 차트 띄움
        if(activity instanceof ChartActivity)
        {
            LineChart chart = (LineChart)activity.findViewById(R.id.chart);
            //비율값들 추가할 ratioValue arrayList 생성
            ArrayList <Entry> ratioValue = new ArrayList<>();

            for (Tag item : output) {
                // 각 항목에서 필요한 데이터 추출
                float timestamp = Float.parseFloat(item.Time); //Time값 밀리 세컨드
                float ratio = Float.parseFloat(item.Ratio); //비율 값 -> float형변환
                ratioValue.add(new Entry(timestamp, ratio)); //ratioValue ArrayList에 (시간, 비율) 값들 for문 돌면서 저장 
            }
            // LineDataSet 객체 생성
            LineDataSet set = new LineDataSet(ratioValue, "Ratio"); //데이터셋으로 앞서 선언한 arrayList 쓰겠다고 정의
            set.setDrawIcons(false); //아이콘 안 쓰겠다 선언

            set.setColor(Color.RED);
            set.setCircleColor(Color.RED);

            LineData data = new LineData(set); // 데이터 셋 추가

            chart.setData(data);
            chart.invalidate(); //차트를 무효화했다가 다시 그리기 
        }
    }
    protected ArrayList<Tag> getArrayListFromJSONString(String jsonString) {
        output = new ArrayList();
        try {
            // 처음 double-quote와 마지막 double-quote 제거
            jsonString = jsonString.substring(1,jsonString.length()-1);
            // \\\" 를 \"로 치환
            jsonString = jsonString.replace("\\\"","\"");

            Log.i(TAG, "jsonString="+jsonString);

            JSONObject root = new JSONObject(jsonString);
            JSONArray jsonArray = root.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                Log.i(TAG,"ratio="+jsonObject.getString("deviceId"));

                Tag thing = new Tag(jsonObject.getString("deviceId"),
                                    jsonObject.getString("ratio"),
                                    jsonObject.getString("timestamp"),
                                    jsonObject.getString("time"));
                output.add(thing);
            }

        } catch (JSONException e) {
            //Log.e(TAG, "Exception in processing JSONString.", e);
            e.printStackTrace();
        }
        Log.i(TAG, "output="+output);
        return output;
    }
        //Tag 클래스는 request해서 받아온 값들의 변수 선언을 위해 존재하며, 비율값, 타임스탬프값(문자열), Time값(밀리 세컨드), id(키)를 멤버 변수로 갖는다.
        class Tag {
            String Ratio;
            String Timestamp;
            String id;
            String Time;
            //생성자 정의 및 초기화 부분
            public Tag(String deviceid, String ratio, String timestamp, String realTime) {
                id = deviceid;
                Ratio = ratio;
                Timestamp = timestamp;
                Time = realTime;
            }
            //받아온 값들을 가지고 문자열로 만든다. -> 로그 조회 시에 활용
            public String toString() {
                return String.format("{%s} [%s] ratio : %s", id, Timestamp, Ratio);
            }
        }
}
