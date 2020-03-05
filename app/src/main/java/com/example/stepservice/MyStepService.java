package com.example.stepservice;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class MyStepService extends Service implements SensorEventListener {
    int count = 0;
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;
    private int steps;
    private static final int SHAKE_THRESHOLD = 800;
    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;
    final static String MY_ACTION = "MY_ACTION";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("onCreate","IN");
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerormeterSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startld) {
        super.onStartCommand(intent , flags, startld);
        Log.i("onSTartCommand" , "IN");
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this , accelerormeterSensor , sensorManager.SENSOR_DELAY_GAME);
        return super.onStartCommand(intent,flags,startld);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("onDestroy","IN");
        if (sensorManager !=null)
            sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor , int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            long currentTime=System.currentTimeMillis();
            long gabOfTime=(currentTime-lastTime);
            if(gabOfTime>100){
                lastTime=currentTime;
                x=event.values[0];
                y=event.values[1];
                z=event.values[2];
                speed=Math.abs(x=y+z-lastX-lastY-lastZ)/gabOfTime*10000;
                if (speed>SHAKE_THRESHOLD){
                    //Intent myFilteredResponse=new Intent("com.nslb.twipee.User.MyStepService");
                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);
                    steps = count++;
                    int tvStepDetector = steps/2;
                    intent.putExtra("ServiceData",tvStepDetector);
                    sendBroadcast(intent);
                }
                lastX=event.values[0];
                lastY=event.values[1];
                lastZ=event.values[2];
            }
        }
    }


}
