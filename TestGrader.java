package exam;


import java.util.*;

/*
Contains methods to grade test, return report on test,
and approximately match student input to stored answer
*/
public class TestGrader implements Grader 
{
    private final List<NodeQuestionAnswer> lstQA;
    private final Report report;
    
    private final static double MIN_PROP_CORRECT = .65;
    
    public TestGrader(List<NodeQuestionAnswer> lstQA)
    {
        this.lstQA = lstQA;
        report = new Report(this.lstQA);
    }
    
    /*
    Overrides gradeTest() contained in abstract Grader class
    */
    @Override
    public List<NodeQuestionAnswer> gradeTest()
    {        
        for (NodeQuestionAnswer question: lstQA)
        {
            String category = question.getCategory();
            String subject = question.getSubject();
            String studentAnswer = question.getTfAnswer().getText().toLowerCase().trim(); //Gets TextField input
            question.setStudentAnswer(studentAnswer); 
            String correctAnswer = question.getCorrectAnswer().toLowerCase().trim();
            
            if (!studentAnswer.isEmpty())
            {
                if (category.equals("True/False")) //Branch to check T/F questions
                {
                    String firstLetterSA = studentAnswer.substring(0, 1);
                    String firstLetterCA = correctAnswer.substring(0, 1);
                    
                    if (firstLetterSA.equals(firstLetterCA))
                    {
                        report.correct(subject);
                        question.setPoints("1/1");
                    }
                    else
                    {
                        report.incorrect(subject);
                        question.setPoints("0/1");
                    }
                }
                else  //Category is not T/F
                {
                    float propMatch = fuzzy(studentAnswer, correctAnswer, subject);
                    if (propMatch <= MIN_PROP_CORRECT) //if fuzzy returns value less than constant
                    {
                        report.incorrect(subject);
                        question.setPoints("0/1");
                    }
                    else 
                    {
                        report.correct(subject);
                        question.setPoints("1/1");
                    }
                }
            }
            else  //Answer is blank
            {
                report.incorrect(subject);
                question.setPoints("0/1");
            }
        }
        
        return lstQA;
    }
    
    @Override
    public Report getReport()
    {
        return report;
    }
    
    /*
    Primitive attempt at string approximation.  There are much more advanced 
    open-source fuzzy methods(e.g. Levenshtein)
    It only handles spelling mistakes at start or end of string or trunctuated 
    answers.
    */
    public float fuzzy(String sAnswer, String cAnswer, String subject)
    {
        char[] charArrayInput = sAnswer.toCharArray();
        char[] charArrayStored = cAnswer.toCharArray();
        int lengthInputArray = charArrayInput.length;
        int lengthStoredArray = charArrayStored.length;
        int numMatches = 0;
        if (lengthInputArray <= lengthStoredArray)
        {
            for (int i = 0; i < lengthInputArray; i++)
            {
                if (charArrayInput[i] == charArrayStored[i])
                {
                    numMatches++;
                }
            }
            float propMatchFrontToBack = (float)(numMatches) / (float)(lengthStoredArray);
            
            numMatches = 0;
            
            for (int i = lengthInputArray - 1, j = 1; i >= 0; i--, j++)
            {
                if (charArrayInput[i] == charArrayStored[lengthStoredArray - j])
                {
                    numMatches++;
                } 
            }
            float propMatchBackToFront = (float)(numMatches) / (float)(lengthStoredArray);

            if (propMatchFrontToBack <= propMatchBackToFront)
            {
                return propMatchBackToFront;
            } 
            else
            {
                return propMatchFrontToBack;
            }
        }
        else
        {
            for (int i = 0; i < lengthStoredArray; i++)
            {
                if (charArrayStored[i] == charArrayInput[i])
                {
                    numMatches++;
                }
            }
            float propMatchFrontToBack = (float)(numMatches) / (float)(lengthInputArray);
            
            for (int i = lengthStoredArray - 1, j = 1; i >= 0; i--, j++)
            {
                if (charArrayStored[i] == charArrayInput[lengthInputArray - j])
                {
                    numMatches++;
                } 
            }
            float propMatchBackToFront = (float)(numMatches) / (float)(lengthInputArray);
            
            if (propMatchFrontToBack <= propMatchBackToFront)
            {
                return propMatchBackToFront;
            } 
            else
            {
                return propMatchFrontToBack;
            }
        }
    }    
}
