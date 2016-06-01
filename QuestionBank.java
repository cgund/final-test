
import java.io.*;
import java.util.*;
import java.security.*;
/*
Encapsulates category, question, and correct answer
*/
public class QuestionBank 
{
    private static final int QUESTIONS_PER_SUBJECT = 5;
    
    /*
    Selects random questions for test
    */
    public List<NodeQuestionAnswer> selectRandom()
    {
        List<NodeQuestionAnswer> lstRandom = new ArrayList<>();
        
        Map<String,List<NodeQuestionAnswer>> map = this.create();
        Set<String> subjects = map.keySet();
        Iterator<String> iterator = subjects.iterator();
        while (iterator.hasNext())
        {
            String subject = iterator.next();
            List<NodeQuestionAnswer> lstSubjectQA = map.get(subject);
            int size = lstSubjectQA.size();
            /*Works with questionBank.txt.  But would enter infinite loop
            if List contained less than 5 questions
            */
            for (int i = 0; i < QUESTIONS_PER_SUBJECT; i++) 
            {
                boolean added = false;
                while (added == false)
                {
                    SecureRandom random = new SecureRandom();
                    int index = random.nextInt(size);
                    NodeQuestionAnswer question = lstSubjectQA.get(index);
                    if (!lstRandom.contains(question))
                    {
                        lstRandom.add(question);
                        added = true;
                    }
                }
            }
        }

        return lstRandom;
    }
    
    /*
    Create map data structure to map question category to list of nodes
    */
    private Map<String,List<NodeQuestionAnswer>> create()
    {
        Map<String,List<NodeQuestionAnswer>> map = new HashMap<>();

        String path = "questionBank.txt";
        File file = new File(path);
        try 
        {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext())
            {
                String line = scanner.nextLine();
                String[] parts = line.split("::");
                if (parts.length == 5)
                {
                    String subject = parts[1];
                    String category = parts[2];
                    String question = parts[3];
                    String answer = parts[4];
                    List<NodeQuestionAnswer> lstQA = new ArrayList<>();
                    if (!map.containsKey(subject))
                    {
                        lstQA.add(new NodeQuestionAnswer(subject, category, question, answer));
                        map.put(subject, lstQA);
                    }
                    else
                    {
                        lstQA = map.get(subject);
                        lstQA.add(new NodeQuestionAnswer(subject, category, question, answer));
                        map.put(subject, lstQA);
                    }
                }
            }
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("File Not Found");
        }
        
        return map;
    }
}
