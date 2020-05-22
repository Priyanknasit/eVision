package org.tensorflow.demo.directions.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.demo.R;
import org.tensorflow.demo.app_logic.AppState;
import org.tensorflow.demo.app_logic.Constants;

import java.util.Locale;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public class DirectionsScreenViewImpl extends AppCompatActivity implements DirectionsScreenView {

  private Context context;
  private DirectionsScreenListener screenListener;
  private TextToSpeech myTTS;
  private Bundle viewState;
  //public String dest="anand";
  private View rootView; //layout
  private Button speechInputButton;
  private Button findPathButton;
  private Button location;
  private AutoCompleteTextView destActv;
  private TextView distanceTV;
  private TextView durationTV;
  public String myvar="rikin";
  public DirectionsScreenViewImpl(){}
  public DirectionsScreenViewImpl(Context context, ViewGroup container) {
    this.context = context;
    rootView = LayoutInflater.from(context).inflate(R.layout.directions_screen_layout, container);
    viewState = new Bundle();
//    if(dest != null){
//     setDestination(dest);
//    }
    initialize();
    //setButtonsSize();
    addListenersToButtons();
  }
  public DirectionsScreenViewImpl(Context context, ViewGroup container, String dest) {
    this.context = context;
    rootView = LayoutInflater.from(context).inflate(R.layout.directions_screen_layout, container);
    viewState = new Bundle();

    initialize();
    //setButtonsSize();
    addListenersToButtons();
    if(dest != null){

      this.setDestination(dest);
      //findPathev();
    }
  }

  private void initialize() {
    speechInputButton = (Button) rootView.findViewById(R.id.speechButton_directions);
    findPathButton = (Button) rootView.findViewById(R.id.findPathB);
    location =(Button) rootView.findViewById(R.id.location);
    destActv = (AutoCompleteTextView) rootView.findViewById(R.id.destACTV);
    distanceTV = (TextView) rootView.findViewById(R.id.distanceTV);
    durationTV = (TextView) rootView.findViewById(R.id.durationTV);

    String[] testLocations = context.getResources().getStringArray(R.array.test_locations);
    ArrayAdapter<String> adapter =
        new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, testLocations);
    destActv.setAdapter(adapter);

    destActv.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        screenListener.hideSoftKeyboard();

      }

    });
  }

public void findPathev(){
  if (isDesinationSet()) {
    String destination = destActv.getText().toString();
    screenListener.findPath(destination);
  }
}
public String getDestActv()
{
//  return destActv.getText().toString();
  return myvar;
}
  //headphone button click is not working in this class so i implemented in DirectionScreenPresenter class
  //headphone button click  will activate the speech recognition
