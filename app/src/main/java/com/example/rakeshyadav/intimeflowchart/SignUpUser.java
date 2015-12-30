package com.example.rakeshyadav.intimeflowchart;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPut;
import java.io.IOException;


public class SignUpUser extends Activity {
    private EditText firstName,lastName,userName,password;
    private Button signup;
    String f_name = new String();
    String l_name="",u_name="",u_pass="",user="";
    HttpClient httpclient = new DefaultHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        firstName = (EditText) findViewById(R.id.fname);
        lastName = (EditText) findViewById(R.id.lname);
        userName = (EditText) findViewById(R.id.uname);
        password = (EditText) findViewById(R.id.pass);
        signup = (Button) findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                f_name = firstName.getText().toString();
                l_name = lastName.getText().toString();
                u_name = userName.getText().toString();
                u_pass = password.getText().toString();
                user = u_name+"_"+u_pass;

                if ((!u_name.equals("")) && (!u_pass.equals(""))) {
                    new PostTask().execute(user);
                } else if ((u_name.equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "User Name field empty", Toast.LENGTH_SHORT).show();
                } else if ((u_pass.equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "User Name and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class PostTask extends AsyncTask<String,Void,Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            String valueIWantToSend = params[0];
            HttpPut httppost = new HttpPut("http://192.168.4.87:8093/UserModule/webapi/myresource/"+valueIWantToSend);
            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void unused) {
            Toast.makeText(getApplicationContext(), "successfully save", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(SignUpUser.this, LoginUser.class);
            startActivity(myIntent);
            finish();
        }
    }
}
