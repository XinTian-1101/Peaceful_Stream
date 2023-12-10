package com.example.assignments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import com.melnykov.fab.FloatingActionButton;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class PlayBackFragment extends DialogFragment {


    private RecordingItem item;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;

    private boolean isPlaying = false;

    long minutes=0;
    long seconds=0;

    CardView cardView ;
    TextView fileName;
    SeekBar progressRecord;
    TextView currentProgressText;
    FloatingActionButton playBtn;
    TextView fileLength_text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        item = (RecordingItem) getArguments().getSerializable("item");

        minutes= TimeUnit.MILLISECONDS.toMinutes(item.getLength());
        seconds= TimeUnit.MILLISECONDS.toSeconds(item.getLength())- TimeUnit.MINUTES.toSeconds(minutes);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_playback, null);
        fileName = view.findViewById(R.id.filename_name_text_view);
        progressRecord = view.findViewById(R.id.seekbar);
        currentProgressText = view.findViewById(R.id.current_progress_text_view);
        playBtn = view.findViewById(R.id.fab_play);
        fileLength_text = view.findViewById(R.id.file_length_text_view);


        setSeekBarValues();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    onPlay(isPlaying);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isPlaying=!isPlaying;

            }
        });

        fileName.setText(item.getName());
        fileLength_text.setText(String.format("%02d:%02d",minutes,seconds));

        builder.setView(view);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return builder.create();

    }


    private void onPlay(boolean isPlaying) throws IOException {

        if(!isPlaying){

            if(mediaPlayer==null){
                startPlaying();
            }
            else{
                pausePlaying();
            }

        }

    }

    private void pausePlaying() {

        playBtn.setImageResource(R.drawable.ic_media_play);
        handler.removeCallbacks(mRunnable);
        mediaPlayer.pause();

    }

    private void startPlaying() throws IOException {


        playBtn.setImageResource(R.drawable.ic_media_pause);
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setDataSource(item.getPath());
        mediaPlayer.prepare();
        progressRecord.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaying();
            }
        });

        updateProgressBar();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    private void setSeekBarValues(){

        ColorFilter colorFilter = new LightingColorFilter(getResources().getColor(R.color.color_primary),getResources().getColor(R.color.color_primary));

        progressRecord.getProgressDrawable().setColorFilter(colorFilter);
        progressRecord.getThumb().setColorFilter(colorFilter);



        progressRecord.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(mediaPlayer!=null && fromUser){

                    mediaPlayer.seekTo(progress);
                    handler.removeCallbacks(mRunnable);

                    long minutes= TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition())-TimeUnit.MINUTES.toSeconds(minutes);

                     currentProgressText.setText(String.format("%02d:%02d",minutes,seconds));

                     updateProgressBar();

                } else if (mediaPlayer==null&&fromUser) {

                    try {
                        prepareMediaFromPoint(progress);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updateProgressBar();

                }


            }

            private void prepareMediaFromPoint(int progress) throws IOException {

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(item.getPath());
                mediaPlayer.prepare();
                progressRecord.setMax(mediaPlayer.getDuration());
                mediaPlayer.seekTo(progress);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlaying();
                    }


                });

            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }


    private void stopPlaying() {

        playBtn.setImageResource(R.drawable.ic_media_play);
        handler.removeCallbacks(mRunnable);
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();

        progressRecord.setProgress(progressRecord.getMax());
        isPlaying = !isPlaying;

        currentProgressText.setText(fileLength_text.getText());
        progressRecord.setProgress(progressRecord.getMax());

    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            if(mediaPlayer!=null){

                int currentPosition = mediaPlayer.getCurrentPosition();
                progressRecord.setProgress(currentPosition);

                long minutes= TimeUnit.MILLISECONDS.toMinutes(currentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(currentPosition)-TimeUnit.MINUTES.toSeconds(minutes);

                currentProgressText.setText(String.format("%02d:%02d",minutes,seconds));
                updateProgressBar();
            }

        }
    };


    private void updateProgressBar(){

        handler.postDelayed(mRunnable,1000);

    }


}
