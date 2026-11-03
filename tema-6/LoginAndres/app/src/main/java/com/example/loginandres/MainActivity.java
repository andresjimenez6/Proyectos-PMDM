package com.example.loginandres;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.google.android.material.materialswitch.MaterialSwitch;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String CORREO_VALIDO = "andres@correo.com";
    private static final String CLAVE_VALIDA = "1234";
    private int intentos = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //cambiar el color del switch
        MaterialSwitch switch1 = findViewById(R.id.switch1);

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },
                new int[] {}
        };

        int[] trackColors = new int[] {
                ContextCompat.getColor(this, R.color.tinta_pino),
                Color.parseColor("#E7E0EC")
        };

        int[] thumbColors = new int[] {
                Color.WHITE,                    // activado: blanco
                Color.parseColor("#79747E")     // desactivado: gris
        };

        switch1.setThumbTintList(new ColorStateList(states, thumbColors));

        switch1.setTrackTintList(new ColorStateList(states, trackColors));

        //LEER LO ESCRITO

        EditText editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        EditText editTextTextPassword = findViewById(R.id.editTextTextPassword);
        TextView txtMensaje = findViewById(R.id.txtMensaje);
        Button btnContinuar = findViewById(R.id.btnContinuar);

        if (savedInstanceState != null) {
            intentos = savedInstanceState.getInt("intentos", 3);
        }
        SharedPreferences preferencias = getSharedPreferences("login", MODE_PRIVATE);
        String correoGuardado = preferencias.getString("correo", "");
        if (savedInstanceState == null) {
            editTextTextEmailAddress.setText(correoGuardado);
            switch1.setChecked(!correoGuardado.isEmpty());
        }
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                preferencias.edit().remove("correo").apply();
            }
        });

        btnContinuar.setOnClickListener(v -> {
            String correo = editTextTextEmailAddress.getText().toString().trim();
            String clave = editTextTextPassword.getText().toString();
            editTextTextEmailAddress.setError(null);
            editTextTextPassword.setError(null);
            if (correo.isEmpty()) {
                editTextTextEmailAddress.setError(getString(R.string.falta_correo));
            }
            if (clave.isEmpty()) {
                editTextTextPassword.setError("La contraseña no puede estar vacía");
            }
            if (correo.isEmpty() || clave.isEmpty()) {
                txtMensaje.setText("");
                return;
            }

            if (correo.equals(CORREO_VALIDO) && clave.equals(CLAVE_VALIDA)) {
                intentos = 3;
                if (switch1.isChecked()) {
                    preferencias.edit().putString("correo", correo).apply();
                } else {
                    preferencias.edit().remove("correo").apply();
                }
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtra(MenuActivity.EXTRA_CORREO, correo);
                startActivity(intent);
                finish();
                return;
            }

            intentos--;
            txtMensaje.setText("Usuario y/o contraseña incorrectos. Intentos restantes: " + intentos);
            txtMensaje.setTextColor(ContextCompat.getColor(this, R.color.incorrecto));
            if (intentos <= 0) {
                btnContinuar.setEnabled(false);
                finish();
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("intentos", intentos);
        super.onSaveInstanceState(outState);
    }
}
