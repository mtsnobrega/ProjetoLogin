package com.meumts.projetologin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import android.os.Build;

public class FormLogin extends AppCompatActivity {




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
}
}