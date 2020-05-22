package org.tensorflow.demo;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class SpeakActivity {
    private TextToSpeech myTTS;
    //text to speech
//    private void initializeTextToSpeech() {
//        myTTS=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int i) {
//                if(myTTS.getEngines().size()==0){
//                    Toast.makeText(MainActivity.this,"No TTS Engine", Toast.LENGTH_LONG).show();
//                    finish();
//                }else{
//                    myTTS.setLanguage(Locale.US);
//                    speak("welcome to main menu");
//                }
//            }
//        });
//    }
    public void initializeTextToSpeech(final Context context, final String text)
    {
        myTTS=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(context,"No TTS Engine", Toast.LENGTH_LONG).show();

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
}
