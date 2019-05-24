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
        
        
        
        new App();
        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
          new MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, Result result) {//TODO
                    this.result = result;

                    Toast.makeText(DialerActivity.this, "Started theMethodChannel ", Toast.LENGTH_SHORT).show();
                      if (call.method.equals("androidphone")) {
                        // invokeMethodの第二引数で指定したパラメータを取得できます
                        parameters = (String)call.arguments;
                        int phonestate = makeCall(parameters);
                        

                        if (phonestate != -1) {
                          result.success(phonestate);
                        } else {
                          result.error("UNAVAILABLE", "AndroidPhone not available.", null);
                        }
                      } else {
                        result.notImplemented();
                      } // TODO
                     
                      //  val parameters = call.arguments<String>();

                    }
                }
          );
    }

    @Override
    public void onStart() {
        super.onStart();
         offerReplacingDefaultDialer();  

        //phoneNumberInput.setOnEditorActionListener((v, actionId, event) -> {
        //makeCall();
        //   return true;
        //});
    }

     // startActivityForResult で起動させたアクティビティが
  // finish() により破棄されたときにコールされる
  // requestCode : startActivityForResult の第二引数で指定した値が渡される
  // resultCode : 起動先のActivity.setResult の第一引数が渡される
  // Intent data : 起動先Activityから送られてくる Intent
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == COUNT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Dart側にsuccessとして結果を渡します
                result.success("result");
            } else {
                // error通知(第三引数は、任意のオブジェクトを渡せます)
                result.error("ErrorCode", "ErrorMessage", null);
            }
        }
  }







    @SuppressLint("MissingPermission")
    public int makeCall(String _phone) {
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
        //Toast.makeText(DialerActivity.this, "makeCall     " + _phone, Toast.LENGTH_SHORT).show();
       
        //CharSequence tv = CallActivity.getCallInfo();
        Toast.makeText(DialerActivity.this, "onRequestPermissionsResult" , Toast.LENGTH_SHORT).show();
        
        return 1;//tv;//buttonHidden.setText(CallStateString.asString(OngoingCall.state));

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
