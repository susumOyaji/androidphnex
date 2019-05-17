// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:flutter/material.dart';
//import 'package:url_launcher/url_launcher.dart';
//import 'url_launcher.dart';
import 'package:flutter/services.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'URL Launcher',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'URL Launcher'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = const MethodChannel('samples.flutter.io/androidphone');
  Future<void> _launched;
  String _phone = '09024922369';

   // Get battery level.
  String _androidphone = 'Unknown androidphone.';

  Future<void> _getphonestate() async {
    String phonestate;
    try {
      final int result = await platform.invokeMethod('androidphone');
      phonestate = 'androidphone atate at $result % .';
    } on PlatformException catch (e) {
      phonestate = "Failed to get androidphone: '${e.message}'.";
    }

    setState(() {
      _androidphone = phonestate;
    });
  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
      title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: TextField(
                  onChanged: (String text) => _phone = text,
                  decoration: const InputDecoration(
                      hintText: 'RepeatCall Telephone number')),
            ),
            RaisedButton(
              onPressed: () => setState(() {
                    _launched = _getphonestate();
                  }),
              child: const Text('Make phone call'),
            ),
            const Padding(padding: EdgeInsets.all(16.0)),
            RaisedButton(
              child: const Text('Call me'),
              onPressed:() => _getphonestate(),
            ),
            
           
            const Padding(padding: EdgeInsets.all(16.0)),
            //FutureBuilder<void>(future: _launched, builder: _launchStatus),
          ],
        ),
      ),
    );
  }
}


//flutter create --org com.sumitomo --template=plugin -i swift -a java ticketcall
