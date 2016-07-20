package google.googlesignin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class SignInActivity extends Activity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    TextView tv_username, tv_Email;


    Button signOutbutton;
    SignInButton signInbutton;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    protected void onStart() {
        super.onStart();
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_Email = (TextView) findViewById(R.id.tv_Email);


        //Register both button and add click listener
        signInbutton = (SignInButton) findViewById(R.id.sign_in_button);
        signOutbutton = (Button) findViewById(R.id.btn_logout);
        signInbutton.setOnClickListener(this);
        signOutbutton.setOnClickListener(this);
        signOutbutton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:

                signIn();

                break;
            case R.id.btn_logout:

                signOut();

                break;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Toast.makeText(SignInActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        mGoogleApiClient.connect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        signOutbutton.setVisibility(View.VISIBLE);
        signInbutton.setVisibility(View.GONE);


    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        tv_username.setText("");
                        tv_Email.setText("");

                    }
                });

        signOutbutton.setVisibility(View.GONE);
        signInbutton.setVisibility(View.VISIBLE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            /*Intent i = new Intent(SignInActivity.this, ThirdActivity.class);
            startActivity(i);*/
            tv_username.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            tv_Email.setText(acct.getEmail());


        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }


}
