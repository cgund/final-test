
import java.util.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/*
Question window.  User enters answers to questions.  Contains Button for submitting
test.
*/
public class TestStage extends Application 
{    
    private TextField tfName;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) 
    {        
        this.primaryStage = primaryStage;
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        
        //Variable to hold list of random QuestionAnswerSets
        QuestionBank qBank = new QuestionBank();
        List<NodeQuestionAnswer> questions = qBank.selectRandom();
        
        root.setTop(createNamePane());
        root.setCenter(createTableView(questions));
        root.setBottom(createSubmitButton(questions));
               
        primaryStage.setTitle("Final Exam");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void noNameAlert()
    {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Missing Name");
        alert.setHeaderText(null);
        alert.setContentText("Name required");
        alert.show();
    }
    
    /*
    Contains control for entering student name
    */
    public Node createNamePane()
    {
        Label lblName = new Label("Enter Your Name: ");
        tfName = new TextField();
        HBox hbName = new HBox(10);
        hbName.setAlignment(Pos.CENTER_RIGHT);
        hbName.setPadding(new Insets(10, 10, 10, 10));
        hbName.getChildren().addAll(lblName, tfName);
        return hbName;
    }
    
    /*
    Contains table for viewing test questions and answer fields
    */
    public Node createTableView(List<NodeQuestionAnswer> lstQA)
    {
        TableView tableView = new TableView();
        tableView.setPadding(new Insets(10, 10, 10, 10));
                
        TableColumn tcSubject = new TableColumn("Subject");
        TableColumn tcCategory = new TableColumn("Category");
        TableColumn tcQuestion = new TableColumn("Question");
        TableColumn tcResponse = new TableColumn("Answer");
        
        //Binds TableView columns width property to TableView width property
        tcSubject.prefWidthProperty().bind(tableView.widthProperty().multiply(.2));
        tcCategory.prefWidthProperty().bind(tableView.widthProperty().multiply(.15));
        tcQuestion.prefWidthProperty().bind(tableView.widthProperty().multiply(.35));
        tcResponse.prefWidthProperty().bind(tableView.widthProperty().multiply(.25));
        
        /*
        Specifies how to populate TableCell within TableColumn.  Passes property of
        QuestionAnswerSet to PropertyValueFactory constructor
        */
        tcSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        tcCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        tcQuestion.setCellValueFactory(new PropertyValueFactory<>("question"));
        /*
        Wraps text within each cell that is part of colQuestion.  
        */
        tcQuestion.setCellFactory(new Callback<TableColumn<NodeQuestionAnswer,String>, TableCell<NodeQuestionAnswer,String>>() 
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
                                text.setWrappingWidth(tcQuestion.getPrefWidth() - 5); // Setting the wrapping width to the Text
                                setGraphic(text);
                        }
                    }
                };
                return cell;
            }
        });
        tcResponse.setCellValueFactory(new PropertyValueFactory<>("tfAnswer"));
        
        //Returns ObservableList bound to TableColumn, adds TableColumns
        tableView.getColumns().addAll(tcSubject, tcCategory, tcQuestion, tcResponse);

        ObservableList<NodeQuestionAnswer> data = FXCollections.observableArrayList();
        data.addAll(lstQA);
        
        tableView.setItems(data);
        return tableView;
    }
    
    /*
    Contains submit button and button click event handler
    */
    public Node createSubmitButton(List<NodeQuestionAnswer> lstQA)
    {
        
        Button btnSubmit = new Button("Submit");                
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.CENTER);
        hbButton.setPadding(new Insets(20, 0, 10, 0));
        hbButton.getChildren().add(btnSubmit);
        
        btnSubmit.setOnAction(e -> 
        {
            TestGrader grader = new TestGrader(lstQA); //passes List to TestGrader
            List<NodeQuestionAnswer> lstGraded = grader.gradeTest(); //Returns graded List
            Report report = grader.getReport(); //Returns Report
            String userName = tfName.getText(); 
            if (userName.isEmpty())
            {
                noNameAlert();
            }
            else
            {
                report.setName(userName);
                ResultsStage results = new ResultsStage(lstGraded, report); //Passes references to ResultsStage
                results.show();
                primaryStage.close();                
            }
        });
        return hbButton;
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
    
}
