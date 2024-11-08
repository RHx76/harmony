package com.example.demo;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
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
    private ListView<String> myListView;

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
        if(timer!=null)
            timer.cancel();
    }

    @FXML
    private void openPlaylistCreationScene() {
        try {
            Stage mainStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("PlaylistCreator.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            mainStage.setScene(scene);
            mainStage.setTitle("New Playlist");
            Image icon=new Image("bully.jpg");
            mainStage.getIcons().add(icon);
            mainStage.show();
            mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    Platform.exit();
                    System.exit(0);
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
                myListView.getItems().add(file.getName());
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
        myListView.setStyle("-fx-accent: #ffdd00; -fx-background-color:#222222 !important;");
        myListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setStyle(""); // Reset style if the item is empty
                        } else {
                            setText(item);
                            setTextFill(Color.rgb( 255, 195, 0 ));  // Text color
                            setStyle("-fx-background-color: #222222;");
                        }
                    }
                };
            }
        });

        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                songNumber=myListView.getSelectionModel().getSelectedIndex();
                mediaPlayer.stop();
                media = new Media(songs.get(songNumber).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                songLabel.setText(songs.get(songNumber).getName());
                if(running)
                    timer.cancel();
            }
        });
    }

}