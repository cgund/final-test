package exam;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
/*
Class for collecting statistics on test results.  Uses hashmap to store scores
by subject.  Contains methods for displaying and saving reports
*/
public class Report 
{
    private String userName;
    private String dateTime;
    
    private int total = 0;
    private int correct = 0;
    private final List<NodeQuestionAnswer> lstQA;
    private final Map<String,Integer[]> hmSubjectScores = new HashMap<>();
    
    public Report(List<NodeQuestionAnswer> lstQA)
    {
        this.lstQA = lstQA;
        for (NodeQuestionAnswer question: lstQA)
        {
            String subject = question.getSubject();
            if (!hmSubjectScores.containsKey(subject))
            {
                Integer[] scores = {0, 0};
                hmSubjectScores.put(subject, scores);
            }
        }
    }
    
    /*
    Processes a correct answer
    */
    public void correct(String subject)
    {
        correct++;
        total++;
        correctByCategory(subject, true);
    }
    
    /*
    Process an incorrect answer
    */
    public void incorrect(String subject)
    {
        total++;
        correctByCategory(subject, false);
    }
    
    /*
    Keeps track of score within category
    */
    public void correctByCategory(String subject, boolean isCorrect)
    {
        Integer[] score = hmSubjectScores.get(subject);
        if (isCorrect)
        {
            score[0] = score[0] + 1;
            score[1] = score[1] + 1;
        }
        else
        {
            score[1] = score[1] + 1;
        }
    }
    
    public void setName(String userName)
    {
        this.userName = userName;
    }
    
    /*
    Displays scores by subject category and overall
    */
    public void displayReport()
    {
        ReportStage reportStage = new ReportStage(hmSubjectScores, this, correct, total);
        reportStage.show();
    }
    
    /*
    Saves report to file
    */
    public void saveReport(Stage stage)
    {    
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(userName + "_" + dateTime + ".txt");
        fileChooser.setTitle("Save Exam");
        FileChooser.ExtensionFilter extFilter = new ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(stage);
        if (file !=  null)
        {
            saveFile(file, dateTime);
        }
    }

    /*
    Timestamps report
    */
    public String stampDateTime()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM uuuu H.m.s");
        LocalDateTime now = LocalDateTime.now();
        dateTime = now.format(formatter);
        return dateTime;        
    }
    
    private void saveFile(File file, String dateTime)
    {      
        try
        {
            PrintWriter output = new PrintWriter(file);
            output.println("STUDENT NAME: " + userName);
            output.println("DATE/TIME: " + dateTime);
            output.println();
            int questionNum = 1;
            for (NodeQuestionAnswer qaSet: lstQA)
            {
                String subject = qaSet.getSubject();
                String studentInput = qaSet.getStudentAnswer();
                String correctAnswer = qaSet.getCorrectAnswer();
                String points = qaSet.getPoints();
                
                output.println(questionNum+"::"+subject+"::"+studentInput+"::"+correctAnswer+"::"+points);
                questionNum++;
            }
            output.println();
            output.println("Final Score: " + correct + "/" + total);
            output.close();
            
            notifySave();
        }
        catch(FileNotFoundException ex)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(ex.getMessage());
            alert.show();
        }        
    }
    
    /*
    Alerts user to successful file save
    */
    private void notifySave()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("File Saved");
        alert.setHeaderText(null);
        alert.setContentText("File successfully saved");
        alert.show();
    }
}
