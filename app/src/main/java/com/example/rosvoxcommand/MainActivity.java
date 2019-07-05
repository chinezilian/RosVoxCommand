package com.example.rosvoxcommand;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;

import android.widget.ImageButton;
import android.widget.Toast;

import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.apache.commons.logging.Log;

public class MainActivity extends RosActivity {
    private RosTextView<std_msgs.String> rosTextView;
    private static Talker talker;
    private Listener listener;
    private static EditText editT;
    // private ImageButton mButton;
    private static String SaidaVoz;
    static final Log log = null;

    public static String getSaidaVoz() {
        return SaidaVoz;
    }

    public static void setSaidaVoz(String saidaVoz) {
        SaidaVoz = saidaVoz;
    }
    public MainActivity() {
        // The RosActivity constructor configures the notification title and ticker
        // messages.
        super("RosVox", "RosVox");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rosTextView = (RosTextView<std_msgs.String>) findViewById(R.id.text);
        rosTextView.setTopicName("chatter");
        rosTextView.setMessageType(std_msgs.String._TYPE);
        rosTextView.setMessageToStringCallable(new MessageCallable<String, std_msgs.String>() {
            @Override
            public String call(std_msgs.String message) {
                return message.getData();
            }
        });

        editT = findViewById(R.id.editText);
       // mButton=findViewById(R.id.button);

     /*   mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio();
            }
        });*/

     inicio(Main2Activity.getSaidaVoz());

    }

    static void inicio(String voz) {
        if(voz!=null) {
            SaidaVoz = voz;
            editT.setText(voz);
        }
    }


   /* private void inicio() {
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
                    String EntradaVoz = result.get(0);
                    SaidaVoz=comparator(EntradaVoz);
                    editT.setText(SaidaVoz);
                }
                    break;

        }

    }
    String comparator(String fala) {
        String listCommand[] = {"frente", "gira direita", "gira esquerda", "vira direita", "vira esquerda", "atrás esquerda", "atrás direita", "atrás"};
         for (int i = 0; i <= listCommand.length - 1; i++) {
            if (listCommand[i].equalsIgnoreCase(fala)) {
                Toast.makeText(getApplicationContext(),fala,Toast.LENGTH_SHORT).show();
                break;
            } else if (listCommand.length - 1 == i) {
                Toast.makeText(getApplicationContext(),"Comando invalido,tente novamente",Toast.LENGTH_SHORT).show();
                fala=null;
            }
        }
        return fala;

    }*/

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        talker = new Talker();
        listener = new Listener();
        // At this point, the user has already been prompted to either enter the URI
        // of a master to use or to start a master locally.

        // The user can easily use the selected ROS Hostname in the master chooser
        // activity.
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(talker, nodeConfiguration);
        nodeMainExecutor.execute(listener, nodeConfiguration);
        // The RosTextView is also a NodeMain that must be executed in order to
        // start displaying incoming messages.
        nodeMainExecutor.execute(rosTextView, nodeConfiguration);
    }
    public void startActivity(View view) {

        Intent secondActivity = new Intent(this, Main2Activity.class);
        startActivity(secondActivity);
    }

}
