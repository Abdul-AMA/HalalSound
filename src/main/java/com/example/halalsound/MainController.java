package com.example.halalsound;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable{

    public File file;
    public Media media;
    public MediaPlayer mediaPlayer;
    public MediaView mediaView;
    public Slider sliderTime;
    public Slider sliderVolume;
    public Button buttonBack;
    public Button buttonPlay;
    public Button buttonNext;

    public void onBack(ActionEvent actionEvent) {
    }

    public void onPlay(ActionEvent actionEvent) {
        file = new File("src\\main\\resources\\com\\example\\halalsound\\video.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }

    public void onNext(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
