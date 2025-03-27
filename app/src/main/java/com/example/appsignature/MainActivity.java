package com.example.appsignature;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.cardview.widget.CardView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SignatureView signatureView;
    private EditText etDescription;
    private Button btnSave, btnClear;
    private RecyclerView rvSignatures;
    private DatabaseHelper dbHelper;
    private SignatureAdapter signatureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        signatureView = findViewById(R.id.signatureView);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        rvSignatures = findViewById(R.id.rvSignatures);

        // Inicializar base de datos
        dbHelper = new DatabaseHelper(this);

        // Configurar RecyclerView
        rvSignatures.setLayoutManager(new LinearLayoutManager(this));
        loadSignatures();

        // Botón para guardar firma
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSignature();
            }
        });

        // Botón para limpiar firma
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clear();
                etDescription.setText("");
            }
        });
    }

    private void saveSignature() {
        try {
            String description = etDescription.getText().toString().trim();

            if (description.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese una descripción", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap signatureBitmap = signatureView.getSignatureBitmap();
            if (signatureBitmap == null) {
                Toast.makeText(this, "No hay firma para guardar", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertir Bitmap a byte[]
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Crear objeto Signature
            Signature signature = new Signature(description, byteArray);

            // Guardar en base de datos
            long result = dbHelper.insertSignature(signature);

            if (result != -1) {
                Toast.makeText(this, "Firma guardada exitosamente", Toast.LENGTH_SHORT).show();
                loadSignatures();
                signatureView.clear();
                etDescription.setText("");
            } else {
                Toast.makeText(this, "Error al guardar la firma", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Registrar el error completo
            Log.e("SaveSignature", "Error al guardar la firma", e);
            Toast.makeText(this, "Error inesperado: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSignatures() {
        List<Signature> signatures = dbHelper.getAllSignatures();
        signatureAdapter = new SignatureAdapter(signatures);
        rvSignatures.setAdapter(signatureAdapter);
    }
}