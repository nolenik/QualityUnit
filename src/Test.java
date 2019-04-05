import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Test {

	final static int MAX_COUNT_S = 100000;
    final static int MAX_SERVICES = 10;
    final static int MAX_VARIATIONS = 3;
    final static int MAX_QUESTIONS_TYPE = 10;
    final static int MAX_CATEGORIES = 20;
    final static int MAX_SUB_CATEGORIES = 5;
    final static String WAITING = "C";
    final static String QUERY = "D";
    final static String FIRST_ANSWER = "P";
    final static String NEXT_ANSWER = "N";
	
	
	public static class Waiting 
    {
    	String type,service,question,answer,date,time;
    	
    	public Waiting(String type, String service, String question, String answer, String date, String time)
    	{
    		this.type=type;
    		this.service=service;
    		this.question=question;
    		this.answer=answer;
    		this.date=date;
    		this.time=time;
    	}
    }
	public static class Query
    {
    	String type,service,question,answer,date;
    	
    	public Query(String type, String service, String question, String answer, String date)
    	{
    		this.type=type;
    		this.service=service;
    		this.question=question;
    		this.answer=answer;
    		this.date=date;
    	}
    }
	
	// argument ¹: 0 - type, 1 - service, 2 - question, 3 - answer, 4 - date, 5 - time
	public static List<String> calculate(List<String> list) throws ParseException
	{
		List<Waiting> waiting = new ArrayList<>();
		List<Query> query = new ArrayList<>();
		List<String> result = new ArrayList<>(); 
	    for (String s:list)
	    {
	    	String[] arr = s.split(" ");
	    	
	    	//check services for correctness
	    	if (arr[1].indexOf(".")!=-1)
	    	{
	    		String[]tmp = arr[1].split("\\.");
	    	    if (Integer.parseInt(tmp[0])>MAX_SERVICES || Integer.parseInt(tmp[1])>MAX_VARIATIONS)
	    	    	continue;
	    	}
	    	else
	    	{
	    		
	    		if (Integer.parseInt(arr[1]) > MAX_SERVICES)
	    		{
	    			
        		    continue;
	    		}
	    	}
    	    //check questions for correctness
	    	if (arr[2].indexOf(".")!=-1)
	    	{
	    		String[]tmp = arr[2].split("\\.");
	    		if (tmp.length>0 && Integer.parseInt(tmp[0])>MAX_QUESTIONS_TYPE)
	    			continue;
	    		if (tmp.length>1 && Integer.parseInt(tmp[1])>MAX_CATEGORIES)
	    			continue;
	    		if (tmp.length>2 && Integer.parseInt(tmp[2])>MAX_SUB_CATEGORIES)
	    			continue;
	    	}
	    	else
	    	{
	    		if(!arr[2].equals("*"))
	    		    if (Integer.parseInt(arr[2])>MAX_QUESTIONS_TYPE)
    		            continue;
	    	}
	    	//check answers for correctness
	    	if (!arr[3].equals(FIRST_ANSWER) && !arr[3].equals(NEXT_ANSWER))
	    		continue;
	    	//check type of string for correctness
            if (!arr[0].equals(WAITING) && !arr[0].equals(QUERY))
            {
            	continue;
            }
            else
            {
            	if(arr[0].equals(WAITING))
            		waiting.add(new Waiting(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5]));
            	else
            		query.add(new Query(arr[0],arr[1],arr[2],arr[3],arr[4]));
            }	
	    }
	    
	    
	    for (Query q:query)
	    {
	    	String serviceId,qServiceId,subService=null,qSubService=null,questionId,qQuestionId,questionCategory=null,qQuestionCategory=null,questionSubCategory=null,qQuestionSubCategory=null;
	    	
	    	String[] services, qServices,questions, qQuestions;
	    	Date dateFrom,dateTo = null;
	    	int subTotal=0, count=0,total=0;
	    	for (Waiting w:waiting)
	    	{
	    		if(w.service.indexOf(".")!=-1)
	    		{
	    			services = w.service.split("\\.");
	    			serviceId=services[0];
	    			subService=services[1];
	 
	    		}
	    		else
	    		{
	    			serviceId=w.service;
	    		}
	    		if(q.service.indexOf(".")!=-1)
	    		{
	    			qServices = q.service.split("\\.");
	    			qServiceId=qServices[0];
	    			qSubService=qServices[1];
	    		}
	    		else
	    		{
	    			qServiceId=q.service;
	    		}
	    		if (w.question.indexOf(".")!=-1)
	    		{
	    			questions = w.question.split("\\.");
	    			questionId=questions[0];
	    			questionCategory = questions[1];
	    			if (questions.length==3)
	    				questionSubCategory = questions[2];
	    		}
	    		else
	    		{
	    			questionId=w.question;
	    		}
	    		if (q.question.indexOf(".")!=-1)
	    		{
	    			qQuestions = q.question.split("\\.");
	    			qQuestionId=qQuestions[0];
	    			qQuestionCategory = qQuestions[1];
	    			if (qQuestions.length==3)
	    				questionSubCategory = qQuestions[2];
	    		}
	    		else
	    		{
	    			qQuestionId=q.question;
	    		}
	    	
	    		if((serviceId.equals(qServiceId) && (subService.equals(qSubService) || qSubService==null)) || qServiceId.equals("*"))
	    		{
	    			if((questionId.equals(qQuestionId) && (questionCategory.equals(qQuestionCategory) || qQuestionCategory==null) && (questionSubCategory.equals(qQuestionSubCategory) || qQuestionSubCategory==null)) || qQuestionId.equals("*"))
	    			{
	    				if(q.date.indexOf("-")!=-1)
	    				{
	    					String[] date = q.date.split("\\-");
	    					dateFrom = new SimpleDateFormat("dd.MM.yyy").parse(date[0]);
	    					dateTo = new SimpleDateFormat("dd.MM.yyy").parse(date[1]);
	    				}
	    				else
	    				{
	    					dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse(q.date);
	    				}
	    				if (dateTo!=null)
	    				{
	    				    if (new SimpleDateFormat("dd.MM.yyy").parse(w.date).before(dateTo) && new SimpleDateFormat("dd.MM.yyy").parse(w.date).after(dateFrom))
	    				    {
	    					    subTotal+=Integer.parseInt(w.time);
	    					    count++;
	    					    
	    				    }
	    				}
	    				else
	    				{
	    					if (new SimpleDateFormat("dd.MM.yyy").parse(w.date).after(dateFrom))
	    				    {
	    					    subTotal+=Integer.parseInt(w.time);
	    					    count++;
	    					
	    				    }
	    				}
	    			}
	    		}
	    		
	    	}
	    	total=(subTotal == 0 ? 0 : subTotal/count);
    		result.add(total==0 ? "-" : Integer.toString(total));
	    }
	    return result;
	}
	public static void main(String[] args) throws FileNotFoundException, ParseException{
		// TODO Auto-generated method stub
		Scanner scanner=new Scanner(new File("test.txt"));
		List<String> list=new ArrayList<>();
		 while(scanner.hasNextLine()){
		     list.add(scanner.nextLine()); 

		 }
		 if(Integer.parseInt(list.get(0))>MAX_COUNT_S)
			 return;
		 list.remove(0);
		System.out.println(calculate(list));
		 //System.out.println();
	}
    
}
