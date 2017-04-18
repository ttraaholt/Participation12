package css.cis3334.firebaseauthentication;

/**
 * This class is used for connecting to the Firebase application.
 *
 * @created Built by Tom Gibbons
 * @author  Tommy Traaholt
 * @date   4/17/2017
 */

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity  {

    private TextView textViewStatus;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoogleLogin;
    private Button buttonCreateLogin;
    private Button buttonSignOut;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_SIGN_IN_FLAG = 9001;

    //On create method called when activity starts.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Widgets are tied to the variables declared above.
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGoogleLogin = (Button) findViewById(R.id.buttonGoogleLogin);
        buttonCreateLogin = (Button) findViewById(R.id.buttonCreateLogin);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);

        //onClick listener added to buttonLogin that contains log message when it is clicked, and uses the sign in method
        //gets the editTextEmail and converts it to a string, and gets the editTextPassword and converts it to a string.
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.d("CIS3334", "normal login ");
                signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        //onClick listener added to buttonCreateLogin that contains log message when it is clicked, and uses createAccount method
        //gets the editTextEmail and converts it to a string, and gets the editTextPassword and converts it to a string.
        buttonCreateLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.d("CIS3334", "Create Account ");
                createAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        //Logs when the google login button is clicked, but the googleSign() method is never used because it is empty.
        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.d("CIS3334", "Google login ");
                googleSignIn();
            }
        });

        //on click listener used for sign out button. Logs a message and calls the signOut() method.
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.d("CIS3334", "Logging out - signOut ");
                signOut();
            }
        });

        //gets the instance from the Firebase authentication.
        mAuth = FirebaseAuth.getInstance();

        //used to track the user and checks if they have signed out or not.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    user.getUid();
                    //Log.d("CIS3334", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d("CIS3334", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }

    //onStart() method that adds authStateListener.
    @Override
    public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
    }

    //onStop method that checks if the mAuthListener is not null, then removeAuthStateListener.
    //Called when the activity is no longer visible to the user.
    @Override
    public void onStop() {
            super.onStop();
        //checks if the mAuthListener is not null. If it isn't, then perform the removeAuthStateListener.
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
    }


    //createAccount() method that uses the email and password variables.
    //It creates a user with the email and password after validating them and uses the createUserWithEmailAndPassword to create the user
    private void createAccount(String email, String password) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //logged and displays the task is successful.
                           // Log.d("CIS3334", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                textViewStatus.setText("Authentication failed.");
                            }

                            // ...
                        }
                    });
    }

    //signIn() method that also uses the email and password variables. Similar to the createAccount() method
    //validates the email and password and signs the user in.
    private void signIn(String email, String password){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Logs if the task is successful.
                           // Log.d("CIS3334", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                               // Log.w("CIS3334", "signInWithEmail", task.getException());
                                textViewStatus.setText("Authentication failed");
                            }

                            // ...
                        }
                    });
    }
    //signOut() method that signs out the user.
    private void signOut () {
        mAuth.signOut();

    }

    private void googleSignIn() {

    }




}
