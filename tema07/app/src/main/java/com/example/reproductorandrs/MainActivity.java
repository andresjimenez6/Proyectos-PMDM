package com.example.reproductorandrs;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private SoundPool soundPool;
    private int idGoteo;
    private int idTormenta;
    private int idSirena;
    private int idAgua;
    private int efectosCargados = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private SeekBar seekBar;
    private TextView tvTiempoActual;
    private TextView tvDuracionTotal;
    private final Runnable actualizarProgreso = new Runnable() {
        @Override
        public void run() {
            if (player != null && player.isPlaying()) {
                long posicion = player.getCurrentPosition();
                seekBar.setProgress((int) posicion);
                tvTiempoActual.setText(formatearTiempo(posicion));
            }
            handler.postDelayed(this, 500);
        }
    };

    private String formatearTiempo(long ms) {
        long segundos = ms / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d", segundos / 60, segundos % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        player = new ExoPlayer.Builder(this).build();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cancion);
        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();

        seekBar = findViewById(R.id.seekBar);
        tvTiempoActual = findViewById(R.id.tvTiempoActual);
        tvDuracionTotal = findViewById(R.id.tvDuracionTotal);
        ImageButton btnPlayPausa = findViewById(R.id.btnPlayPausa);
        ImageButton btnSkipBack = findViewById(R.id.btnSkipBack);
        ImageButton btnSkipForward = findViewById(R.id.btnSkipForward);

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    long duracion = player.getDuration();
                    seekBar.setMax((int) duracion);
                    tvDuracionTotal.setText(formatearTiempo(duracion));
                } else if (playbackState == Player.STATE_ENDED) {
                    btnPlayPausa.setImageResource(R.drawable.ic_play);
                    seekBar.setProgress(seekBar.getMax());
                    tvTiempoActual.setText(tvDuracionTotal.getText());
                }
            }
        });

        btnPlayPausa.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
                btnPlayPausa.setImageResource(R.drawable.ic_play);
            } else {
                if (player.getPlaybackState() == ExoPlayer.STATE_ENDED) {
                    player.seekTo(0);
                }
                player.play();
                btnPlayPausa.setImageResource(R.drawable.ic_pause);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                if (fromUser) {
                    tvTiempoActual.setText(formatearTiempo(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {
                player.seekTo(bar.getProgress());
            }
        });

        btnSkipBack.setOnClickListener(v -> player.seekTo(0));
        btnSkipForward.setOnClickListener(v -> player.seekTo(0));

        Button btnGoteo = findViewById(R.id.btnGoteo);
        Button btnTormenta = findViewById(R.id.btnTormenta);
        Button btnSirena = findViewById(R.id.btnSirena);
        Button btnAgua = findViewById(R.id.btnAgua);

        AudioAttributes atributos = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(atributos)
                .build();

        idGoteo = soundPool.load(this, R.raw.efecto_goteo, 1);
        idTormenta = soundPool.load(this, R.raw.efecto_tormenta, 1);
        idSirena = soundPool.load(this, R.raw.efecto_sirena, 1);
        idAgua = soundPool.load(this, R.raw.efecto_agua, 1);

        soundPool.setOnLoadCompleteListener((pool, sampleId, status) -> {
            if (status == 0) {
                efectosCargados++;
                if (efectosCargados == 4) {
                    btnGoteo.setEnabled(true);
                    btnTormenta.setEnabled(true);
                    btnSirena.setEnabled(true);
                    btnAgua.setEnabled(true);
                }
            }
        });

        btnGoteo.setOnClickListener(v -> soundPool.play(idGoteo, 1f, 1f, 1, 0, 1f));
        btnTormenta.setOnClickListener(v -> soundPool.play(idTormenta, 1f, 1f, 1, 0, 1f));
        btnSirena.setOnClickListener(v -> soundPool.play(idSirena, 1f, 1f, 1, 0, 1f));
        btnAgua.setOnClickListener(v -> soundPool.play(idAgua, 1f, 1f, 1, 0, 1f));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        handler.post(actualizarProgreso);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(actualizarProgreso);
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
        if (soundPool != null) {
            soundPool.release();
        }
    }

}