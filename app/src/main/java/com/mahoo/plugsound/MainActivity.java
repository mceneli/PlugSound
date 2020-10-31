package com.mahoo.plugsound;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Locale;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    TextToSpeech t1;
    EditText ed1,ed2;
    Button b1,b2,b3;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);

        startService(new Intent(getApplicationContext(), BgService.class));

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        ed1.setText(sharedpreferences.getString("connect",getString(R.string.connected)));
        ed2.setText(sharedpreferences.getString("disconnect",getString(R.string.disconnected)));

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    //t1.setLanguage(Locale.UK);
                    t1.setLanguage(new Locale("tr", "TR"));
                }
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String connectSound = ed1.getText().toString();
                String disconnectSound = ed2.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("connect", connectSound);
                editor.putString("disconnect", disconnectSound);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Kaydedildi",Toast.LENGTH_SHORT).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(ed1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(ed2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        final String defaultlanguage = Locale.getDefault().getDisplayLanguage();
        System.out.println(defaultlanguage);
    }

}