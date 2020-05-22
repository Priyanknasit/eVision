package org.tensorflow.demo.Help.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import org.tensorflow.demo.Help.model.Contact;
import org.tensorflow.demo.Help.model.HelpModelManager;
import org.tensorflow.demo.Help.view.HelpScreenView;
import org.tensorflow.demo.MainActivity;
import org.tensorflow.demo.app_logic.AppFeaturesEnum;
import org.tensorflow.demo.app_logic.Constants;
import org.tensorflow.demo.util.ModelsFactory;
import org.tensorflow.demo.util.ViewsFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Marius Olariu <mariuslucian.olariu@gmail.com>
 */
public class HelpScreenPresenter extends HelpScreenListener {

    private HelpScreenView rootView;
    private HelpModelManager locationModelManager;
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech myTTS;
    //logic
    private static final String LOCATION_SCREEN_TAG = "LocationScreen";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PICK_CONTACT_REQUEST = 90;
    private static final int PERMISSIONS_REQUEST_CODE = 91;
    private Location currentLocation;
    private boolean isThisActivityInForeground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeTextToSpeech("welcome, now you are in Help activity");

        //null is passed beacause the layout is the parent of all views, i.e. container=none
        rootView = (HelpScreenView) ViewsFactory.createView(this, AppFeaturesEnum.HELP);
        rootView.setListener(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(rootView.getAndroidLayoutView());
        locationModelManager = (HelpModelManager) ModelsFactory.createModel(this, AppFeaturesEnum.HELP);
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        askForNeededPermissions();
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

                        if(command.contains("route navigation")||command.contains("navigation"))
                        {
                            Intent intent=new Intent(HelpScreenPresenter.this,org.tensorflow.demo.directions.presenter.DirectionsScreenPresenter.class);
                            startActivity(intent);
                        }
                        else if(command.contains("ocr"))
                        {
                            Intent intent=new Intent(HelpScreenPresenter.this,org.tensorflow.demo.ocr.OcrActivity.class);
                            startActivity(intent);
                        }
                        else if(command.contains("help"))
                        {
                            Intent intent=new Intent(HelpScreenPresenter.this,org.tensorflow.demo.Help.presenter.HelpScreenPresenter.class);
                            startActivity(intent);
                        }
                        else if(command.contains("four") || command.contains("4"))
                        {
                            Intent intent=new Intent(HelpScreenPresenter.this,org.tensorflow.demo.Activity4.class);
                            startActivity(intent);
                        }
                        else if(command.contains("location"))
                        {
                            Intent intent=new Intent(HelpScreenPresenter.this, org.tensorflow.demo.location.presenter.LocationScreenPresenter.class);
                            startActivity(intent);
                        }
                        else if(command.contains("menu") )
                        {
                            Intent intent=new Intent(HelpScreenPresenter.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            initializeTextToSpeech("Invalid message");
                        }

                    }
                    else if(command.contains("send message")){
                        rootView.sendMsgActivity();
                        //Log.d("111","222");
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
    //text to speech
    private void initializeTextToSpeech() {
        myTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(HelpScreenPresenter.this,"No TTS Engine", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(HelpScreenPresenter.this                                                                                                                                                                                                ,"No TTS Engine", Toast.LENGTH_LONG).show();
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

    private void askForNeededPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isThisActivityInForeground = true;
        detectUserCurrentAddress();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isThisActivityInForeground = false;
    }


    @Override
    public void execute(String detectedText) {
        if (detectedText.toUpperCase().equals("SEND")) {
            textToSpeech.speak("Sending messages to friends ", TextToSpeech.QUEUE_ADD, null);
            locationModelManager.getContacts();
            sendMessages();
        }else{
            textToSpeech.speak("Cannot process command: " + detectedText, TextToSpeech.QUEUE_ADD, null);
        }
    }

    @Override
    public void sendMessages() {
        StringBuilder messageBody = new StringBuilder("Hello,\n");
        messageBody.append("I'm here: ");
        messageBody.append("(").append(currentLocation.getLatitude()).append(",")
                .append(currentLocation.getLongitude()).append(").\n");
        messageBody.append(" Can you come and pick me up please?");
        List<Contact> contacts = locationModelManager.getContacts();

        if (contacts.size() == 0) {
            textToSpeech.speak("Add friends contact info first!", TextToSpeech.QUEUE_ADD, null);
        } else {
            Log.i(LOCATION_SCREEN_TAG, "Sending messages to following contacts: ");
        }

        String message = messageBody.toString();
        for (Contact c : contacts) {
            Log.i(LOCATION_SCREEN_TAG, c.toString());
            sendMessage(c.phoneNumber, message);
        }

    }


    private void detectUserCurrentAddress() {
        try {
            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        currentLocation = task.getResult();

                        Address currentAddress = getAdressFromLocation(currentLocation,
                                HelpScreenPresenter.this);

                        String prettyPrintedAddress = prettyPrintAddress(currentAddress);

                        if ((!textToSpeech.isSpeaking()) && (isThisActivityInForeground)) {
                            //textToSpeech.speak(prettyPrintedAddress, TextToSpeech.QUEUE_ADD, null);
                        }

                        Log.i(LOCATION_SCREEN_TAG, "Pretty: " + prettyPrintedAddress);

                        rootView.displayUserCurrentLocation(prettyPrintedAddress);

                    } else {
                        Log.e(LOCATION_SCREEN_TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e(LOCATION_SCREEN_TAG, e.getMessage());
        }
    }

    /**
     * Returns a string where each element from address is separted by comma
     */
    public static String prettyPrintAddress(Address currentAddress) {
        StringBuilder buffer = new StringBuilder();

        int lastAddressLineIndex = currentAddress.getMaxAddressLineIndex();
        for (int i = 0; i <= lastAddressLineIndex; i++) {
            buffer.append(currentAddress.getAddressLine(i)).append(",");
        }

        return buffer.toString();
    }

    /**
     */
    private Address getAdressFromLocation(Location location, Context context) {
        Geocoder geocoder = new Geocoder(context);

        try {
            List<Address> addresses = geocoder
                    .getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {
                //Log.i(Constants.HMS_INFO, "Detected current address: " + address);
                return addresses.get(0);
            }

        } catch (IOException e) {
            e.printStackTrace();

            Log.d(Constants.HMS_INFO, "Couldn't reverse geocode from location: " + location);
        }

        return null;
    }

    @Override
    public void addContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_CONTACT_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursorContact = getContentResolver().query(contactData, null, null, null, null);
                    if (cursorContact.moveToFirst()) {
                        String contactName = cursorContact
                                .getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String contactId = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasPhone = cursorContact
                                .getString(cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String contactPhoneNumber = null;

                        if ("1".equals(hasPhone) || Boolean.parseBoolean(hasPhone)) {
                            Cursor cursorPhoneNumbers = this.getContentResolver()
                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null,
                                            null);
                            while (cursorPhoneNumbers.moveToNext()) {
                                contactPhoneNumber = cursorPhoneNumbers.getString(
                                        cursorPhoneNumbers
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            cursorPhoneNumbers.close();
                        }

                        if (contactPhoneNumber != null) {
                            Log.i(LOCATION_SCREEN_TAG,
                                    "Picked contact:" + contactName + "-" + contactPhoneNumber);
                            locationModelManager.addContact(contactName, contactPhoneNumber);
                        }
                    }

                    cursorContact.close();
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (PERMISSIONS_REQUEST_CODE == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) { //ask again
                askForNeededPermissions();
                textToSpeech.speak("The app cannot work without the required permissions!",
                        TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

    public void sendMessage(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            Log.i(LOCATION_SCREEN_TAG,
                    "Could not send help msg to : " + phoneNo + ".\n Reason: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}
