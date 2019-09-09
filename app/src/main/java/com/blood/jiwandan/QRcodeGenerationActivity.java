package com.blood.jiwandan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRcodeGenerationActivity extends AppCompatActivity {
    private TextView key;
    private ImageView QRcode;
    public String Key;
    private Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private String TAG="qr generate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generation);

        Intent KeyRecive = getIntent();

        Key = getIntent().getExtras().get("key").toString().trim();

        initializeFields();

        key.setText(Key);

        if (Key.length()>0){
            WindowManager manager=(WindowManager)getSystemService(WINDOW_SERVICE);
            Display QR=manager.getDefaultDisplay();
            Point point=new Point();
            QR.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallDimention=width<height ? width:height;
            smallDimention=smallDimention*3/4;
            qrgEncoder=new QRGEncoder(Key,null, QRGContents.Type.TEXT,smallDimention);
            try {
                bitmap=qrgEncoder.encodeAsBitmap();
                QRcode.setImageBitmap(bitmap);
            }
            catch (WriterException e){
                Log.v(TAG,e.toString());
            }


        }

    }

    private void initializeFields() {

        key=(TextView)findViewById(R.id.key);
        QRcode=(ImageView)findViewById(R.id.QRcode);

    }
}
