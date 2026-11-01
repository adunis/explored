package com.adunis.explored;

import android.app.Activity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.widget.Toast;
import android.content.Context;

public class MainActivity extends Activity {
    private WebView web;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        web = new WebView(this);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        web.addJavascriptInterface(new Bridge(), "ExploredAndroid");
        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }
    class Bridge {
        @JavascriptInterface public void share(String data) {
            runOnUiThread(() -> {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("application/json");
                i.putExtra(Intent.EXTRA_TEXT, data);
                startActivity(Intent.createChooser(i, "Export explored places"));
            });
        }
        @JavascriptInterface public void locate() {
            runOnUiThread(() -> {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 42);
                    return;
                }
                updateLocation();
            });
        }
    }
    private void updateLocation() {
        try {
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) web.evaluateJavascript("window.goToPosition("+loc.getLatitude()+","+loc.getLongitude()+")",null);
            else Toast.makeText(this,"No recent GPS location available",Toast.LENGTH_LONG).show();
        } catch (Exception ex) { Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show(); }
    }
    @Override public void onRequestPermissionsResult(int code,String[] perms,int[] results) {
        super.onRequestPermissionsResult(code,perms,results);
        if(code==42 && results.length>0 && results[0]==PackageManager.PERMISSION_GRANTED) updateLocation();
    }
    @Override public void onBackPressed() {
        web.evaluateJavascript("window.closeEditor && window.closeEditor()",null);
    }
}
