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
import android.telecom.InCallService;
import android.widget.EditText;
import java.util.ArrayList;
import android.Manifest;
import android.app.Activity;
import android.widget.Toast;
import android.widget.TextView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;




import static android.Manifest.permission.CALL_PHONE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;


public class DialerActivity extends FlutterActivity {
    private static final String CHANNEL = "samples.flutter.io/androidphone";
    private static  final String EXTRA_STRING = "extra_string";
    //CallActivity callActivity = new CallActivity();
     
    String phoneNumberInput;
    String parameters;
    

    private static final int REQUEST_PERMISSION = 0;
    static final int REQUEST_CODE = 1;
    private static final int REQUEST_ID = 1;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);    
        GeneratedPluginRegistrant.registerWith(this);


        Toast.makeText(DialerActivity.this, "Started the DialerActivity.app", Toast.LENGTH_SHORT).show();
        
        
        //ユーザがDialerユーザインターフェイスを経由せずに通話を開始し、通話を確認することをアプリケーションに許可します。
        ActivityCompat.requestPermissions(this, new String[]{
            Manifest.permission.CALL_PHONE
        }, REQUEST_CODE);

        //setContentView(R.layout.activity_dialer);

        phoneNumberInput = "123456789";//findViewById(R.id.phoneNumberInput);
        
        // get Intent data (tel number)
        //if (getIntent().getData() != null)
        //  phoneNumberInput.setText(getIntent().getData().getSchemeSpecificPart());
        
        
        
        //new App();
        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
          new MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, Result result) {//TODO
                      if (call.method.equals("androidphone")) {
                        Toast.makeText(DialerActivity.this, "Started theMethodChannel to makeCall", Toast.LENGTH_SHORT).show();

                        // invokeMethodの第二引数で指定したパラメータを取得できます
                        parameters = call.arguments.toString();
                        String phonestate = makeCall(parameters);
                        
                        if (phonestate != null) {
                          result.success(phonestate);//return to Flutter
                        } else {
                          result.error("UNAVAILABLE", "AndroidPhone not available.", null);
                        }
                      } else {
                              if (call.method.equals("hangup")) {
                                Toast.makeText(DialerActivity.this, "Started theMethodChannel to hangup ", Toast.LENGTH_SHORT).show();
                                // invokeMethodの第二引数で指定したパラメータを取得できます
                                boolean hangupparameters = (boolean)call.arguments;
                                boolean hangup = hangup(hangupparameters);
                        
                                if (hangup != true) {
                                  result.success(hangup);
                                } else {
                                  result.error("UNAVAILABLE", "Hangup not available.", null);
                                }
                              } else {
                                result.notImplemented();//該当するメソッドが実装されていない
                              } // TOD
                      } // TODO


                     /*
                      if (call.method.equals("hangup")) {
                        Toast.makeText(DialerActivity.this, "Started theMethodChannel to hangup ", Toast.LENGTH_SHORT).show();
                        // invokeMethodの第二引数で指定したパラメータを取得できます
                        boolean hangupparameters = (boolean)call.arguments;
                        boolean hangup = hangup(hangupparameters);
                        
                        if (hangup != true) {
                          result.success(hangup);
                        } else {
                          result.error("UNAVAILABLE", "Hangup not available.", null);
                        }
                      } else {
                        result.notImplemented();
                      } // TODO
                      */
                     
                    }
                }
          );
    }

    @Override
    public void onStart() {
        super.onStart();
         offerReplacingDefaultDialer();  
    }

  







    @SuppressLint("MissingPermission")
    public String makeCall(String _phone) {
        // If permission to call is granted
        if (checkSelfPermission(CALL_PHONE) == PERMISSION_GRANTED) {
            // Create the Uri from phoneNumberInput
            Uri uri = Uri.parse("tel:"+ _phone);
            
            // Start call to the number in input
            startActivity(new Intent(Intent.ACTION_CALL, uri));
        } else {
            // Request permission to call
            ActivityCompat.requestPermissions(this, new String[]{CALL_PHONE}, REQUEST_PERMISSION);
        }
             
        String tv = CallActivity.PhoneState;
        Toast.makeText(DialerActivity.this, "makeCall  " + tv , Toast.LENGTH_SHORT).show();
        
        return tv;
    }

   
    public boolean hangup(boolean hangup) {
      Toast.makeText(DialerActivity.this, "hangup  to True ", Toast.LENGTH_SHORT).show();
      //CallActivity.onHangup();
      OngoingCall.hangup();
      return true;
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        Toast.makeText(DialerActivity.this, "onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
        ArrayList<Integer> grantRes = new ArrayList<>();
        // Add every result to the array
        for (Integer grantResult: grantResults) grantRes.add(grantResult);
        if (requestCode == REQUEST_PERMISSION && grantRes.contains(PERMISSION_GRANTED)) {
            makeCall(parameters);
        }

    }



    //ユーザーが自分のアプリをデフォルトの電話アプリとして設定する
    private void offerReplacingDefaultDialer() {
        TelecomManager systemService = this.getSystemService(TelecomManager.class);
        if (systemService != null && !systemService.getDefaultDialerPackage().equals(this.getPackageName())) {
        startActivity((new Intent(ACTION_CHANGE_DEFAULT_DIALER)).putExtra(EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, this.getPackageName()));
        }
    }


     


}
