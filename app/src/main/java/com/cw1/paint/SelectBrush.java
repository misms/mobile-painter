package com.cw1.paint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SelectBrush extends AppCompatActivity {

    private static SeekBar seekBar;
    private TextView currentValue;
    public int currentBrushSize, changedBrushSize = 0;
    public String currentBrushShape, changedBrushShape = null;
    public Button roundShape,squareShape,okBtn,cancelBtn;
    public Bundle result_bundle = new Bundle();
    public Intent result = new Intent();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_brush);

        Bundle bundle = getIntent().getExtras();
        currentBrushSize = bundle.getInt("currentBrushSize");

        String shape= bundle.getString("currentBrushShape");
        currentBrushShape = shape;

        displaySeekBar(currentBrushSize);

    }

    public void displaySeekBar(int currentBrushSize) {
        // inflating required widget on the interface
        seekBar = (SeekBar)findViewById(R.id.seekBar2);
        currentValue =(TextView)findViewById(R.id.currentBrush);
        roundShape = (Button) findViewById(R.id.roundBtn);
        squareShape = (Button) findViewById(R.id.squareBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        okBtn = (Button) findViewById(R.id.okBtn);

        currentValue.setText(currentValue.getText().toString() + currentBrushSize);
        seekBar.setProgress(currentBrushSize);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress_value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                currentValue.setText("Current Brush Size : " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                currentValue.setText("SHAPE :" + currentBrushShape);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentValue.setText("Current Brush Size : " + progress_value);
                changedBrushSize = progress_value;
            }

        });

        View.OnClickListener okBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBrushUpdate();
            }
        };
        okBtn.setOnClickListener(okBtnClickListener);

        View.OnClickListener roundShapeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRoundBtn();
            }
        };
        roundShape.setOnClickListener(roundShapeClickListener);

        View.OnClickListener squareShapeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSquareBtn();
            }
        };
        squareShape.setOnClickListener(squareShapeClickListener);

        View.OnClickListener cancelBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelBtn();
            }
        };
        cancelBtn.setOnClickListener(cancelBtnClickListener);
    }

    //confirm & update brush size
        public void onBrushUpdate(){
        if(changedBrushSize == 0){
            changedBrushSize = currentBrushSize;
        }
        if(changedBrushShape == null){
            changedBrushShape = currentBrushShape;
        }
        result_bundle.putInt("changedBrushSize", changedBrushSize);
        result_bundle.putString("changedBrushShape",changedBrushShape.toString());
        result.putExtras(result_bundle);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
        private void onRoundBtn(){
            changedBrushShape = Paint.Cap.ROUND.toString();
        }
        private void onSquareBtn(){
            changedBrushShape = Paint.Cap.SQUARE.toString();
        }
        private void onCancelBtn() {
            finish();
            Toast.makeText(getBaseContext(), "No Changes Made! ", Toast.LENGTH_LONG).show();
        }
}

//todo: start of seekbar should be the correct position
//todo: ok/cancel button
//todo: send information for changes
//todo: brush shape
//todo: reference
// REFERENCE LIST
//ref : https://developer.android.com/reference/android/graphics/Paint.Cap.html
//ref : https://code.google.com/archive/p/android-color-picker/
//ref : http://wptrafficanalyzer.in/blog/android-color-picker-application-using-ambilwarna-color-picker-library/
//ref : https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-essential-functionality--mobile-19328
//ref : http://www.codebind.com/android-tutorials-and-examples/android-studio-android-seekbar-example/
//ref : http://valokafor.com/android-drawing-app-tutorial-pt-2/
//ref : https://www.codeproject.com/Articles/1045443/Android-Drawing-App-Tutorial-Pt
//ref : https://developer.android.com/reference/android/graphics/Paint.Cap.html
