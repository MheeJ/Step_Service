package com.example.stepservice;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ToggleButton Toggle;
    private Button Go_Beacon_Service;
    private TextView Step_Text;
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 100;
    private int DataFromService;
    MyReceiver myReceiver;
    private int Total_Step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGUI();
    }


    private void initGUI(){
        Step_Text = (TextView)findViewById(R.id.step_text);
        Toggle = (ToggleButton)findViewById(R.id.toggleButton);
        Toggle.setOnClickListener(this);
    }

    //버튼 눌렀을때 서비스 시작
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toggleButton:
                if(Toggle.isChecked()){
                    Start_StepService();
                    setToast("Service 시작");
                }
                else {
                    End_BeaconService();
                    setToast("Service 끝");
                }
        }
    }

    //필요없으면 삭제가능
    //텍스트뷰
    public void setTextView(String text){Step_Text.setText(text);}

    //필요없으면 삭제가능
    //토스트
    public void  setToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    //비콘 서비스 시작 준비
    public void Start_StepService(){
        myReceiver = new MyReceiver();
        Total_Step = 0;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyStepService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        Intent intent = new Intent(getApplication(), MyStepService.class);
        startService(intent);
    }

    //비콘 서비스 종료
    public void End_BeaconService(){
        //확인용임 필요없으면 삭제가능
        setTextView("Hello Step");

        //꼭필요한 부분
        //Beacon_Condition = "TurnOff";
        Intent intent = new Intent(this, MyStepService.class);
        stopService(intent);

        //필요없으면 삭제가능
        setTextView("걸음수 : "+ Total_Step);
    }

    //서비스에서 데이터 가져오는 부분
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            DataFromService = arg1.getIntExtra("ServiceData",0 );

            //필요없으면 삭제가능
            //UI에 표시하는 부분 여기에 입력하면 됨 _ 현재 접속된 비콘 이름 출력
            Total_Step = DataFromService;
            set_UI();
        }
    }


    //필요없으면 삭제가능
    //서비스에서 가져온 데이터 UI에 표시
    public void set_UI(){
        setToast("걸음수 : "+DataFromService);
        setTextView("걸음수 : "+DataFromService);
    }

}
