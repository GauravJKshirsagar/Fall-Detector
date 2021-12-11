package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;



public class MainActivity extends AppCompatActivity {

    Interpreter interpreter ;

    Float accxmin = -1.9776106f;
    Float accymin = 8.240824f;
    Float acczmin = 4.4436383f;
    Float gyroxmin = 0.07f;
    Float gyroymin = -0.08f;
    Float gyrozmin = -0.03f;

    Float accxmax = -1.9776106f;
    Float accymax = 8.240824f;
    Float acczmax = 4.4436383f;
    Float gyroxmax = 0.07f;
    Float gyroymax = -0.08f;
    Float gyrozmax = -0.03f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         String[] acc = new String[3];
         String[] gyro = new String[3];
        String[] magneto = new String[3];





        DatabaseReference mDatabase;
// ...
        mDatabase = FirebaseDatabase.getInstance("https://helloworld-84573-default-rtdb.firebaseio.com/").getReference();




        SensorManager sensorManager;
        Sensor gyroscopeSensor,accelerometerSensor;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        // Create a listener for gyroscope Sensor
        SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here

                TextView tv1 = (TextView)findViewById(R.id.txtview_gyrox);
                tv1.setText(Float.toString(sensorEvent.values[0]));
                TextView tv2 = (TextView)findViewById(R.id.txtview_gyroy);
                tv2.setText(Float.toString(sensorEvent.values[1]));
                TextView tv3 = (TextView)findViewById(R.id.txtview_gyroz);
                tv3.setText(Float.toString(sensorEvent.values[2]));


                float gyroxtemp = sensorEvent.values[0];
                float gyroytemp = sensorEvent.values[1];
                float gyroztemp = sensorEvent.values[2];

                if(gyroxtemp<gyroxmin)
                {
                    gyroxmin = gyroxtemp;
                }
                if(gyroytemp<gyroymin)
                {
                    gyroymin = gyroxtemp;
                }
                if(gyroztemp<gyrozmin)
                {
                    gyrozmin = gyroxtemp;
                }
                /////
                if(gyroxtemp>gyroxmax)
                {
                    gyroxmax = gyroxtemp;
                }
                if(gyroytemp>gyroymin)
                {
                    gyroymax = gyroxtemp;
                }
                if(gyroztemp>gyrozmax)
                {
                    gyrozmax = gyroxtemp;
                }



                gyro[0] = Float.toString(sensorEvent.values[0]);
                gyro[1] = Float.toString(sensorEvent.values[1]);
                gyro[2] = Float.toString(sensorEvent.values[2]);

                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();

                Map map = new HashMap();
                String gz = gyro[2];

                Log.d("Gyrometer Y", gz);

                map.put("gyrox", gyro[0]);
                map.put("gyroy", gyro[1]);
                map.put("gyroz", gyro[2]);

                //DatabaseReference newUserRef = mDatabase.child(ts).push();
                //newUserRef.setValue(map);

                }



            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

// Register the listener
//        sensorManager.registerListener(gyroscopeSensorListener,
//                gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Specify the layout you are using.
        setContentView(R.layout.activity_main);

// Load and use views afterwards


        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Create a listener for accelerometer Sensor
        SensorEventListener accelerometerSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here

                TextView tv1 = (TextView)findViewById(R.id.txtview_accx);
                tv1.setText(Float.toString(sensorEvent.values[0]));
                TextView tv2 = (TextView)findViewById(R.id.txtview_accy);
                tv2.setText(Float.toString(sensorEvent.values[1]));
                TextView tv3 = (TextView)findViewById(R.id.txtview_accz);
                tv3.setText(Float.toString(sensorEvent.values[2]));

                float accxtemp = sensorEvent.values[0];
                float accytemp = sensorEvent.values[1];
                float accztemp = sensorEvent.values[2];

                if(accxtemp<accxmin)
                {
                    accxmin = accxtemp;
                }
                if(accytemp<accymin)
                {
                    accymin = accxtemp;
                }
                if(accztemp<acczmin)
                {
                    acczmin = accxtemp;
                }
                /////
                if(accxtemp>accxmax)
                {
                    accxmax = accxtemp;
                }
                if(accytemp>accymin)
                {
                    accymax = accxtemp;
                }
                if(accztemp>acczmax)
                {
                    acczmax = accxtemp;
                }

                acc[0] = Float.toString(sensorEvent.values[0]);
                acc[1] = Float.toString(sensorEvent.values[1]);
                acc[2] = Float.toString(sensorEvent.values[2]);

                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();

                Map map = new HashMap();



                map.put("accx", acc[0]);
                map.put("accy", acc[1]);
                map.put("accz", acc[2]);

                //DatabaseReference newUserRef = mDatabase.child(ts).push();
                //newUserRef.setValue(map);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };


        // Register the listener
