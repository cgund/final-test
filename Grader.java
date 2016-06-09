package exam;

/*
Craig Gundacker
Exam 3

Abstract Grader class.
*/
import java.util.List;

interface Grader 
{
    List<NodeQuestionAnswer> gradeTest();
    Report getReport();
}
