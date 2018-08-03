package gitmad.gitmadheatmap;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText passwordEntry;
    private EditText emailEntry;
    private EditText fNameEntry;
    private EditText mNameEntry;
    private EditText lNameEntry;
    private String email;
    private String password;

    // Firebase
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerButton = findViewById(R.id.register_btn_create_account);
        passwordEntry = findViewById(R.id.register_editText_password);
        emailEntry = findViewById(R.id.register_editText_email);
        fNameEntry = findViewById(R.id.register_editText_first_name);
        lNameEntry = findViewById(R.id.register_editText_last_name);

        Intent intent = getIntent();
        email = intent.getStringExtra("enteredUsername");
        password = intent.getStringExtra("enteredPassword");
        emailEntry.setText(email);
        passwordEntry.setText(password);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    public void registerUser() {
        final String email = emailEntry.getText().toString();
        final String password = passwordEntry.getText().toString();
        final String firstName = fNameEntry.getText().toString();
        final String lastName = fNameEntry.getText().toString();

        if( !areCredentialsValid() ) {
            return;
        }

        FbAuth mAuth = new FbAuth();
        mAuth.createNewUser( new User( firstName, lastName, email), password);

        Intent intent = new Intent( this, EnterActivity.class );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity( intent );
    }

    private boolean areCredentialsValid() {
        // Store values at the time of the login attempt.
        String email = emailEntry.getText().toString();
        String password = passwordEntry.getText().toString();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            notifyUserError( "INVALID_PASSWORD" );

            return false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            notifyUserError("EMPTY_EMAIL");

            return false;
        } else if (!isEmailValid(email)) {
            notifyUserError("INVALID_EMAIL");

            return false;
        }

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

        return true;
    }

    private void notifyUserError( String error ) {
        switch( error) {
            case "INVALID_PASSWORD":
                Toast.makeText( RegistrationActivity.this, R.string.reg_invalid_password, Toast.LENGTH_LONG ).show();
                break;
            case "EMPTY_EMAIL":
                Toast.makeText( RegistrationActivity.this, R.string.reg_invalid_email, Toast.LENGTH_LONG ).show();
                break;
            case "INVALID_EMAIL":
                Toast.makeText( RegistrationActivity.this, R.string.reg_empty_email, Toast.LENGTH_LONG ).show();
                break;
        }
    }

    private boolean isPasswordValid( String password ) {
        return password.length() > 5;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

}
