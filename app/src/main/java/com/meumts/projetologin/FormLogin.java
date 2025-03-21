package com.meumts.projetologin;


import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import android.os.Build;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executors;


public class FormLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Altera a cor da status bar para a cor definida em colors.xml
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        // Referências diretas para os EditText
        EditText loginEdit = findViewById(R.id.ConteinerItensLogin);
        EditText senhaEdit = findViewById(R.id.ConteinerItensSenha);
        AppCompatButton loginButton = findViewById(R.id.btn2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Captura dos textos digitados
                String login = loginEdit.getText().toString();
                String senha = senhaEdit.getText().toString();

                // Verificação simples
                if (login.equals("adm") && senha.equals("adm")) {
                    Intent intent = new Intent(FormLogin.this, FormHome.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(FormLogin.this, "Login ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });





        mAuth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(this);

        Button btnGoogleSignIn = findViewById(R.id.btn1);
        btnGoogleSignIn.setOnClickListener(view -> signInWithGoogle());

        btnGoogleSignIn.setOnClickListener(view -> {
            Log.d(TAG, "Botão de login do Google foi clicado.");
            signInWithGoogle();
        });
    }

    private void signInWithGoogle() {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.web_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                FormLogin.this,  // Certifique-se de usar o contexto da Activity
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        // Processamento do resultado
                        handleSignIn(result.getCredential());
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        // Tratamento de erro
                        Log.e(TAG, "Couldn't retrieve user's credentials: " + e.getLocalizedMessage());
                    }
                }
        );










    }

    private void handleSignIn(Credential credential) {
        if (credential instanceof CustomCredential && "google.com".equals(credential.getType())) {
            // Recuperando o GoogleIdToken
            Bundle credentialData = ((CustomCredential) credential).getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        } else {
            Log.w(TAG, "Credencial inválida ou não reconhecida.");
            Toast.makeText(this, "Credencial inválida. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Login bem-sucedido: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Falha no login!", Toast.LENGTH_SHORT).show();
                    }
                });
    }




}