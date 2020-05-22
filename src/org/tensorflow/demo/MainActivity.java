package org.tensorflow.demo;
//511999

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.demo.app_logic.AppState;
import org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter;
import org.tensorflow.demo.directions.view.DirectionsScreenListener;
import org.tensorflow.demo.ocr.OcrActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DirectionsScreenListener screenListner;
    private SpeechRecognizer speechRecognizer;
    final int RequestCameraPermissionID=1001;
    final int RequestLocationPermissionID=1002;
    final int RequestInternetPermissionID=1003;
    final int RequestSMSPermissionID=1004;
    final int RequestContactsPermissionID=1005;
    final int RequestMicroPhonePermissionID=1006;
   Button btn1,btn2,btn3,btn4,btn5,btn6;
   TextView tvResult;
   public final AppState appState = AppState.getInstance();
   private TextToSpeech myTTS;
//    private SpeechRecognizer mySpeechRecognizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn1= (Button) findViewById(R.id.btn1);
        btn2= (Button) findViewById(R.id.btn2);
        btn3= (Button) findViewById(R.id.btn3);
        btn4= (Button) findViewById(R.id.btn4);
        btn5=(Button) findViewById(R.id.btn5);
        btn6=(Button) findViewById(R.id.btn6);
        checkPermissions();

        //tvResult= (TextView) findViewById(R.id.tvResult);

//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //the first recognition method
////                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
////                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
////                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,Locale.getDefault());
////                mySpeechRecognizer.startListening(intent);
//
////                voiceautomation();//speech recognition
////                tvResult.setText("hey");
//                Intent intent=new Intent(MainActivity.this,com.example.bh1.Activity1.class);
//                startActivity(intent);
//            }
//        });
        initializeTextToSpeech();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DirectionsScreenPresenter.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.ocr.OcrActivity.class);
                startActivity(intent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.location.presenter.LocationScreenPresenter.class);
                startActivity(intent);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.DetectorActivity.class);
                startActivity(intent);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.Help.presenter.HelpScreenPresenter.class);
                startActivity(intent);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.Activity4.class);
                startActivity(intent);
            }
        });


//        initializeSpeechRecognizer();


    }

    private void checkPermissions() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
            return;
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},RequestLocationPermissionID);
            return;
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET},RequestInternetPermissionID);
            return;
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},RequestSMSPermissionID);
            return;
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},RequestContactsPermissionID);
            return;
        }
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)

        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},RequestMicroPhonePermissionID);
            return;
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

                        if(command.contains("route navigation") || command.contains("navigation") || command.contains("navigate"))
                        {
                            if(command.contains("to"))
                            {
                                String destination=command.substring(command.indexOf("to") + 3 , command.length());
                                Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
                                intent.putExtra("dest",destination);

                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
                                startActivity(intent);
                            }

                        }
                        else if(command.contains("ocr") )
                        {
                            Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.ocr.OcrActivity.class);
                            startActivity(intent);
                        }
                        else if(command.contains("help"))
                        {
                            Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.Help.presenter.HelpScreenPresenter.class);
                            startActivity(intent);
                        }
                        else if(command.contains("four") || command.contains("4"))
                        {
                            Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.Activity4.class);
                            startActivity(intent);
                        }
                        else if(command.contains("location") )
                        {
                            Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.location.presenter.LocationScreenPresenter.class);
                            startActivity(intent);
                        }
                        else if(command.contains("menu") )
                        {
                            Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            initializeTextToSpeech("Invalid message");
                        }

                    }
                    else if(command.contains("take me to")){
                        String destination=command.substring(command.indexOf("to") + 3 , command.length());
                        Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
                        intent.putExtra("dest",destination);
//                        DirectionsScreenPresenter dsp=new DirectionsScreenPresenter();
//                        dsp.findPath(destination);
                        startActivity(intent);

                    }
                    else if(command.contains("read") && command.contains("front")){
                        Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.ocr.OcrActivity.class);
                        intent.putExtra("delay","set");
                        startActivity(intent);
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


    //speech recognition activity with prompt v1.0
    private void voiceautomation() {
        Intent voice=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak now...");
        startActivityForResult(voice,1);
    }
    //speech recognition activity with prompt v1.0
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
                    Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
                    startActivity(intent);
                }
                else if(command.contains("two") || command.contains("2"))
                {
                    Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.ocr.OcrActivity.class);
                    startActivity(intent);
                }
                else if(command.contains("three") || command.contains("3"))
                {
                    Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.Activity3.class);
                    startActivity(intent);
                }
                else if(command.contains("four") || command.contains("4"))
                {
                    Intent intent=new Intent(MainActivity.this,org.tensorflow.demo.Activity4.class);
                    startActivity(intent);
                }
                else
                {
                    initializeTextToSpeech("Invalid message");
                }

            }
            else
            {
                initializeTextToSpeech("Invalid message");
            }
        }
    }
// The first method we used for speech recognition
//    private void initializeSpeechRecognizer() {
//        if(mySpeechRecognizer.isRecognitionAvailable(this)){
//            mySpeechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
//            mySpeechRecognizer.setRecognitionListener(new RecognitionListener() {
//                @Override
//                public void onReadyForSpeech(Bundle bundle) {
//
//                }
//
//
//                @Override
//                public void onBeginningOfSpeech() {
//
//                }
//
//                @Override
//                public void onRmsChanged(float v) {
//
//                }
//
//                @Override
//                public void onBufferReceived(byte[] bytes) {
//
//                }
//
//                @Override
//                public void onEndOfSpeech() {
//
//                }
//
//                @Override
//                public void onError(int i) {
//
//                }
//
//                @Override
//                public void onResults(Bundle bundle) {
//                    List<String> results=bundle.getStringArrayList(
//                            SpeechRecognizer.RESULTS_RECOGNITION
//                    );
//                    processResult(results.get(0));
//                }
//
//                @Override
//                public void onPartialResults(Bundle bundle) {
//
//                }
//
//                @Override
//                public void onEvent(int i, Bundle bundle) {
//
//                }
//            });
//        }
//    }
//
//    private void processResult(String command) {
//        command=command.toLowerCase();
//
//        if(command.contains("what")){
//            if(command.contains("your name")){
//                speak("My name is priyank Rikin.");
//            }
//
//            if(command.contains("time")){
//                speak("Time is on your phone");
//            }
//        }else if(command.contains("open")){
//            if(command.contains("browser")){
//                speak("opening sunder browser");
//            }
//
//        }
//    }
//
    //text to speech
    private void initializeTextToSpeech() {
        myTTS=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(MainActivity.this,"No TTS Engine", Toast.LENGTH_LONG).show();
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
        myTTS=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(MainActivity.this,"No TTS Engine", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    myTTS.setLanguage(Locale.US);
                    speak(text);
                }
            }
        });
    }
    private void speak(String message) {
        if(Build.VERSION.SDK_INT >=21){
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null,null);
        }else{
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    public void speakForMe(String message)
    {
        initializeTextToSpeech(message);
    }
    protected void onPause(){
        super.onPause();
        //myTTS.shutdown();
    }



}

