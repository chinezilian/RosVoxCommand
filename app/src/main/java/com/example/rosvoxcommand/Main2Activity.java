package com.example.rosvoxcommand;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.Activity;

import java.util.ArrayList;
import java.util.Locale;

public class Main2Activity extends Activity {
    private static final int REQ_COD_INPUT =1000;
    private EditText editText;
    private static String SaidaVoz;
    int i=0;

    public static void setSaidaVoz(String saidaVoz) {
        SaidaVoz = saidaVoz;
    }
    public static String getSaidaVoz() {
        return SaidaVoz;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        checkPermission();
        editText = findViewById(R.id.edit_text);
        final ImageButton mButton;
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio();


            }
        });


    }
    private void inicio() {
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pode dizer:");
        try {
            startActivityForResult(mSpeechRecognizerIntent, REQ_COD_INPUT);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "O celular não suporta voz",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int id, int resultCode, Intent dados){
        super.onActivityResult(id,resultCode,dados);
        switch (id)
        {
            case REQ_COD_INPUT:
                if(resultCode==RESULT_OK&&null != dados) {
                    ArrayList<String> result = dados.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String EntradaVoz;
                    i=0;
                    EntradaVoz = result.get(0);
                    SaidaVoz=comparator(EntradaVoz);
                    editText.setText(SaidaVoz);

                }
                break;

        }

    }
    String comparator(String fala) {
        String listCommand[] = {"frente", "vira direita", "vira a esquerda", "traz"};
        for (int i = 0; i <= listCommand.length - 1; i++) {
            if (listCommand[i].equalsIgnoreCase(fala)) {
                Toast.makeText(getApplicationContext(),fala,Toast.LENGTH_SHORT).show();
                break;
            } else if (listCommand.length - 1 == i) {
                Toast.makeText(getApplicationContext(),"Comando invalido,tente novamente",Toast.LENGTH_SHORT).show();
                fala="invalido";
            }
        }
        return fala;

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }


    public void startSecondActivity(View view) {

        Intent secondActivity = new Intent(this, MainActivity.class);
        startActivity(secondActivity);
    }
}
