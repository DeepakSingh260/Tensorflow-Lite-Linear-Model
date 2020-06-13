package com.example.machinelearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    private EditText  Input;
    private TextView Output;
    Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Input = (EditText) findViewById(R.id.Input);
       Output = (TextView) findViewById(R.id.Outputs);
       Button buttom = (Button) findViewById(R.id.button);

       try {
           tflite = new Interpreter(loadModelFile());
       }catch (Exception e){
           e.printStackTrace();
       }

       buttom.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               float prediction = inference(Input.getText().toString());
               Output.setText(Float.toString(prediction));
                Input.setText("");
           }
       });

    }

    public float inference(String s){
        float [] inputValue = new float[1];
        inputValue[0] = Float.valueOf(s);

        float[][] outputValue = new float[1][1];
        tflite.run(inputValue,outputValue);
        float inferedValue = outputValue[0][0];
        return  inferedValue;
    }

    private MappedByteBuffer loadModelFile() throws  IOException{
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("foo.tflite");
        FileInputStream fileInputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffSets = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return  fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffSets,declaredLength);

    }


}
