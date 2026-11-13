package com.example.reproductorandrs;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        player = new ExoPlayer.Builder(this).build();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cancion);
        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();

        ImageButton btnPlayPausa = findViewById(R.id.btnPlayPausa);
        ImageButton btnSkipBack = findViewById(R.id.btnSkipBack);
        ImageButton btnSkipForward = findViewById(R.id.btnSkipForward);

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    btnPlayPausa.setImageResource(R.drawable.ic_play);
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

        btnSkipBack.setOnClickListener(v -> player.seekTo(0));
        btnSkipForward.setOnClickListener(v -> player.seekTo(0));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}