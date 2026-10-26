package com.example.listaandres;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //1. Los datos: al menos 8 elementos
        ArrayList<Motos> motos = new ArrayList<>();
        motos.add(new Motos("Voge", "DS650X 2021", R.drawable.voge));
        motos.add(new Motos("Honda", "CBR1000R 2006", R.drawable.cbr1000));
        motos.add(new Motos("Kawasaki", "VULCAN S 2020", R.drawable.vulcan));
        motos.add(new Motos("Yamaha", "SR250 SPECIAL 1987", R.drawable.sr));
        motos.add(new Motos("KTM", "MOTARD 990 2020", R.drawable.motard));
        motos.add(new Motos("Kawasaki", "NINJA 400", R.drawable.ninja));
        motos.add(new Motos("Dervi", "VARIANT SPORT 2015", R.drawable.dervi));
        motos.add(new Motos("Suzuki", "Hayabusa 1300 2001", R.drawable.hayabusa));

        // 2. La lista: cómo se colocan las filas y quién las pinta
        RecyclerView rvLista = findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        rvLista.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvLista.setAdapter(new AdaptadorMotos(motos) {
            @Override
            public void onBindViewHolder(AdaptadorMotos.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                Motos moto = motos.get(position);
                // Al tocar la fila, un Toast con su título
                holder.itemView.setOnClickListener(v ->
                        Toast.makeText(v.getContext(), moto.getMarca(),
                                Toast.LENGTH_SHORT).show());
            }
        });
    }
}
