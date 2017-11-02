package com.cw1.paint;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import yuku.ambilwarna.AmbilWarnaDialog;

public class SelectColor extends AppCompatActivity {

    private int initialColor;
    private Button reopen,backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_color);

        Bundle bundle = getIntent().getExtras();
        initialColor = bundle.getInt("currentColor");
        colorPicker(initialColor);

        reopen = (Button) findViewById(R.id.colorPicker);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker();
            }
        };
        reopen.setOnClickListener(clickListener);

        backToMain = (Button) findViewById(R.id.backToMain);
        View.OnClickListener clicksListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        backToMain.setOnClickListener(clicksListener);
    }

    private void colorPicker(int initialColor) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            // Executes, when user click Cancel button
            @Override
            public void onCancel(AmbilWarnaDialog dialog){
            }

            // Executes, when user click OK button
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getBaseContext(), "Selected Color Code : " + color, Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putInt("changedColor", color);

                Intent result = new Intent();
                result.putExtras(bundle);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
        dialog.show();
    }

    //to reopen color picker if user accidently pressed outside from the dialog
    private void colorPicker() {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, initialColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog){
            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getBaseContext(), "Selected Color : " + color, Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putInt("changedColor", color);

                Intent result = new Intent();
                result.putExtras(bundle);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });
        dialog.show();
    }
}



