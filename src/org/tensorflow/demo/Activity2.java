package org.tensorflow.demo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import java.util.Locale;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
    }

    //headphone button click  will activate the speech recognition
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
            //voiceautomation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //speech recognition activity
    private void voiceautomation() {
        Intent voice=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        voice.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak now...");
        startActivityForResult(voice,1);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1 && resultCode==RESULT_OK && data != null )
//        {
//            ArrayList<String> arrayList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            String command=arrayList.get(0).toString().toLowerCase();
//            if(command.contains("open"))
//            {
//                //  tvResult.setText("camera");
//                if(command.contains("one") || command.contains("1"))
//                {
//                    Intent intent=new Intent(Activity2.this,com.example.bh1.Activity1.class);
//                    startActivity(intent);
//                }
//                if(command.contains("two") || command.contains("2"))
//                {
//                    Intent intent=new Intent(Activity2.this,com.example.bh1.Activity2.class);
//                    startActivity(intent);
//                }
//                if(command.contains("three") || command.contains("3"))
//                {
//                    Intent intent=new Intent(Activity2.this,com.example.bh1.Activity3.class);
//                    startActivity(intent);
//                }
//                if(command.contains("four") || command.contains("4"))
//                {
//                    Intent intent=new Intent(Activity2.this,com.example.bh1.Activity4.class);
//                    startActivity(intent);
//                }
//                if(command.contains("menu"))
//                {
//                    Intent intent=new Intent(Activity2.this,com.example.bh1.MainActivity.class);
//                    startActivity(intent);
//                }
//            }
//        }
//    }
}
