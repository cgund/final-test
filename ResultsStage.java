package exam;


import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/*
Basically the same as QuestionStage.  Except this class displays student answer
compared to stored answer and number of points earned for that question.  It also
contains two buttons.  One button displays test report and the other allows student
to retake test
*/
public class ResultsStage 
{
    private Stage stage;
    
    public ResultsStage(List<NodeQuestionAnswer>lstQA, Report report)
    {
        stage = new Stage();
        
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        
        root.setCenter(createTableResultsPane(lstQA));
        root.setBottom(createButtonPane(report));
        
        stage.setScene(scene);
    }
    
    public void show()
    {
        stage.show();
    }
    
    private Node createTableResultsPane(List<NodeQuestionAnswer>lstQA)
    {
        TableView tvResults = new TableView();        
        tvResults.setPadding(new Insets(10, 10, 10, 10));
        
        TableColumn colSubject = new TableColumn("Subject");
        TableColumn colCategory = new TableColumn("Category");
        TableColumn colQuestion = new TableColumn("Question");
        TableColumn colStudentInput = new TableColumn("Your Answer");
        TableColumn colCorrectAnswer = new TableColumn("Correct Answer");
        TableColumn colPoints = new TableColumn("Points");
        
        tvResults.getColumns().addAll(colSubject, colCategory, colQuestion, colStudentInput, colCorrectAnswer, colPoints);
        
        colSubject.prefWidthProperty().bind(tvResults.widthProperty().multiply(.2));
        colCategory.prefWidthProperty().bind(tvResults.widthProperty().multiply(.15));
        colQuestion.prefWidthProperty().bind(tvResults.widthProperty().multiply(.3));
        colStudentInput.prefWidthProperty().bind(tvResults.widthProperty().multiply(.125));
        colCorrectAnswer.prefWidthProperty().bind(tvResults.widthProperty().multiply(.125));
        colPoints.prefWidthProperty().bind(tvResults.widthProperty().multiply(.075));
        
        colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuestion.setCellValueFactory(new PropertyValueFactory<>("question"));
        colQuestion.setCellFactory(new Callback<TableColumn<NodeQuestionAnswer,String>, TableCell<NodeQuestionAnswer,String>>() 
        {
            @Override
            public TableCell<NodeQuestionAnswer, String> call(TableColumn<NodeQuestionAnswer, String> param) 
            {
                final TableCell<NodeQuestionAnswer, String> cell = new TableCell<NodeQuestionAnswer, String>() 
                {
                    private Text text;
                    @Override
                    public void updateItem(String item, boolean empty) 
                    {
                        super.updateItem(item, empty);
                        if (!isEmpty()) 
                        {
                                text = new Text(item);
                                text.setWrappingWidth(colQuestion.getPrefWidth() - 5); // Setting the wrapping width to the Text
                                setGraphic(text);
                        }
                    }
                };
                return cell;
            }
        });
        colStudentInput.setCellValueFactory(new PropertyValueFactory<>("studentAnswer"));
        colCorrectAnswer.setCellValueFactory(new PropertyValueFactory<>("correctAnswer"));
        colPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        
        ObservableList<NodeQuestionAnswer> data = FXCollections.observableArrayList();
        data.addAll(lstQA);
        
        tvResults.setItems(data);
        return tvResults;        
    }
    
    private Node createButtonPane(Report report)
    {
        Button btnSave = new Button("Display Report");
        Button btnRedo = new Button("Redo Test");
        
        btnSave.setOnAction(e ->
        {
            report.displayReport();
        });
        
        btnRedo.setOnAction(e -> 
        {
            TestStage questionStage = new TestStage();
            questionStage.start(new Stage());
            stage.close();
        });
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10, 0, 10, 0));
        hBox.getChildren().addAll(btnSave, btnRedo);       
        return hBox;
    }
}
