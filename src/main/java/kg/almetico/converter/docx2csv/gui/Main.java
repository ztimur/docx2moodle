package kg.almetico.converter.docx2csv.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kg.almetico.converter.docx2csv.DocParser;
import kg.almetico.converter.docx2csv.Utils;
import kg.almetico.converter.docx2csv.model.moodle.Quiz;

import java.io.File;
import java.io.FileOutputStream;

public class Main extends Application {

    private final String datePattern = "yyyy-MM-dd";

    FileChooser fileChooser = new FileChooser();
    DatePicker defaultGrade;
    DatePicker endDate;
    Button browseButton, processButton;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    ToggleGroup group;
    ProgressBar progressBar;
    Label infoLine;
    Label messages;

    Stage primaryStage;
    private String selectedFileName;
    private Quiz quiz;

    private Pane buildContents() {

        Label labelSelectFile = new Label("Please select the *.docx file:");
        GridPane.setRowIndex(labelSelectFile, 0);
        GridPane.setColumnIndex(labelSelectFile, 0);

        browseButton = new Button("Browse");
        GridPane.setRowIndex(browseButton, 0);
        GridPane.setColumnIndex(browseButton, 1);

        Label defaultGradeLabel = new Label("Default grade:");
        GridPane.setRowIndex(defaultGradeLabel, 1);
        GridPane.setColumnIndex(defaultGradeLabel, 0);

        GridPane pane = new GridPane();
//        pane.setMinWidth(500);
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setPadding(new Insets(10));

        pane.getChildren().addAll(labelSelectFile, browseButton);

        BorderPane root = new BorderPane();
        root.setCenter(pane);

        processButton = new Button("Run");
        GridPane.setRowIndex(processButton, 4);
        GridPane.setColumnIndex(processButton, 1);

        VBox footer = new VBox();
        messages = new Label();

        infoLine = new Label();
        infoLine.setVisible(false);
        progressBar = new ProgressBar(0);
        progressBar.setVisible(false);
        progressBar.setMinWidth(400);
        footer.getChildren().addAll(messages, infoLine, progressBar, processButton);
        footer.setSpacing(10);
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-background-color: #e0e0e0;");
        footer.setPadding(new Insets(4));
        root.setBottom(footer);


        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Pane root = buildContents();

        initHandlers(primaryStage);

        Scene scene = new Scene(root, 440, 500);
//        scene.getStylesheets().add("css/app.css");

        primaryStage.setScene(scene);

        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void initHandlers(Stage primaryStage) {
        browseButton.setOnAction(event -> {
            fileChooser.setTitle("Please select a file");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                primaryStage.setTitle(selectedFile.getAbsolutePath());
                this.selectedFileName = selectedFile.getAbsolutePath();
            }
        });

        processButton.setOnAction(event -> {

            messages.setText("");

            try {
                process(this.selectedFileName);
            } catch (Throwable e) {
                alert(Alert.AlertType.ERROR, e.getMessage());
            }
        });
    }

    private void process(String filename) {

        new Thread(() -> {
            try {
                infoLine.setVisible(true);
                processButton.setDisable(true);
                progressBar.setVisible(true);
                progressBar.setProgress(0);
                DocParser parser = new DocParser();
                parser.parse(filename);
                quiz = parser.getQuiz();

                FileOutputStream outputStream = new FileOutputStream(this.selectedFileName + ".xml");
                Utils.marshallQuiz(quiz, outputStream);
                outputStream.close();
                Platform.runLater(() -> {
                    messages.setText(String.format("Processed %d questions.", quiz.getQuestions().size()));
                });
            } catch (Throwable e) {
                e.printStackTrace();
                messages.setText(e.getMessage());
            } finally {
                processButton.setDisable(false);
                progressBar.setVisible(false);
                infoLine.setVisible(false);
            }
        }).start();
    }

    private void alert(Alert.AlertType type, String msg) {
        new Alert(type, msg).showAndWait();
    }

    public static void main(String[] args) {
        launch(Main.class, args);
    }
}
