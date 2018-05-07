package com.lianurfadilah46.apps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lianurfadilah46.apps.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewAccount extends AppCompatActivity {
    EditText txtNama, txtUser, txtPassword,txtRepass;
    RadioButton  btnDokter, btnApoteker;
    Button btnSignIn, btnSave;
    Intent intent;
    ProgressDialog pDialog;
   /* private RadioGroup radioGroup;
    private RadioButton jRad;
    private RadioGroup radioGroup1;
    private RadioButton jAk;*/
//    String akses;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL + "register.php";
    private static final String TAG = NewAccount.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        //txtNama = (EditText) findViewById(R.id.txtNama);
        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRepass = (EditText) findViewById(R.id.txtRepass);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSignIn = (Button) findViewById(R.id.btnBatal);


        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                intent = new Intent(NewAccount.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        /*final RadioButton RadioL = findViewById(R.id.btnL);
        final RadioButton RadioP = findViewById(R.id.btnP);*/
        final RadioButton RadioApotek = findViewById(R.id.btnLk);
        final RadioButton RadioDokter = findViewById(R.id.btnPr);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUser.getText().toString().trim().equals("")||
                        txtPassword.getText().toString().trim().equals("")||
                        txtRepass.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Tidak boleh kosong", Toast.LENGTH_LONG).show();
                }else{
                    String username = txtUser.getText().toString();
                    String password = txtPassword.getText().toString();
                    String repassword = txtRepass.getText().toString();


                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkNewAccount(username, password, repassword);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkNewAccount(final String username, final String password, final String repassword) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Register ...");
        showDialog();

        StringRequest strReq = new StringRequest (Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response: " + response.toString());
                hideDialog();

                intent = new Intent(NewAccount.this, MainActivity.class);
                finish();
                startActivity(intent);

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        Log.e("Successfully Register!", jObj.toString());
                        Toast.makeText(getApplicationContext(),
                                "SUKSES", Toast.LENGTH_LONG).show();

                        txtUser.setText("");
                        txtPassword.setText("");
                        txtRepass.setText("");




                    } else {
                        Toast.makeText(getApplicationContext(),
                                "GAGAL", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Unreach", Toast.LENGTH_LONG).show();
                hideDialog();
            }
            }){

                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("password", password);
                  params.put("repassword", repassword);
//                   // params.put("nama", nama);
//                    params.put("akses", akses);
                    return params;
                }
            };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
        }

            private void hideDialog() {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            private void showDialog() {
                if (!pDialog.isShowing())
                    pDialog.show();
            }
}