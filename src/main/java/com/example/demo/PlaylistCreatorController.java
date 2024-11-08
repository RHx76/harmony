package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.io.*;

public class PlaylistCreatorController implements Initializable {

    @FXML
    private Button addTrackButton;

    @FXML
    private Button deleteTrackButton;

    @FXML
    private ListView<String> myListView;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button saveTrackButton;

    @FXML
    private FileChooser fileChooser = new FileChooser();

    @FXML
    private ArrayList<File> playlist;

    @FXML
    private TextField playlistTextfieldName;

    @FXML
    private Label outputLabel;

    @FXML
    void addTrack() {
        File file = fileChooser.showOpenDialog(new Stage());
        playlist.add(file);
        myListView.getItems().add(file.getName());
    }

    @FXML
    void savePlaylist(){
        String val=playlistTextfieldName.getText();
        if(val.isEmpty())
        {
            outputLabel.setStyle("-fx-text-fill: red;");
            outputLabel.setText("Enter a name for your playlist!");
            return;
        }

        val=val+".ser";

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(val))) {
            out.writeObject(playlist);  // Serialize the ArrayList<File> object
            outputLabel.setStyle("-fx-text-fill: green;");
            outputLabel.setText("Playlist saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            outputLabel.setStyle("-fx-text-fill: red;");
            outputLabel.setText("Error while saving playlist!");
            return;
        }
    }

    @FXML
    void deleteTrack(){
        int songNumber=myListView.getSelectionModel().getSelectedIndex();
        playlist.remove(songNumber);
        myListView.getItems().remove(songNumber);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playlist=new ArrayList<>();

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
    }
}
