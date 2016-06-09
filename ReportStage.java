package exam;



import java.text.*;
import java.util.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
/*
Window for displaying report of scores by subject.  Also allows users to save
test
*/
class ReportStage 
{
    private final Stage stage;
    private final Map<String,Integer[]> hmSubjectScores;
    private final Report report;
    private final int correct;
    private final int total;
    
    public ReportStage(Map<String,Integer[]> hashMap, Report report, int correct, int total)
    {
        stage = new Stage();
        hmSubjectScores = hashMap;
        this.report = report;
        this.correct = correct;
        this.total = total;
        
        BorderPane root = new BorderPane();
        
        root.setTop(createHeaderPane());
        root.setCenter(createGridPane());
        root.setBottom(createButtonPane());
        
        Scene scene = new Scene(root, 300, 400);
        stage.setScene(scene);
        stage.setTitle("Test Report");
    }
    
    private Node createHeaderPane()
    {    
        Label lblHeader = new Label("Results by Subject Area");
        HBox hBoxHeader = new HBox(10);
        hBoxHeader.setAlignment(Pos.CENTER);
        hBoxHeader.setPadding(new Insets(25, 10, 10, 10));
        hBoxHeader.getChildren().add(lblHeader);
        return lblHeader;        
    }
    
    private Node createGridPane()
    {  
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        
        Set<String> subjects = hmSubjectScores.keySet();
        
        DecimalFormat df = new DecimalFormat("#.00");  //Object to format double values
                
        int row = 0;
        int column = 0;

        for (String subject: subjects)
        {
            Integer[] scores = hmSubjectScores.get(subject);
            String score = scores[0] + "/" + scores[1];
            double score0 = (double)scores[0];
            double score1 = (double)scores[1];
            double percentCorrect = (score0/score1) * 100;
            gridPane.add(new Label(subject), column, row);
            column++;
            gridPane.add(new Label(score), column, row);
            column++;
            gridPane.add(new Label(df.format(percentCorrect) + "%"), column, row);
            column = 0;
            row++;
        }

        gridPane.add(new Label("Total"), column, row);
        column++;
        gridPane.add(new Label(this.correct + "/" + this.total), column, row);
        column++;
        double totalPercentCorrect = (double)this.correct/(double)this.total * 100;
        gridPane.add(new Label(df.format(totalPercentCorrect) + "%"), column, row);
        return gridPane;        
    }
    
    private Node createButtonPane()
    {       
        Button btnSave = new Button("Save");
        btnSave.setOnAction(e ->
        {
            report.saveReport(stage); 
        });
        Button btnClose = new Button("Close");
        btnClose.setOnAction(e ->
        {
           stage.close(); 
        });
        
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10, 0, 10, 0));        
        hBox.getChildren().addAll(btnSave, btnClose);  
        return hBox;
    }
    
    public void show()
    {
        stage.show();
    }
}
