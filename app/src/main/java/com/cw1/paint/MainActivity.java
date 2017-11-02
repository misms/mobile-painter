package com.cw1.paint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private FingerPainterView painterView;

    final private int SELECT_COLOR_REQUEST_CODE = 33;
    final private int SELECT_BRUSH_REQUEST_CODE = 44;
    final private int SELECT_IMAGE_CODE = 55;
    private double someNumber = 1.1119;
    private ImageButton pickColorBtn;
    private ImageButton pickBrushBtn;
    private ImageButton clearCanvasBtn;
    private ImageButton importImageBtn;
    private ImageButton saveImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        //checking if another application want to open image with this app
        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                createMainInterface();
                Log.i("MAIN ACTIVITY", "TRYING TO OPEN URI IMAGE FROM GALLERY");
                painterView.load(intent.getData());
            }
        }else {
            createMainInterface();
        }
    }

    private void createMainInterface() {

        painterView = (FingerPainterView) findViewById(R.id.painter_view);
        pickColorBtn = (ImageButton) findViewById(R.id.select_color);
        pickBrushBtn = (ImageButton) findViewById(R.id.select_brush);
        clearCanvasBtn = (ImageButton) findViewById(R.id.clear_canvas);
        importImageBtn = (ImageButton) findViewById(R.id.import_image);
        saveImageBtn = (ImageButton) findViewById(R.id.save_image);

//      setting up each listener for buttons declared above
        View.OnClickListener colorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(painterView.getColour());
            }
        };
        pickColorBtn.setOnClickListener(colorClickListener);

        View.OnClickListener brushClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrushPicker(painterView.getBrush(),painterView.getBrushWidth());
            }
        };
        pickBrushBtn.setOnClickListener(brushClickListener);

        View.OnClickListener clearClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickClearCanvas();
            }
        };
        clearCanvasBtn.setOnClickListener(clearClickListener);

        View.OnClickListener importImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImportImage();
            }
        };
        importImageBtn.setOnClickListener(importImageClickListener);

        View.OnClickListener saveImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSaveImage();
            }
        };
        saveImageBtn.setOnClickListener(saveImageClickListener);
    }

    private void onClickSaveImage() {
        Bitmap  bitmap = Bitmap.createBitmap( painterView.getWidth(), painterView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        painterView.draw(canvas);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path+"/image.png");
        FileOutputStream ostream;
        try {
            file.createNewFile();
            ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
            Toast.makeText(getApplicationContext(), "image saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error saving image", Toast.LENGTH_LONG).show();
        }
    }

    private void onClickImportImage() {
        Intent importIntent  = new Intent(Intent.ACTION_PICK);
        File imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String imageDirectoryPath = imageDirectory.getPath();
        Uri data = Uri.parse(imageDirectoryPath);
        importIntent.setDataAndType(data,"image/*");
        startActivityForResult(importIntent,SELECT_IMAGE_CODE);
    }

    private void openBrushPicker(Paint.Cap brush, int brushWidth) {
        Bundle bundle = new Bundle();
        bundle.putInt("currentBrushSize",brushWidth);
        bundle.putString("currentBrushShape",brush.toString());
        Intent intent = new Intent(MainActivity.this,SelectBrush.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,SELECT_BRUSH_REQUEST_CODE);
    }

    private void openColorPicker(int color) {
        Bundle bundle = new Bundle();
        bundle.putInt("currentColor", color);
        Intent intent = new Intent(MainActivity.this,SelectColor.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,SELECT_COLOR_REQUEST_CODE);
    }

    protected void onClickClearCanvas(){
        painterView.onClear();
    }

    public void onClickIcon(View v){
        Toast.makeText(getBaseContext(), "You just clicked on Fake Icon ;) ", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_COLOR_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                int newColor = bundle.getInt("changedColor");
                painterView.setColour(newColor);

                Log.i("MainActivity","COLOR WAS CHANGED TO ----->" + newColor);
            }
            else if(resultCode == RESULT_CANCELED){
                Log.i("MainActivity","RESULT WAS CANCELED!");
            }
        }

        if(requestCode == SELECT_BRUSH_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                painterView.setBrushWidth(bundle.getInt("changedBrushSize"));
                String brushShape = bundle.getString("changedBrushShape");
                Paint.Cap newBrushShape = Paint.Cap.valueOf(brushShape);
                painterView.setBrush(newBrushShape);
                Toast.makeText(getBaseContext(), "BRUSH UPDATED! ", Toast.LENGTH_LONG).show();

            }else if(resultCode == RESULT_CANCELED){
                Log.i("MainActivity","BRUSH UPDATE IS CANCELED");
            }
        }
        if(requestCode == SELECT_IMAGE_CODE){
            if(resultCode == RESULT_OK){
                painterView.load(data.getData());
                Toast.makeText(getBaseContext(), "Image uri data Imported! " + data.getData(), Toast.LENGTH_LONG).show();

            }else if(resultCode == RESULT_CANCELED){
                Log.i("MainActivity","IMAGE IMPORT IS CANCELED");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble("myDouble",someNumber);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.someNumber = savedInstanceState.getDouble("myDouble");
    }

}