//        sensorManager.registerListener(accelerometerSensorListener,
//                accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        // Specify the layout you are using.
        setContentView(R.layout.activity_main);


        Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Create a listener for accelerometer Sensor
        SensorEventListener magnetometerSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // More code goes here

                TextView tv1 = (TextView)findViewById(R.id.txtview_magnetox);
                tv1.setText(Float.toString(sensorEvent.values[0]));
                TextView tv2 = (TextView)findViewById(R.id.txtview_magnetoy);
                tv2.setText(Float.toString(sensorEvent.values[1]));
                TextView tv3 = (TextView)findViewById(R.id.txtview_magnetoz);
                tv3.setText(Float.toString(sensorEvent.values[2]));







                magneto[0] = Float.toString(sensorEvent.values[0]);
                magneto[1] = Float.toString(sensorEvent.values[1]);
                magneto[2] = Float.toString(sensorEvent.values[2]);

                Long tsLong = System.currentTimeMillis() / 1000;
                String ts = tsLong.toString();

                Map map = new HashMap();



                map.put("magnetox", magneto[0]);
                map.put("magnetoy", magneto[1]);
                map.put("magnetoz", magneto[2]);

                //DatabaseReference newUserRef = mDatabase.child(ts).push();
                //newUserRef.setValue(map);



            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }


        };


        // Register the listener
//        sensorManager.registerListener(magnetometerSensorListener,
//                magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Specify the layout you are using.
        setContentView(R.layout.activity_main);


        Button start = (Button)findViewById(R.id.startbtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.registerListener(accelerometerSensorListener,accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(gyroscopeSensorListener,gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(magnetometerSensorListener,magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });


        Button end = (Button)findViewById(R.id.endbtn);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(accelerometerSensorListener);
                sensorManager.unregisterListener(gyroscopeSensorListener);
                sensorManager.unregisterListener(magnetometerSensorListener);
            }
        });






        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                        .requireWifi()
                        .build();
                FirebaseModelDownloader.getInstance()
                        .getModel("Fall-Net", DownloadType.LOCAL_MODEL, conditions)
                        .addOnSuccessListener(new OnSuccessListener<CustomModel>() {
                            @Override
                            public void onSuccess(CustomModel model) {
                                // Download complete. Depending on your app, you could enable
                                // the ML feature, or switch from the local model to the remote
                                // model, etc.
                                File modelFile = model.getFile();
                                if (modelFile != null) {
                                    interpreter = new Interpreter(modelFile);
                                    ByteBuffer input = ByteBuffer.allocateDirect(12*4).order(ByteOrder.nativeOrder());
                                    input.putFloat(accxmin);
                                    input.putFloat(accymin);
                                    input.putFloat(acczmin);
                                    input.putFloat(accxmax);
                                    input.putFloat(accymax);
                                    input.putFloat(acczmax);

                                    input.putFloat(gyroxmin);
                                    input.putFloat(gyroymin);
                                    input.putFloat(gyrozmin);
                                    input.putFloat(gyroxmax);
                                    input.putFloat(gyroymax);
                                    input.putFloat(gyrozmax);




                                    int bufferSize = 1 * java.lang.Float.SIZE / java.lang.Byte.SIZE;
                                    ByteBuffer output = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
                                    interpreter.run(input,output);

                                    output.rewind();
                                    FloatBuffer opbuf = output.asFloatBuffer();
                                    Float op = opbuf.get(0);
                                    TextView tvres = (TextView)findViewById(R.id.txtview_result);
                                    tvres.setTextColor(Color.GREEN);
                                    String Finalresult = "NO FALL \n";
                                    if(op==1.0)
                                    {
                                        Finalresult = "FALL DECTECTED with probability\n" + Float.toString(op);
                                        tvres.setTextColor(Color.RED);
                                    }

                                    tvres.setText(Finalresult);

                                    Long tsLong = System.currentTimeMillis() / 1000;


                                    accxmin = -1.9776106f;
                                    accymin = 8.240824f;
                                    acczmin = 4.4436383f;
                                    gyroxmin = 0.07f;
                                    gyroymin = -0.08f;
                                    gyrozmin = -0.03f;

                                    accxmax = -1.9776106f;
                                    accymax = 8.240824f;
                                    acczmax = 4.4436383f;
                                    gyroxmax = 0.07f;
                                    gyroymax = -0.08f;
                                    gyrozmax = -0.03f;




//                                    String ts = tsLong.toString();
//                                    tvres.setText(ts);
//                            output.rewind();
//                            FloatBuffer probabilities = output.asFloatBuffer();
//
//
//                                for (int i = 0; i < probabilities.capacity(); i++) {
//
//                                    float probability = probabilities.get(0);
//                                    TextView tvres = (TextView)findViewById(R.id.txtview_result);
//                                    tvres.setText(Float.toString(probability));
//                                }



                                    //TextView tvres = (TextView)findViewById(R.id.txtview_result);
                                    //tvres.setText(Float.toString(op));





                                }
                            }
                        });

            }
        }, 0, 5000);//put here time 1000 milliseconds=1 second

//
//        DatabaseReference mDatabase;
//// ...
//        mDatabase = FirebaseDatabase.getInstance("https://helloworld-84573-default-rtdb.firebaseio.com/").getReference();
//

//        while(true) {
//
//            Long tsLong = System.currentTimeMillis() / 1000;
//            String ts = tsLong.toString();
//
//            Map map = new HashMap();
//            String gz = gyro[2];
//
//            Log.d("Gyrometer Y", gz);
//
//            map.put("gyro_z", gz);
//            map.put("accelero_z", "Rahul");
//
//            mDatabase.child(ts).setValue(map);
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }




    }





}