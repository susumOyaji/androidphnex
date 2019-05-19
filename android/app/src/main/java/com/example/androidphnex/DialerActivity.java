package com.example.androidphnex;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;


import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.widget.EditText;
import java.util.ArrayList;
import android.Manifest;
import android.app.Activity;
import android.widget.Toast;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;





import static android.Manifest.permission.CALL_PHONE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;


public class DialerActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.io/androidphone";
    String phoneNumberInput;
    private static final int REQUEST_PERMISSION = 0;
    static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);    
        GeneratedPluginRegistrant.registerWith(this);
        Toast.makeText(DialerActivity.this, "Started the DialerActivity.app", Toast.LENGTH_SHORT).show();

        //setContentView(R.layout.activity_dialer);

        phoneNumberInput = "1234567890";//findViewById(R.id.phoneNumberInput);
        
        // get Intent data (tel number)
        //if (getIntent().getData() != null)
        //    phoneNumberInput.setText(getIntent().getData().getSchemeSpecificPart());
    }

    @Override
    public void onStart() {
        super.onStart();
                
        //ユーザがDialerユーザインターフェイスを経由せずに通話を開始し、通話を確認することをアプリケーションに許可します。
        ActivityCompat.requestPermissions(this, new String[]{
            Manifest.permission.CALL_PHONE
        }, REQUEST_CODE);

        offerReplacingDefaultDialer();    
      

        //phoneNumberInput.setOnEditorActionListener((v, actionId, event) -> {
        //makeCall();
        //   return true;
        //});
    }

    @SuppressLint("MissingPermission")
    private void makeCall() {
        Toast.makeText(DialerActivity.this, "makeCall", Toast.LENGTH_SHORT).show();
        // If permission to call is granted
        if (checkSelfPermission(CALL_PHONE) == PERMISSION_GRANTED) {
            // Create the Uri from phoneNumberInput
            Uri uri = Uri.parse("tel:"+phoneNumberInput);
            // Start call to the number in input
            startActivity(new Intent(Intent.ACTION_CALL, uri));
        } else {
            // Request permission to call
            ActivityCompat.requestPermissions(this, new String[]{CALL_PHONE}, REQUEST_PERMISSION);
        }
        

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        ArrayList<Integer> grantRes = new ArrayList<>();
        // Add every result to the array
        for (Integer grantResult: grantResults) grantRes.add(grantResult);
        if (requestCode == REQUEST_PERMISSION && grantRes.contains(PERMISSION_GRANTED)) {
            Toast.makeText(DialerActivity.this, "makeCall & onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
            makeCall();
        }

    }

    //ユーザーが自分のアプリをデフォルトの電話アプリとして設定する
    private void offerReplacingDefaultDialer() {
        TelecomManager systemService = this.getSystemService(TelecomManager.class);
        if (systemService != null && !systemService.getDefaultDialerPackage().equals(this.getPackageName())) {
        startActivity((new Intent(ACTION_CHANGE_DEFAULT_DIALER)).putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, this.getPackageName()));
        }

        /*ユーザーが自分のアプリをデフォルトの電話アプリとして設定する
        if (getSystemService(TelecomManager::class.java).defaultDialerPackage != packageName) {
            Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
            .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
            .let(::startActivity)
        }
        */



        //Toast.makeText(DialerActivity.this, "offerReplacingDefaultDialer", Toast.LENGTH_SHORT).show();
    }


     


}
