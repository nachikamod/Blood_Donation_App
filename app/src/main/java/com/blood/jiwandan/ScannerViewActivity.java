package com.blood.jiwandan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScannerViewActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA=1;
    private ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();

            }
            else
            {
                requestPermission();
            }
        }
    }

    private boolean checkPermission ()
    {
        return (ContextCompat.checkSelfPermission(ScannerViewActivity.this, CAMERA)== PackageManager.PERMISSION_GRANTED);

    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode,String permission[],int grantResults[])
    {
        switch (requestCode){
            case REQUEST_CAMERA:
                if (grantResults.length>0)
                {
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted)
                    {
                        Toast.makeText(ScannerViewActivity.this,"permission granted",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(ScannerViewActivity.this,"permission denied",Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if (shouldShowRequestPermissionRationale(CAMERA))
                            {
                                displayAlertMessage("allow access", new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        requestPermissions(new String[]{CAMERA},REQUEST_CAMERA);
                                    }
                                });
                                return;

                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                if (scannerView==null)
                {
                    scannerView=new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();

            }
            else
            {
                requestPermission();
            }
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String Message, DialogInterface.OnCancelListener listener)
    {
        new AlertDialog.Builder(ScannerViewActivity.this)
                .setMessage(Message)
                .setPositiveButton("ok", (DialogInterface.OnClickListener) listener)
                .setNegativeButton("cancel",null)
                .create()
                .show();

    }


    @Override
    public void handleResult(Result result) {
        final String scanResult=result.getText();
        Intent intent=new Intent(this,DocumentationActivity.class);
        intent.putExtra("uid",scanResult);
        startActivity(intent);


    }
}
