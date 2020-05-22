package org.tensorflow.demo.Help.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import org.tensorflow.demo.Help.presenter.HelpScreenListener;
import org.tensorflow.demo.Help.presenter.HelpScreenPresenter;
import org.tensorflow.demo.MainActivity;
import org.tensorflow.demo.R;
import org.tensorflow.demo.SpeakActivity;

import java.util.List;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public class HelpScreenViewImpl implements HelpScreenView {

    private View rootView;
    private Context context;
    private HelpScreenListener locationScreenListener;
    private SpeechRecognizer speechRecognizer;
    //android views
    private Button speechBtn;
    private Button addContactBtn;
    private Button helpBtn;
    SpeakActivity sa=new SpeakActivity();
    //private TextView locationTV;


    public HelpScreenViewImpl(Context context, ViewGroup container) {
        this.context = context;

        rootView = LayoutInflater.from(context).inflate(R.layout.activity_help, container);

        initialize();
    }

    private void initialize() {
        speechBtn = (Button) rootView.findViewById(R.id.speechBtn);
        addContactBtn = (Button) rootView.findViewById(R.id.addContactBtn);
        helpBtn = (Button) rootView.findViewById(R.id.helpBtn);


       // locationTV = (TextView) rootView.findViewById(R.id.locationTV);

//        locationTV.setText("No location found!");

        //LayoutParams bottomLeftButtonLP = AppState.getInstance().getBottomLeftButtonLP();
        //LayoutParams speechButtonLayoutParams = AppState.getInstance().getSpeechButtonLayoutParams();

        //addContactBtn.setLayoutParams(bottomLeftButtonLP);
        //speechBtn.setLayoutParams(speechButtonLayoutParams);

        speechBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                locationScreenListener.startRecording();
            }
        });

        addContactBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                locationScreenListener.addContact();
            }
        });

        helpBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                locationScreenListener.sendMessages();
            }
        });
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
        if(SpeechRecognizer.isRecognitionAvailable(context)){
            speechRecognizer= SpeechRecognizer.createSpeechRecognizer(context);
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

                        if(command.contains("route navigation")||command.contains("navigation"))
                        {
                            Intent intent=new Intent(context,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
                            locationScreenListener.startActivity(intent);
                        }
                        else if(command.contains("ocr"))
                        {
                            Intent intent=new Intent(context,org.tensorflow.demo.ocr.OcrActivity.class);
                            locationScreenListener.startActivity(intent);
                        }
                        else if(command.contains("help"))
                        {
                            Intent intent=new Intent(context,org.tensorflow.demo.Help.presenter.HelpScreenPresenter.class);
                            locationScreenListener.startActivity(intent);
                        }
                        else if(command.contains("four") || command.contains("4"))
                        {
                            Intent intent=new Intent(context,org.tensorflow.demo.Activity4.class);
                            locationScreenListener.startActivity(intent);
                        }
                        else if(command.contains("location"))
                        {
                            Intent intent=new Intent(context, org.tensorflow.demo.location.presenter.LocationScreenPresenter.class);
                            locationScreenListener.startActivity(intent);
                        }
                        else if(command.contains("menu") )
                        {
                            Intent intent=new Intent(context, MainActivity.class);
                            locationScreenListener.startActivity(intent);
                        }
                        else
                        {
                            sa.initializeTextToSpeech(context,"Invalid message");
                        }

                    }
                    else if(command.contains("send message")){
                        sendMsgActivity();
                        //Log.d("111","222");
                    }
                    else
                    {
                        sa.initializeTextToSpeech(context,"Invalid message");
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
    @Override
    public void sendMsgActivity(){
        locationScreenListener.sendMessages();
    }
    @Override
    public View getAndroidLayoutView() {
        return rootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public void onSaveViewState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {

    }

    @Override
    public void displayUserCurrentLocation(String userCurrentAddressText) {
        if (userCurrentAddressText == null) {
            return;
        }

        String[] addressInfo = userCurrentAddressText.split(",");
       // locationTV.setText("");

        int length = addressInfo.length;
        for (int i = 0; i < length; i++) {

            if (addressInfo[i].charAt(0) == ' ') { // remove leading whitespace
                addressInfo[i] = addressInfo[i].replaceFirst(" ", "");
            }
           // locationTV.append(addressInfo[i]);
           // locationTV.append(",\n");
        }

    }


    @Override
    public void setListener(HelpScreenListener listener) {
        this.locationScreenListener = listener;
    }
}
