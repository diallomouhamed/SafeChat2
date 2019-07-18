package com.diallo.safechat;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 1;
    public static String EMAIL = "adresse.user@gmail.com";
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton button;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //cacher les bar de l'app
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // cacher la navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        //changer la couleur de la navigation bar
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGoogleSingIn();
    }

    private void initGoogleSingIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        button = findViewById(R.id.sign_in_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });
    }

    public void connect() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null) {
            Intent fragmentActivity = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(fragmentActivity);
        } else {
            forceSignIn();
        }
    }

    private void forceSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                String a = task.getResult(ApiException.class).getEmail();
                System.out.println(a);
            } catch (ApiException e) {
                finish();
            }

            final FirebaseFirestore database = FirebaseFirestore.getInstance();

            DocumentReference docRef = database.collection("User").document(EMAIL);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (!document.exists()) {

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            CollectionReference users = db.collection("User");

                            Map<String, String> user = new HashMap<>();
                            user.put("pseudoUser", EMAIL.substring(0, EMAIL.length() - 10));
                            user.put("emailUser", EMAIL);

                            users.document(EMAIL).set(user);

                            HashMap<String, Object> contact = new HashMap<>();
                            contact.put("emailUser", EMAIL);
                            contact.put("emailContact", "adresse.test1@gmail.com");
                            db.collection("Contact").add(contact);

                            contact = new HashMap<>();
                            contact.put("emailUser", EMAIL);
                            contact.put("emailContact", "adresse.test2@gmail.com");
                            db.collection("Contact").add(contact);

                            contact = new HashMap<>();
                            contact.put("emailUser", EMAIL);
                            contact.put("emailContact", "adresse.test3@gmail.com");
                            db.collection("Contact").add(contact);

                            contact = new HashMap<>();
                            contact.put("emailUser", EMAIL);
                            contact.put("emailContact", "adresse.test4@gmail.com");
                            db.collection("Contact").add(contact);

                            contact = new HashMap<>();
                            contact.put("emailUser", EMAIL);
                            contact.put("emailContact", "adresse.test5@gmail.com");
                            db.collection("Contact").add(contact);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            readContact();

            Intent fragmentActivity = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(fragmentActivity);
        }
    }

    public void readContact() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("Contact")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, String> element;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                element = new HashMap<>();

                                String emailUser = document.getData().get("emailUser").toString();
                                String emailContact = document.getData().get("emailContact").toString();

                                if(emailUser.equals(EMAIL)) {
                                    element.put("emailContact", emailContact);
                                    ContactFragment.listContact.add(element);
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
