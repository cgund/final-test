/*
Craig Gundacker
Exam 3

Each instance of class represents one question-answer "set".
*/
import javafx.scene.control.TextField;


public class NodeQuestionAnswer 
{
    private final String subject;
    private final String category;
    private final String question;
    private final TextField tfAnswer;
    private String studentAnswer;
    private final String correctAnswer;
    private String points;

    public NodeQuestionAnswer(String subject, String category, String question, String answer)
    {
        this.subject = subject;
        this.category = category;
        this.question = question;
        tfAnswer = new TextField();
        this.correctAnswer = answer;
    }

    public String getSubject() {
        return subject;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public TextField getTfAnswer() {
        return tfAnswer;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setPoints(String points)
    {
        this.points = points;
    }
    
    public String getPoints()
    {
        return points;
    }
}
