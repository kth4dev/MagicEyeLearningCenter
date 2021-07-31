package com.magiceye.admin.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import com.magiceye.admin.helper.CollectionName;
import com.magiceye.admin.helper.FireStore;
import com.magiceye.admin.helper.MSharePreference;
import com.magiceye.admin.MainActivity;
import com.magiceye.admin.R;
import com.magiceye.admin.model.Admin;


public class Login extends AppCompatActivity {
    EditText editTextPhoneNumber;
    Button btn_login;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        btn_login=findViewById(R.id.login);
        if(user !=null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        editTextPhoneNumber= findViewById(R.id.editTextPhoneNumber);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actionLogin(view);
            }
        });
    }

    public void actionLogin(View view) {
        ProgressDialog progressDialog =new  ProgressDialog(view.getContext());
        progressDialog.setTitle("Log In");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        btn_login.setEnabled(false);
        editTextPhoneNumber.setEnabled(false);


        String phoneNo = editTextPhoneNumber.getText().toString();
        FireStore.INSTANCE.instance().collection(CollectionName.admin)
                .whereEqualTo("phone", phoneNo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size()!=0){
                                Admin admin =task.getResult().getDocuments().get(0).toObject(Admin.class);
                                admin.setId(task.getResult().getDocuments().get(0).getId());
                                MSharePreference.saveString(Login.this,CollectionName.sId,admin.getId());
                                MSharePreference.saveString(Login.this,CollectionName.sName,admin.getName());
                                MSharePreference.saveString(Login.this,CollectionName.sPhone,admin.getPhone());
                                MSharePreference.setAdmin(Login.this,admin.getAdmin());
                                progressDialog.dismiss();
                                Intent phoneAuthActivity = new Intent(Login.this,PhoneAuthActivity.class);
                                phoneAuthActivity.putExtra("phone_no",phoneNo);
                                startActivity(phoneAuthActivity);
                                finish();

                            }else{
                                Toast.makeText(Login.this,"not found!",Toast.LENGTH_LONG).show();
                                btn_login.setEnabled(true);
                                editTextPhoneNumber.setEnabled(true);
                                progressDialog.dismiss();
                            }

                        } else {
                            Toast.makeText(Login.this,"No Internet Connection!",Toast.LENGTH_LONG).show();
                            btn_login.setEnabled(true);
                            editTextPhoneNumber.setEnabled(true);
                            progressDialog.dismiss();
                        }
                    }
                });

    }
}
