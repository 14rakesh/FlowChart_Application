package com.example.rakeshyadav.intimeflowchart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginUser extends Activity {
    private EditText userName,password;
    Button signIn,newUser;
    String resp,user;
    String u_name,u_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        userName = (EditText) findViewById(R.id.uname);
        password = (EditText) findViewById(R.id.pass);
        signIn = (Button) findViewById(R.id.login);
        newUser = (Button) findViewById(R.id.newuser);
        newUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginUser.this, SignUpUser.class);
                startActivity(myIntent);
                finish();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                u_name = userName.getText().toString();
                u_pass = password.getText().toString();
                user = u_name+"_"+u_pass;
                if ((!u_name.equals("")) && (!u_pass.equals(""))) {
                    new Login().execute();
                } else if ((!u_name.equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "User Name field empty", Toast.LENGTH_SHORT).show();
                } else if ((!u_pass.equals(""))) {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "User Name and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class Login extends AsyncTask<String, String, String> {
        private ProgressDialog nDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginUser.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }
        @Override
        protected String doInBackground(String... args) {

            String response;

            try {
                response = SimpleHttpClient.executeHttpGet("http://192.168.4.87:8093/UserModule/webapi/myresource/"+user);
                String res = response.toString();
                resp = res.replaceAll("\\s+", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String repo) {

         if(resp.equals("true"))
         {
             nDialog.dismiss();
             startActivity(new Intent(LoginUser.this, ProjectList.class));
          }
            else {
             Toast.makeText(getApplicationContext(), "Wrong User Name and Password", Toast.LENGTH_LONG).show();
             nDialog.dismiss();
         }

        }

    }
}
