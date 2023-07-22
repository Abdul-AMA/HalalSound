package com.example.halalsound;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
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
    public Button buttonPause;
    public MenuBar MenuItem;
    public Menu menuFile;
    public Menu menuTools;
    public Menu menuHelp;

    public FileChooser fileChooserSave;
    public ImageView imageView;
    public Label labelTime;
    public Label labelVolume;
    public Label labelSpeed;
    public Label labelTimeMax;
    public MediaView cMedia;

    double rate = 1.0;
    double zoom = 1.0;


    public void onPlay( ) {
        if( mediaPlayer != null && mediaView.isVisible()){
            mediaPlayer.play();
            buttonPlay.setVisible(false);
            buttonPause.setVisible(true);
        }
    }
    public void onPause( ) {
        if( mediaPlayer != null && mediaView.isVisible()){
            mediaPlayer.pause();
            buttonPause.setVisible(false);
            buttonPlay.setVisible(true);
        }
    }

    public void onNext( ) {
        if( mediaPlayer != null && mediaView.isVisible()){
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5)));
        }
    }

    public void onBack( ) {
        if( mediaPlayer != null && mediaView.isVisible()){
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-5)));
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMenus();
        mediaView.setVisible(false);
        fileChooserSave = new FileChooser();
        cMedia.setVisible(false);


    }



    public void loadMenus(){
        //----------------------------------------------------------------
        MenuItem menuFileNew = new MenuItem("New");
        menuFileNew.setOnAction( e-> {
            setToPlay(fileLoader());
            ;});
        menuFile.getItems().add(menuFileNew);
        //----------------------------------------------------------------
        MenuItem menuFileSave = new MenuItem("Save");
        menuFileSave.setOnAction( e-> {
            try {
                saveFile();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuFile.getItems().add(menuFileSave);
        //----------------------------------------------------------------
        MenuItem menuFileClose = new MenuItem("Close");
        menuFileClose.setOnAction( e-> {
            if (mediaView.isVisible()){
                    close();

            }

        });
        menuFile.getItems().add(menuFileClose);
        //----------------------------------------------------------------
        menuFile.getItems().add(new SeparatorMenuItem());
        //----------------------------------------------------------------
        MenuItem menuFileSettings = new MenuItem("Settings");
        menuFileSettings.setOnAction( e-> {
            try {
                onSetting();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuFile.getItems().add(menuFileSettings);
        //----------------------------------------------------------------
        menuFile.getItems().add(new SeparatorMenuItem());
        //----------------------------------------------------------------
        MenuItem menuFileExit = new MenuItem("Exit");
        menuFileExit.setOnAction( e-> {
            System.exit(0);
        });
        menuFile.getItems().add(menuFileExit);
        //----------------------------------------------------------------
        //----------------------------------------------------------------
        MenuItem menuSpeedUp = new MenuItem("Speed Up");
        menuSpeedUp.setOnAction( e-> {
            if (mediaView.isVisible()) {
                rate+=0.1;
                mediaPlayer.setRate(rate);
                labelSpeed.setText(String.format("X%.1f", rate));
            }
            ;});
        menuTools.getItems().add(menuSpeedUp);
        //----------------------------------------------------------------
        MenuItem menuSpeedDown = new MenuItem("Speed down");
        menuSpeedDown.setOnAction( e-> {
            if (mediaView.isVisible()) {
                rate-=0.1;
                mediaPlayer.setRate(rate);
                labelSpeed.setText(String.format("X%.1f", rate));
            }
            ;});
        menuTools.getItems().add(menuSpeedDown);
        //----------------------------------------------------------------
        menuTools.getItems().add(new SeparatorMenuItem());
        //----------------------------------------------------------------
        MenuItem menuZoomIn = new MenuItem("Zoom in");
        menuZoomIn.setOnAction( e-> {
            if (mediaView.isVisible()) {
                zoom += 0.01;
                if (zoom <= 1.05){
                    mediaView.setScaleX(zoom);
                    mediaView.setScaleY(zoom);
                }else
                    zoom = 1.05;

            }

        });
        menuTools.getItems().add(menuZoomIn);
        //----------------------------------------------------------------
        MenuItem menuZoomOut = new MenuItem("Zoom out");
        menuZoomOut.setOnAction( e-> {
            if (mediaView.isVisible()) {
                zoom -= 0.03;
                if (zoom >= 0.7){
                    mediaView.setScaleX(zoom);
                    mediaView.setScaleY(zoom);
                }else
                    zoom = 0.7;
            }
        });
        menuTools.getItems().add(menuZoomOut);
        //----------------------------------------------------------------
        MenuItem menuAbout = new MenuItem("About");
        menuAbout.setOnAction( e-> {
            try {
                onAbout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuHelp.getItems().add(menuAbout);
    }



    private void saveFile() throws FileNotFoundException {
        if (mediaView.isVisible()){
            fileChooserSave.setTitle("Save");
            fileChooserSave.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MP4 Files", "*.mp4"),
                    new FileChooser.ExtensionFilter("MP3 Files", "*.mp3")

            );

            File selectedFile = fileChooserSave.showSaveDialog(null);
            if (selectedFile != null) {
                // Perform your save operation here
                try {
                    saveFileAsMP4(file , selectedFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public String fileLoader(){
        FileChooser fileChooser = new FileChooser();
        File file1 = fileChooser.showOpenDialog(null);
        if (file1 != null){
            String path = file1.getPath();
            System.out.print(path);
            return path;
        }else
            return null;
    }

    public void setToPlay(String s){
        if (s != null){
            file = new File(s);
            media = new Media(file.toURI().toString());
            System.out.println(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

//            DoubleProperty widthProp = mediaView.fitWidthProperty();
//            DoubleProperty heightProp = mediaView.fitHeightProperty()   ;
//
//            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
//            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            handleSlideTime();
            handleSlideVolume();


            mediaPlayer.setAutoPlay(true);
            mediaView.setSmooth(true);
            buttonPlay.setVisible(false);
            buttonPause.setVisible(true);
            mediaView.setVisible(true);
            labelSpeed.setVisible(true);
            labelTime.setVisible(true);
            labelTimeMax.setVisible(true);
            labelVolume.setVisible(true);
            labelSpeed.setText(String.format("X%.1f", rate));



        }

    }

    public  void saveFileAsMP4(File inputFile, File outputFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        fileOutputStream.close();

        System.out.println("File saved as " + outputFile.getPath());
    }

    public void handleSlideTime(){
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                sliderTime.setValue(t1.toSeconds());
                int totalSeconds = (int) t1.toSeconds();
                int hours = totalSeconds / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                int seconds = (totalSeconds % 3600) % 60;
                labelTime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }
        });

        sliderTime.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(sliderTime.getValue()));
            }
        });

        sliderTime.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(sliderTime.getValue()));
            }
        });

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                Duration total = media.getDuration();
                sliderTime.setMax(total.toSeconds());
                int totalSeconds = (int) total.toSeconds();
                int hours = totalSeconds / 3600;
                int minutes = (totalSeconds % 3600) / 60;
                int seconds = (totalSeconds % 3600) % 60;
                labelTimeMax.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }
        });
    }

    public void handleSlideVolume(){

        mediaPlayer.setVolume(0.5);
        sliderVolume.setValue(mediaPlayer.getVolume()*100);
        int volume = (int) (mediaPlayer.getVolume()*100);
        labelVolume.setText(String.format("%%%d",volume));
        sliderVolume.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(sliderVolume.getValue()/100);
                int volume = (int) (mediaPlayer.getVolume()*100);
                labelVolume.setText(String.format("%%%d",volume));
            }
        });



    }


    public void onAbout() throws IOException {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(6)));
        setToPlay("src/main/resources/com/example/halalsound/cena.mp4");
        timeline.play();
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Perform your action here
                if (mediaView.isVisible()){
                    close();
                }

                // This code will be executed after 5 seconds
            }
        });



    }

    public void onSetting() throws IOException {
        Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(10)));
        setToPlay("src/main/resources/com/example/halalsound/rat.mp4");
        timeline1.play();
        timeline1.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Perform your action here
                if (mediaView.isVisible()){
                    close();
                }

                // This code will be executed after 5 seconds
            }
        });



    }

    public void close() {
        onPause();
        mediaPlayer.dispose();
        mediaView.setVisible(false);
        labelSpeed.setVisible(false);
        labelTime.setVisible(false);
        labelTimeMax.setVisible(false);

    }
}
