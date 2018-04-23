import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView.TableViewSelectionModel;


/**
 * @author schatrathi3
 * @version 1
 */
public class MusicPlayer extends Application {

    private TableView<MediaExtension> mediaTable;
    private List<MediaExtension> mediaList;
    private ObservableList<MediaExtension> obsMediaList;
    private int mediaFileCount = 0;
    private int mediaCounter = 0;
    private Stage myStage;

    /**
     * this is a constructor for the MusicPlayer class
     */
    public MusicPlayer() {
        mediaTable = new TableView<MediaExtension>();
        mediaList = new ArrayList<MediaExtension>();
        obsMediaList = FXCollections.observableArrayList();
    }

    /**
     * this is a getter method for the mediatable variable
     * @return a TableView object is returned
     */
    public TableView<MediaExtension> getMediaTable() {
        return mediaTable;
    }

    /**
     * this is a getter method for the medialist variable
     * @return a List object is returned
     */
    public List<MediaExtension> getMediaList() {
        return mediaList;
    }

    /**
     * this is a getter method for the obsmedialist variable
     * @return a observablelist object is returned
     */
    public ObservableList<MediaExtension> getObsMediaList() {
        return obsMediaList;
    }

    /**
     * this is a getter method for the mediafilecount variable
     * @return a int is returned
     */
    public int getMediaFileCount() {
        return mediaFileCount;
    }

    /**
     * this is a getter method for the mediamediacount variable
     * @return a int is returned
     */
    public int getMediaCounter() {
        return mediaCounter;
    }

    /**
     * this is a getter method for the getmystage variable
     * @return a stage object is returned
     */
    public Stage getMyStage() {
        return myStage;
    }

