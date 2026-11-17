package com.example.firebaselearing;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseLearningHub";

    // Firebase Services Declarations
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseFirestore mFirestore;

    // UI Elements for 6 Sections
    private TextView tvStatus;
    private Button btnAuthSignIn;
    private EditText etDataInput;
    private Button btnSaveRealtime;
    private Button btnReadRealtime;
    private Button btnSaveFirestore;
    private Button btnReadFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Initialize Firebase App if not already initialized
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        // 2. Initialize Firebase Services
        mAuth = FirebaseAuth.getInstance();

        String dbUrl = "https://fir-learningproject-7d503-default-rtdb.firebaseio.com";
        try {
            mDatabase = FirebaseDatabase.getInstance(dbUrl);
        } catch (Exception e) {
            mDatabase = FirebaseDatabase.getInstance();
        }
        mDatabase.goOnline();
        mRef = mDatabase.getReference("learning_data");

        mFirestore = FirebaseFirestore.getInstance();

        // 3. Bind UI Views
        tvStatus = findViewById(R.id.tvStatus);
        btnAuthSignIn = findViewById(R.id.btnAuthSignIn);
        etDataInput = findViewById(R.id.etDataInput);
        btnSaveRealtime = findViewById(R.id.btnSaveRealtime);
        btnReadRealtime = findViewById(R.id.btnReadRealtime);
        btnSaveFirestore = findViewById(R.id.btnSaveFirestore);
        btnReadFirestore = findViewById(R.id.btnReadFirestore);

        // Check active session
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            tvStatus.setText("Status: Signed in as UID: " + currentUser.getUid());
        }

        // Section 1: Connection Status Listener
        setupConnectionListener();

        // Sections 2 - 6: Event Listeners
        setupListeners();
    }

    /**
     * Section 1: Check & Listen for Firebase Connection Status
     */
    private void setupConnectionListener() {
        DatabaseReference connectedRef = mDatabase.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean connected = snapshot.getValue(Boolean.class);
                if (Boolean.TRUE.equals(connected)) {
                    tvStatus.setText("Status: Connected to Firebase Realtime Database");
                    Log.d(TAG, "Realtime DB connected");
                } else {
                    tvStatus.setText("Status: Disconnected / Initialized locally");
                    Log.d(TAG, "Realtime DB disconnected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvStatus.setText("Status Error: " + error.getMessage());
            }
        });
    }

    private void setupListeners() {
        // Section 2: Firebase Authentication (Anonymous Sign-In)
        btnAuthSignIn.setOnClickListener(v -> {
            tvStatus.setText("Auth Status: Signing in anonymously...");
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user != null ? user.getUid() : "Unknown";
                            tvStatus.setText("Auth Status: Signed in anonymously as " + uid);
                            Toast.makeText(MainActivity.this, "Auth Success!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Anonymous auth success: " + uid);
                        } else {
                            Exception e = task.getException();
                            String msg = e != null ? e.getMessage() : "Auth Failed";
                            Log.e(TAG, "Anonymous auth failed", e);
                            if (msg != null && msg.contains("CONFIGURATION_NOT_FOUND")) {
                                tvStatus.setText("Auth Error: [CONFIGURATION_NOT_FOUND]\n1. Verify package com.rajveer.firebasestudy is registered in Firebase Console fir-learningproject-7d503\n2. Re-download google-services.json to app/\n3. Enable Anonymous Sign-in under Authentication -> Sign-in method");
                            } else {
                                tvStatus.setText("Auth Error: " + msg);
                            }
                        }
                    });
        });

        // Section 3: Realtime Database Write Operation
        btnSaveRealtime.setOnClickListener(v -> {
            String text = etDataInput.getText() != null ? etDataInput.getText().toString().trim() : "";
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(MainActivity.this, "Please enter data to save", Toast.LENGTH_SHORT).show();
                return;
            }

            mRef.child("message").setValue(text)
                    .addOnSuccessListener(aVoid -> {
                        tvStatus.setText("Realtime DB Write: Saved successfully!");
                        Toast.makeText(MainActivity.this, "Saved to Realtime DB", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> tvStatus.setText("Realtime DB Write Error: " + e.getMessage()));
        });

        // Section 4: Realtime Database Read Operation
        btnReadRealtime.setOnClickListener(v -> {
            mRef.child("message").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String value = snapshot.getValue(String.class);
                        tvStatus.setText("Realtime DB Read Result: " + value);
                    } else {
                        tvStatus.setText("Realtime DB Read: No data found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tvStatus.setText("Realtime DB Read Error: " + error.getMessage());
                }
            });
        });

        // Section 5: Cloud Firestore Write Operation
        btnSaveFirestore.setOnClickListener(v -> {
            String text = etDataInput.getText() != null ? etDataInput.getText().toString().trim() : "";
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(MainActivity.this, "Please enter data to save", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("message", text);
            data.put("timestamp", System.currentTimeMillis());

            mFirestore.collection("learning_hub").document("sample_doc")
                    .set(data)
                    .addOnSuccessListener(aVoid -> {
                        tvStatus.setText("Firestore Write: Saved successfully!");
                        Toast.makeText(MainActivity.this, "Saved to Firestore", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> tvStatus.setText("Firestore Write Error: " + e.getMessage()));
        });

        // Section 6: Cloud Firestore Read Operation
        btnReadFirestore.setOnClickListener(v -> {
            mFirestore.collection("learning_hub").document("sample_doc")
                    .get()
                    .addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                        if (documentSnapshot.exists()) {
                            String message = documentSnapshot.getString("message");
                            tvStatus.setText("Firestore Read Result: " + message);
                        } else {
                            tvStatus.setText("Firestore Read: Document does not exist");
                        }
                    })
                    .addOnFailureListener(e -> tvStatus.setText("Firestore Read Error: " + e.getMessage()));
        });
    }
}