package com.a10e15.mspo.speeddateing;

/**
 * Created by justking100 on 2/8/2018.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    public static final  int REQUEST_GET_EXTRA_INFO = 0;
    public static final String ARG_EMAIL = "email_final";
    public static final String ARG_NAME = "name_final";
    public static final String ARG_UID = "uid_final";
    EditText _nameText;
    EditText _emailText;
    EditText _passwordText;
    Button _signupButton;
   TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sighup);
        _emailText = findViewById(R.id.input_email);
        _nameText = findViewById(R.id.input_name);
        _passwordText = findViewById(R.id.input_password);
        _signupButton = findViewById(R.id.btn_signup);
        _loginLink = findViewById(R.id.link_login);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("");
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String  name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();


        SplashAct.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");


                    onSignupSuccess(FirebaseAuth.getInstance().getCurrentUser());
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    if (task.getException()!=null)
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        _passwordText.setError(getString(R.string.error_weak_password));
                        _passwordText.requestFocus();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        _emailText.setError(getString(R.string.error_invalid_email));
                        _emailText.requestFocus();
                    } catch(FirebaseAuthUserCollisionException e) {
                        _emailText.setError(getString(R.string.error_user_exists));
                        _emailText.requestFocus();
                    } catch(Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    onSignupFailed("");
                }


            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_GET_EXTRA_INFO:
                setResult(RESULT_OK, null);
                finish();
                break;

        }
    }

    public void onSignupSuccess(FirebaseUser user) {
        _signupButton.setEnabled(true);
        Bundle bd = new Bundle();
        bd.putString(SigupInfo.ARG_EMAIL,_emailText.getText().toString());
        bd.putString(SigupInfo.ARG_NAME, _nameText.getText().toString());
        bd.putString(SigupInfo.ARG_UID, user.getUid());
        Intent i = new Intent(this, SigupInfo.class);
        i.putExtra(SigupInfo.ARG_SIGNHUP, bd);

        startActivityForResult(i, REQUEST_GET_EXTRA_INFO);

    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}