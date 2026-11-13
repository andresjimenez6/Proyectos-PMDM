package com.example.reproductorandrs;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    }

}