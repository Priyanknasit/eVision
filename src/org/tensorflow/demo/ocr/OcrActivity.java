package org.tensorflow.demo.ocr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.demo.MainActivity;
import org.tensorflow.demo.R;
import org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OcrActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private TextToSpeech myTTS;
    Button stop,start;
    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID=1001;
    TextToSpeech textToSpeech;
    private View main;
    private ImageView imageView;
    private Camera camera;
    String text;
    int flag=0;

    //to open CAMERA
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode)
//        {
//            case RequestCameraPermissionID:
//            {
//                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
//                {
//                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
//                    {
//                        return;
//                    }
//                    try {
//                        cameraSource.start(cameraView.getHolder());
//                    }
//                    catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr_activity);


        initializeTextToSpeech("welcome, now you are in OCR activity");
        stop=findViewById(R.id.stop);
        start=findViewById(R.id.start);
        cameraView=(SurfaceView)findViewById(R.id.surface_view);
        textView=(TextView)findViewById(R.id.text_view);

        //text to speech
        textToSpeech=new TextToSpeech(OcrActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!= TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        //read button
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraSource.stop();
                flag=1;
                initializeTextToSpeech(text);
            }
        });
        //restart button
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera
                if(flag==0) {
                    cameraSource.stop();
                    flag=1;
                    initializeTextToSpeech(text);

                }else{
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myTTS.shutdown();
                }}
        });
        TextRecognizer textRecognizer= new  TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational())
        {
            Log.v("MainActivity","Detector dependencies are not available yet");

        }
        else
        {
            cameraSource=new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {

                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)

                        {
                            ActivityCompat.requestPermissions(OcrActivity.this,new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                            return;
                        }
                        //open camera
                        cameraSource.start(cameraView.getHolder());

                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();

                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items=detections.getDetectedItems();
                    if(items.size()!=0)
                    {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder=new StringBuilder();
                                for (int i=0;i<items.size();++i)
                                {

                                    TextBlock item=items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                textView.setText(stringBuilder.toString());
                                text=stringBuilder.toString();



                                // textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);

//                                      ArrayList<String>arrayList=new ArrayList<String>();
//                                      arrayList.add(text);
                            }

                        });
                    }
                }
            });
        }

    }

    //headphone button click  will activate the speech recognition
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
            voiceAutomation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //speech recognition without the prompt v1.1
    private void voiceAutomation(){
        initSpeechRecognizer();
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        speechRecognizer.startListening(intent);
    }

    //speech recognition without the prompt v1.1
    private void initSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            speechRecognizer= SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    String command=result.get(0);


                    if(command.contains("open") || command.contains("start"))
                    {
                        //  tvResult.setText("camera");
                        if(command.contains("route navigation") || command.contains("navigation"))
                        {
                            Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
                            startActivity(intent);
                        }
                        else if(command.contains("ocr"))
                        {
                            Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.ocr.OcrActivity.class);
                            startActivity(intent);
                        }
                        else if(command.contains("help"))
                        {
                            Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.Help.presenter.HelpScreenPresenter.class);
                            startActivity(intent);
                        }
                        else if(command.contains("location") )
                        {
                            Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.location.presenter.LocationScreenPresenter.class);
                            startActivity(intent);
                        }
                        else if(command.contains("four") || command.contains("4"))
                        {
                            Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.Activity4.class);
                            startActivity(intent);
                        }
                        else if(command.contains("menu") )
                        {
                            Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            initializeTextToSpeech("Invalid message");
                        }

                    }
                    else if(command.contains("read")){
                        //read button
                        cameraSource.stop();
                        flag=1;
                        initializeTextToSpeech(text);
                    }
                    else if(command.contains("restart")){
                        //restart button
                        //open camera
                        if(flag==0) {
                            cameraSource.stop();
                            flag=1;
                            initializeTextToSpeech(text);

                        }else{
                            try {
                                cameraSource.start(cameraView.getHolder());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            myTTS.shutdown();
                        }
                    }
                    else
                    {
                        initializeTextToSpeech("Invalid message");
                    }

                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    //speech recognition activity v1.0
    private void voiceautomation() {
        Intent voice=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak now...");
        startActivityForResult(voice,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data != null )
        {
            ArrayList<String> arrayList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String command=arrayList.get(0).toString().toLowerCase();
            if(command.contains("open"))
            {
                //  tvResult.setText("camera");
                if(command.contains("one") || command.contains("1"))
                {
                    Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
                    startActivity(intent);
                }
                else if(command.contains("two") || command.contains("2"))
                {
                    Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.ocr.OcrActivity.class);
                    startActivity(intent);
                }
                else if(command.contains("three") || command.contains("3"))
                {
                    Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.Activity3.class);
                    startActivity(intent);
                }
                else if(command.contains("four") || command.contains("4"))
                {
                    Intent intent=new Intent(OcrActivity.this,org.tensorflow.demo.Activity4.class);
                    startActivity(intent);
                }
                else
                {
                    initializeTextToSpeech("Invalid message");
                }

            }
            else if(command.contains("read")){
                //read button
                cameraSource.stop();
                flag=1;
                initializeTextToSpeech(text);
            }
            else if(command.contains("restart")){
                //restart button
                //open camera
                if(flag==0) {
                    cameraSource.stop();
                    flag=1;
                    initializeTextToSpeech(text);

                }else{
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myTTS.shutdown();
                }
            }
            else
            {
                initializeTextToSpeech("Invalid message");
            }
        }
    }
    //text to speech
    private void initializeTextToSpeech() {
        myTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(OcrActivity.this,"No TTS Engine", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.US);
                    speak("welcome to main menu");
                }
            }
        });
    }
    public void initializeTextToSpeech(final String text)
    {
        myTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(OcrActivity.this,"No TTS Engine", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.US);
                    speak(text);
                }
            }
        });
    }


    private void speak(String message){
        if(Build.VERSION.SDK_INT >= 21){
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null,null);

        }
        else {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        myTTS.shutdown();
    }
}

