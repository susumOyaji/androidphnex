package com.example.androidphnex;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

//orignal to Add
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.telecom.TelecomManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.telecom.Call;
import android.content.Context;

import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.telecom.InCallService;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;



public class MainActivity extends FlutterActivity 
{
  private static final String CHANNEL = "samples.flutter.io/androidphone";
  private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 1;
  private static Call call;
  /*
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
                new MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, Result result) {
                      if (call.method.equals("getBatteryLevel")) {
                        int batteryLevel = getBatteryLevel();

                        if (batteryLevel != -1) {
                          result.success(batteryLevel);
                        } else {
                          result.error("UNAVAILABLE", "Battery level not available.", null);
                        }
                      } else {
                        result.notImplemented();
                      }   // TODO
                    }
                });
  }//onCreate
  */
  
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        
        new App();
        Toast.makeText(MainActivity.this, "Started the MainActivity.app", Toast.LENGTH_SHORT).show();
        CallService CallService = new CallService();
        //CallService.onCallAdded();

        new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
          new MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, Result result) {//TODO
                    Toast.makeText(MainActivity.this, "Started theMethodChannel ", Toast.LENGTH_SHORT).show();
                      if (call.method.equals("androidphone")) {
                        int phonestate = TelPhoneCall();

                        if (phonestate != -1) {
                          result.success(phonestate);
                        } else {
                          result.error("UNAVAILABLE", "AndroidPhone not available.", null);
                        }
                      } else {
                        result.notImplemented();
                      }   // TODO
                    }
                }
          );
   
  }


  private int TelPhoneCall() {
    Toast.makeText(MainActivity.this, "TelPhoneDial",Toast.LENGTH_SHORT).show();


    int phonestate = -1;
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      OngoingCall.answer();
    }
      return phonestate;
  }








}
