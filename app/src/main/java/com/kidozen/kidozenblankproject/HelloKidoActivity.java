package com.kidozen.kidozenblankproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpStatus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import kidozen.client.InitializationException;
import kidozen.client.KZApplication;
import kidozen.client.ServiceEvent;
import kidozen.client.ServiceEventListener;



public class HelloKidoActivity extends Activity {

    public static final String TAG = "HelloKido Activity";
    final String TENANT = "https://YOURMARKEPLACE.kidocloud.com/";
    final String APPLICATION = "appName";
    final String AppKey = "appKEY";
    final String KidoZenProvider = "Kidozen";
    final String KidoZenUser = "USER@EMAIL.com";
    final String KidoZenPassword = "?????";

    Button authButton ;
    TextView textMessage;

    KZApplication app;

    private View.OnClickListener authenticateWithKidozen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            try {
                app.Authenticate(KidoZenProvider, KidoZenUser, KidoZenPassword, new ServiceEventListener() {
                    @Override
                    public void onFinish(ServiceEvent e) {
                        if (e.StatusCode != HttpStatus.SC_OK) {
                            Log.d(TAG, "**** ERROR MESSAGE: Unable to reach the kidozen server. Make sure your KidoZenAppCenterUrl and KidoZenAppName are correct");
                        } else {
                            textMessage.setText("Hello: " + app.GetKidoZenUser().Claims.get("name"));
                            Log.d(TAG, app.GetKidoZenUser().Claims.get("name"));
                            Log.d(TAG, "KidoZen autentication sucessful.");
                        }

                    }
                });
            } catch (InitializationException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_kido);
        textMessage = (TextView)findViewById(R.id.textViewMessages);

        authButton = (Button)findViewById(R.id.buttonAuthenticate);
        authButton.setOnClickListener(authenticateWithKidozen);
        authButton.setEnabled(false);

        try {
            app = new KZApplication(TENANT,APPLICATION,AppKey,true);

            app.Initialize(new ServiceEventListener() {
                @Override
                public void onFinish(ServiceEvent e) {
                    authButton.setEnabled(true);
                }
            });


        }
        catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hello_kido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
