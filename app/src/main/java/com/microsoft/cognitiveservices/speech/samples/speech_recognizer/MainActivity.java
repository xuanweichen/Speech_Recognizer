package com.microsoft.cognitiveservices.speech.samples.speech_recognizer;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import java.util.concurrent.Future;

import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity {

    // Replace below with your own subscription key
    private static String speechSubscriptionKey = "75e2f2cf3bda44c0b7a43ea56ed89cb3";
    // Replace below with your own service region (e.g., "westus").
    private static String serviceRegion = "westus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Note: we need to request the permissions
        int requestCode = 5; // unique code for the permission request
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, INTERNET}, requestCode);
    }

    public void onSpeechButtonClicked(View v) {
        Log.d("tag", "Start recognising...");
        TextView txt = (TextView) this.findViewById(R.id.hello); // 'hello' is the ID of your text view

        try {
            SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
            assert(config != null);

            SpeechRecognizer reco = new SpeechRecognizer(config);
            assert(reco != null);

            Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();
            assert(task != null);

            // Note: this will block the UI thread, so eventually, you want to
            //        register for the event (see full samples)
            SpeechRecognitionResult result = task.get();
            assert(result != null);
            Log.d("tag", ResultReason.RecognizedSpeech.toString());

            if (result.getReason() == ResultReason.RecognizedSpeech) {
                String output_result = result.toString();
                String answer = "";
                for (int i = output_result.length()-4; i > 0; i--){
                    if(output_result.charAt(i) != '<'){
                        answer = output_result.charAt(i) + answer;
                    }else {
                        break;
                    }
                }
                answer = answer.toLowerCase();

                if (answer.contains("yes")){
                    txt.setText("Yes");
                    //Add code to pull up the pdf

                }else {
                    txt.setText(answer);
                    //Go back and restart detecting objects
                }
            }
            else {
                txt.setText("Not recognizing. Did you update the subscription info?" + System.lineSeparator() + result.toString());
            }

            reco.close();
        } catch (Exception ex) {
            Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
            assert(false);
        }
    }
}