//  @Override
//  public boolean onKeyDown(int keyCode, KeyEvent event) {
//    if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
//      voiceautomation();
//      return true;
//    }
//    return super.onKeyDown(keyCode, event);
//  }
//
//  //speech recognition activity
//  private void voiceautomation() {
//    Intent voice=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//    voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//    voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//    voice.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak now...");
//    startActivityForResult(voice,1);
//  }
//
//  protected void onActivityResult(int requestCode, int resultCode, Intent data){
//    super.onActivityResult(requestCode, resultCode, data);
//    if(requestCode==1 && resultCode==RESULT_OK && data != null )
//    {
//      ArrayList<String> arrayList=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//      String command=arrayList.get(0).toString().toLowerCase();
//      if(command.contains("open")|| command.contains("start"))
//      {
//        //  tvResult.setText("camera");
//        if(command.contains("route navigation") )
//        {
//          Intent intent=new Intent(DirectionsScreenViewImpl.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
//          startActivity(intent);
//        }
//        else if(command.contains("ocr") )
//        {
//          Intent intent=new Intent(DirectionsScreenViewImpl.this,org.tensorflow.demo.ocr.OcrActivity.class);
//          startActivity(intent);
//        }
//        else if(command.contains("three") || command.contains("3"))
//        {
//          Intent intent=new Intent(DirectionsScreenViewImpl.this,org.tensorflow.demo.Activity3.class);
//          startActivity(intent);
//        }
//        else if(command.contains("four") || command.contains("4"))
//        {
//          Intent intent=new Intent(DirectionsScreenViewImpl.this,org.tensorflow.demo.Activity4.class);
//          startActivity(intent);
//        }
//        else if(command.contains("location") )
//        {
//          Intent intent=new Intent(DirectionsScreenViewImpl.this,org.tensorflow.demo.location.presenter.LocationScreenPresenter.class);
//          startActivity(intent);
//        }
//        else if(command.contains("menu") )
//        {
//          Intent intent=new Intent(DirectionsScreenViewImpl.this,org.tensorflow.demo.MainActivity.class);
//          startActivity(intent);
//        }
//        else
//        {
//          initializeTextToSpeech("Invalid message");
//        }
//
//      }
//      else if(command.contains("find")||command.contains("path")){
//        // initializeTextToSpeech("find");
//        // findPath();
////        DirectionsScreenViewImpl d=new DirectionsScreenViewImpl(DirectionsScreenPresenter.this,null);
//////            d.findpathevent();
////        DirectionsScreenPresenter dsp=new DirectionsScreenPresenter();
////        dsp.findPath(d.getDestActv());
//          screenListener.findPath(getDestActv());
//      }
//      else if(command.contains("set")||command.contains("destination")){
//        //initializeTextToSpeech("set");
//        if(command.contains("to")){
//          String destination=command.substring(command.indexOf("to") + 3 , command.length());
//          Intent intent=new Intent(DirectionsScreenViewImpl.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
//          intent.putExtra("dest",destination);
//          startActivity(intent);}
//        else
//        {
//          String destination=command.substring(command.indexOf("destination") + 12 , command.length());
//          Intent intent=new Intent(DirectionsScreenViewImpl.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
//          intent.putExtra("dest",destination);
//          startActivity(intent);}
//
//      }
//      else
//      {
//        initializeTextToSpeech("Invalid message");
//      }
//    }
//  }
  //text to speech
  private void initializeTextToSpeech() {
    myTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int i) {
        if(myTTS.getEngines().size()==0){
          Toast.makeText(context,"No TTS Engine", Toast.LENGTH_LONG).show();
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
          Toast.makeText(context,"No TTS Engine", Toast.LENGTH_LONG).show();
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


  private void setButtonsSize() {
    LayoutParams speechLp = AppState.getInstance().getSpeechButtonLayoutParams();
    LayoutParams bottomLeftButtonLP = AppState.getInstance().getBottomLeftButtonLP();

    speechInputButton.setLayoutParams(speechLp);
    findPathButton.setLayoutParams(bottomLeftButtonLP);
  }

  private void addListenersToButtons() {
    findPathButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (isDesinationSet()) {
          String destination = destActv.getText().toString();
          Log.d("distance in km","11111111111111111111111111111111111");
          screenListener.findPath(destination);


        }
      }
    });



    speechInputButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        screenListener.startRecording();
      }
    });

    location.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, org.tensorflow.demo.location.presenter.LocationScreenPresenter.class);
        screenListener.startActivity(intent);
      }
    });

  }
  @Override
  public void suggesion()
  {
    String destint=distanceTV.getText().toString().substring(0,distanceTV.getText().toString().indexOf("km"));
    int distint= Integer.parseInt(destint);
    if(distint>=5 && distint<=30)
    {
      initializeTextToSpeech("I suggest you to take an autoriksha");
    }
    else if(distint>=31)
    {
      initializeTextToSpeech("I suggest you to take a bus");
    }
  }
  @Override
  public View getAndroidLayoutView() {
    return rootView;
  }

  @Override
  public Bundle getViewState() {
    return viewState;
  }

  @Override
  public void onSaveViewState(Bundle outState) {
    String destinationText = destActv.getText().toString();

    if (!TextUtils.isEmpty(destinationText)) {
      outState.putString(Constants.DESTINATION_TEXT, destinationText);
    }

    String distanceText = distanceTV.getText().toString();
    if (!TextUtils.isEmpty(distanceText)) {
      outState.putString(Constants.DISTANCE_KM, distanceText);
      String duration = durationTV.getText().toString();
      outState.putString(Constants.DURATION_TIME, duration);
    }

  }

  @Override
  public void onRestoreInstanceState(Bundle inState) {
    String destination = inState.getString(Constants.DESTINATION_TEXT);
    destActv.setText(destination);

    String distance = inState.getString(Constants.DISTANCE_KM);
    distanceTV.setText(distance);

    String duration = inState.getString(Constants.DURATION_TIME);
    durationTV.setText(duration);
  }

  private boolean isDesinationSet() {
    boolean valid = true;

    String destination = destActv.getText().toString();

    if (TextUtils.isEmpty(destination)) {
      destActv.setError("Required!");
      valid = false;
    }

    return valid;
  }
  @Override
  public void findpathEvent()
  {
    if (isDesinationSet()) {
      String destination = destActv.getText().toString();
      screenListener.findPath(destination);
    }
   Log.d("rikin check kar",distanceTV.getText().toString());
  }
  @Override
  public void setScreenListener(DirectionsScreenListener listener) {
    screenListener = listener;
  }

  @Override
  //this method will set the text decoded from speech input
  //jo..destination varu boltutu e DirectionScreenPresenter ma 575 mi line par 6 tyathi change karelu
  public void setDestination(String destination) {
    //talk button input comes here
    if(destination.contains("open"))
    {
     // initializeTextToSpeech("going to open");

      Intent intent=new Intent(context,org.tensorflow.demo.ocr.OcrActivity.class);
      screenListener.startActivity(intent);
    }
    else
    {
    destActv.setText(destination);}
  }

  @Override
  public void setDistance(String distance) {


    distanceTV.setText(distance);
    Log.d("in DirViewImpl","2");


  }

  @Override
  public void setDuration(String duration) {
    durationTV.setText(duration);
  }



}
