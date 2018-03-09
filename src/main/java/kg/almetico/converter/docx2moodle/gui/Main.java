package kg.almetico.converter.docx2moodle.gui;

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
import kg.almetico.converter.docx2moodle.DocParser;
import kg.almetico.converter.docx2moodle.QuestionValidationException;
import kg.almetico.converter.docx2moodle.Utils;
import kg.almetico.converter.docx2moodle.model.moodle.Question;
import kg.almetico.converter.docx2moodle.model.moodle.Quiz;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main extends Application {

    private final String datePattern = "yyyy-MM-dd";

    FileChooser fileChooser = new FileChooser();
    DatePicker defaultGrade;
    DatePicker endDate;
    Button browseButton, processButton;
    Label infoLine;
    TextInputControl messages;

    Stage primaryStage;
    private String selectedFileName;
    private Quiz quiz;
    private File lastPath;

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
        messages = new TextArea();
        messages.setEditable(false);


        infoLine = new Label();
        infoLine.setVisible(false);
        footer.getChildren().addAll(messages, infoLine, processButton);
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

        Scene scene = new Scene(root, 1024, 640);
//        scene.getStylesheets().add("css/app.css");

        primaryStage.setScene(scene);

        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void initHandlers(Stage primaryStage) {
        browseButton.setOnAction(event -> {
            fileChooser.setTitle("Please select a file");
            if (this.lastPath != null) {
                System.out.println(this.lastPath);
                fileChooser.setInitialDirectory(this.lastPath);
            }
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                primaryStage.setTitle(selectedFile.getAbsolutePath());
                this.selectedFileName = selectedFile.getAbsolutePath();
                this.lastPath = selectedFile.getParentFile();
            }
        });

        processButton.setOnAction(event -> {

            messages.setText("");
            process(this.selectedFileName);
        });
    }

    private void process(String filename) {

        new Thread(() -> {
            try {
                infoLine.setVisible(true);
                processButton.setDisable(true);
                DocParser parser = new DocParser();
                parser.parse(filename);
                quiz = parser.getQuiz();

                StringBuilder sb = new StringBuilder();
                int i = 0;
                for (Question q : quiz.getQuestions()) {
                    sb.append(String.format("%d. %s\n", ++i, q.getName()));
                }

                FileOutputStream outputStream = new FileOutputStream(this.selectedFileName + ".xml");

                String r = Utils.marshallQuiz(quiz);
                FileUtils.writeStringToFile(new File(this.selectedFileName + ".xml"), r, "UTF-8");
                outputStream.close();
                Platform.runLater(() -> {
                    messages.setText(String.format("%s\nОбработано %d вопросов.", sb.toString(), quiz.getQuestions().size()));
                });
            } catch (QuestionValidationException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    String m = "";
                    try {
                        m = Utils.marshallQuestion(e.getQuestion());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JAXBException e1) {
                        e1.printStackTrace();
                    }
                    messages.setText(String.format("Error: %s. %s. %s", e.getMessage(), e.getQuestion().getName(), m));
                });
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    messages.setText(String.format("Error: %s.", e.getMessage()));
                });
            } finally {
                processButton.setDisable(false);
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