    /**
     * this method fills a list with media objects
     */
    public void fillList() {
        File folder = new File(System.getProperty("user.dir"));

        mediaFileCount = folder.list((File dir, String name) -> {
                boolean value;
                if (name.endsWith(".mp3")) {
                    value = true;
                } else {
                    value = false;
                }
                return value;
            }).length;

        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()
                && fileEntry.getName().endsWith(".mp3")) {
                MediaExtension song =
                    new MediaExtension(fileEntry.toURI().toString(),
                    fileEntry.getName());
                mediaList.add(song);
            }
        }
    }

    /**
     * this method refreshes the observable list
     * each time the search function is used
     * @param searchTag   a string value of the tag
     * @param searchValue a string vale of the value
     */
    public void refreshObservable(String searchTag, String searchValue) {
        obsMediaList.clear();

        boolean searchFlag = true;

        if (searchTag == null || searchTag.isEmpty()
            || searchValue == null || searchValue.isEmpty()) {
            searchFlag = false;
        }

        for (MediaExtension m : mediaList) {
            if (!searchFlag) {
                obsMediaList.add(m);
            } else {
                if (searchTag.contains("filename")) {
                    if (!m.fileNameProperty().toString().contains(searchValue))
                    {
                        continue;
                    }
                } else if (searchTag.contains("artist")) {
                    if (!m.artistProperty().toString().contains(searchValue)) {
                        continue;
                    }
                } else if (searchTag.contains("album")) {
                    if (!m.albumProperty().toString().contains(searchValue)) {
                        continue;
                    }
                } else if (searchTag.contains("title")) {
                    if (!m.titleProperty().toString().contains(searchValue)) {
                        continue;
                    }
                }

                obsMediaList.add(m);
            }
        }
    }

    /**
     * @author schatrathi3
     * @version 1
     */
    public class MediaExtension {
        protected Media song;
        private MediaPlayer mPlayer;

        private StringProperty mediaFileName = new SimpleStringProperty();
        protected StringProperty author = new SimpleStringProperty();
        protected StringProperty title = new SimpleStringProperty();
        protected StringProperty album = new SimpleStringProperty();

        /**
         * this is a constructor for the mediaextension class
         * @param  uri      this is a string repr of the uri
         * @param  fileName name of the file, a string
         */
        public MediaExtension(String uri, String fileName) {
            mediaFileName.setValue(fileName);

            song = new Media(uri);

            mPlayer = new MediaPlayer(song);
            mPlayer.setAutoPlay(false);
            mPlayer.setOnReady(new CheckReady());
        }

        /**
         * this method gets the author variable
         * @return a stringproperty object is returned
         */
        public StringProperty artistProperty() {
            return author;
        }

         /**
         * this method gets the title variable
         * @return a stringproperty object is returned
         */
        public StringProperty titleProperty() {
            return title;
        }

         /**
         * this method gets the album variable
         * @return a stringproperty object is returned
         */
        public StringProperty albumProperty() {
            return album;
        }

         /**
         * this method gets the mediafilename variable
         * @return a stringproperty object is returned
         */
        public StringProperty fileNameProperty() {
            return mediaFileName;
        }

         /**
         * this method gets the mplayer variable
         * @return a mediaplayer object is returned
         */
        public MediaPlayer getMediaPlayer() {
            return this.mPlayer;
        }

        /**
         * this gets the media object song
         * @return a media object is returned
         */
        public Media getMedia() {
            return this.song;
        }

        /**
        * @author schatrathi3
        * @version 1
        */
        public class CheckReady implements Runnable {

            /**
             * this method gets the metadata from
             * the mp3 files and sets them
             */
            public void run() {
                String propValue = "";
                Object objValue = song.getMetadata().get("title");
                if (objValue != null) {
                    propValue = objValue.toString();
                }
                title.setValue(propValue);

                propValue = "";
                objValue = song.getMetadata().get("album");
                if (objValue != null) {
                    propValue = objValue.toString();
                }
                album.setValue(propValue);

                propValue = "";
                objValue = song.getMetadata().get("artist");
                if (objValue != null) {
                    propValue = objValue.toString();
                }
                author.setValue(propValue);

                mediaCounter++;

                if (mediaCounter == mediaFileCount) {
                    readyToShow();
                }
            }
        }

    }

    /**
     * this method creates the tableview and populates it
     */
    public void createTableView() {

        mediaTable.setItems(obsMediaList);

        //java compiler has ability to use type inference so you dont have
        //to put the type in the diamond on the right side, so u can
        //use empty diamonds on the initialization
        TableColumn<MediaExtension, String> fileName =
            new TableColumn<>("File Name");
        TableColumn attributes = new TableColumn("Attributes");
        TableColumn<MediaExtension, String> artist =
            new TableColumn<MediaExtension, String>("Artist");
        TableColumn<MediaExtension, String> title =
            new TableColumn<MediaExtension, String>("Title");
        TableColumn<MediaExtension, String> album =
            new TableColumn<MediaExtension, String>("Album");

        attributes.getColumns().addAll(artist, title, album);

        mediaTable.getColumns().add(fileName);
        mediaTable.getColumns().add(attributes);

        fileName.setCellValueFactory(
            new PropertyValueFactory<MediaExtension, String>("fileName"));
        artist.setCellValueFactory(
            new PropertyValueFactory<MediaExtension, String>("artist"));
        title.setCellValueFactory(
            new PropertyValueFactory<MediaExtension, String>("title"));
        album.setCellValueFactory(
            new PropertyValueFactory<MediaExtension, String>("album"));
    }

    /**
     * this method calls the show scene for the stage once
     * the stage is ready to be seen in javafx
     */
    public void readyToShow() {
        refreshObservable(null, null);

        createTableView();
        mediaTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Button playButton = new Button();
        playButton.setText("Play");


        Button pauseButton = new Button();
        pauseButton.setText("Pause");
        pauseButton.setDisable(true);

        Button searchButton = new Button();
        searchButton.setText("Search Songs");
        Button showButton = new Button();
        showButton.setText("Show all Songs");
        showButton.setDisable(true);

        TableViewSelectionModel<MediaExtension> defaultSelectionModel =
            mediaTable.getSelectionModel();

        playButton.setOnAction(e -> {
                MediaExtension mObject =
                    mediaTable.getSelectionModel().getSelectedItem();
                if (mObject != null) {
                    playButton.setDisable(true);
                    mediaTable.setSelectionModel(null);

                    mObject.getMediaPlayer().play();
                    pauseButton.setDisable(false);
                }
            });

        pauseButton.setOnAction(e -> {
                mediaTable.setSelectionModel(defaultSelectionModel);
                MediaExtension mObject =
                    mediaTable.getSelectionModel().getSelectedItem();

                if (mObject != null) {
                    mObject.getMediaPlayer().pause();
                    playButton.setDisable(false);
                    pauseButton.setDisable(true);
                }
            });

        searchButton.setOnAction(e -> {
                Stage newStage = new Stage();
                VBox comp = new VBox();

                Label label1 =
                    new Label("Pick a category"
                        + "(FileName, Artist, Title, Album):");
                TextField textField1 = new TextField();
                Label label2 = new Label("Enter Value(case sensitive):");
                TextField textField2 = new TextField();

                Button okButton = new Button();
                okButton.setText("Ok");

                okButton.setOnAction(y -> {
                        String searchTag = textField1.getText().toLowerCase();
                        String searchValue = textField2.getText();

                        newStage.close();

                        refreshObservable(searchTag, searchValue);
                        showButton.setDisable(false);
                        searchButton.setDisable(true);
                    });

                comp.getChildren().addAll(label1, textField1,
                    label2, textField2, okButton);

                Scene stageScene = new Scene(comp, 300, 300);
                newStage.setScene(stageScene);
                newStage.show();

                showButton.setDisable(true);
            });

        showButton.setOnAction(e -> {
                refreshObservable(null, null);
                searchButton.setDisable(false);
                showButton.setDisable(true);
            });

        HBox entryBox = new HBox();
        entryBox.getChildren().addAll(playButton, pauseButton,
            searchButton, showButton);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(mediaTable, entryBox);

        Scene scene = new Scene(vbox, 690, 400);
        myStage.setScene(scene);
        myStage.setTitle("Music Player");

        myStage.show();
    }

    /**
     * this method initializes the stage and
     * starts the javafx functionality
     * so that the tableview comes up
     * @param  stage a Stage object that
     * is initialized
     */
    @Override public void start(Stage stage) {
        myStage = stage;
        fillList();
    }

    /**
     * this method is used to launch the app
     * @param args this is a string array that
     * can be used to pass in arguments
     * from command line
     */
    public static void main(String[] args) {
        launch();

    }

}