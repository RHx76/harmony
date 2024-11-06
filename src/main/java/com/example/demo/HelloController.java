package com.example.demo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController implements Initializable {
    @FXML
    public AnchorPane pane;
    @FXML
    public Label songLabel;
    @FXML
    public Button playButton,pauseButton,resetButton,previousButton,nextButton;
    @FXML
    public ProgressBar songProgressBar;
    @FXML
    public ComboBox<String> speedComboBox;
    @FXML
    public Slider volumeMedia;

    private File directory;

    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;

    private int[] speeds={25,50,75,100,125,150,175,200};

    private Timer timer;

    private TimerTask task;

    private boolean running;

    private Media media;

    private MediaPlayer mediaPlayer;

    @FXML
    public void playMedia()
    {
        beginTimer();
        mediaPlayer.setVolume(volumeMedia.getValue()*0.01);
        mediaPlayer.play();
    }

    @FXML
    public void pauseMedia()
    {
        cancelTime();
        changeSpeed(null);
        mediaPlayer.pause();
    }

    @FXML
    public void resetMedia(){
        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    @FXML
    public void previousMedia()
    {
        if(songNumber>0)
            songNumber--;
        else
            songNumber=songs.size()-1;

        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
        if(running)
            timer.cancel();
    }

    @FXML
    public void nextMedia()
    {
        if(songNumber<songs.size()-1)
            songNumber++;
        else
            songNumber=0;

        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
        if(running)
            timer.cancel();
    }

    @FXML
    public void onVolumeChange()
    {}

    public void changeSpeed(javafx.event.ActionEvent actionEvent) {
        String speed=speedComboBox.getValue();
        if(speed==null)
        {
            mediaPlayer.setRate(1);
            return;
        }
        speed=speed.substring(0,speed.length()-1);
        mediaPlayer.setRate(Integer.parseInt(speed)*0.01);
    }

    @FXML
    public void beginTimer()
    {
        timer=new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                running=true;
                double current=mediaPlayer.getCurrentTime().toSeconds();
                double end=mediaPlayer.getTotalDuration().toSeconds();
                System.out.println(current/end);
                songProgressBar.setProgress(current/end);

                if(current/end==1)
                {
                    cancelTime();
                }
            }
        };

        timer.scheduleAtFixedRate(task,0,1000);
    }

    @FXML
    public void cancelTime()
    {
        running=false;
        timer.cancel();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs=new ArrayList<File>();
        directory=new File("music");
        files = directory.listFiles();
//        System.out.println("hey!"+directory.getAbsolutePath());

        if(files!=null)
        {
            for(File file:files)
            {
                songs.add(file);
                System.out.println(file);
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());
        }

        for(int i=0;i<speeds.length;i++)
        {
            speedComboBox.getItems().add(Integer.toString(speeds[i])+"%");
        }

        speedComboBox.setOnAction(this::changeSpeed);
        volumeMedia.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeMedia.getValue()*0.01);
            }
        });
        songProgressBar.setStyle("-fx-accent: #ffdd00;");
    }

}