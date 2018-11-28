package myLambdaFunction;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;


public class Csye6225LambdaFunction implements RequestHandler<SNSEvent,Object>{


    public Object handleRequest(SNSEvent request, Context context)
    {
        String time=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        context.getLogger().log("Invocation started: "+time);
        context.getLogger().log("Number of records: "+request.getRecords().size());
        context.getLogger().log("Record Message: "+request.getRecords().get(0).getSNS().getMessage());

        String snsMessage= request.getRecords().get(0).getSNS().getMessage();
        String userEmail=null;
        String userPassToken=null;

        try
        {
            userEmail = snsMessage.split(":")[0];
            userPassToken = snsMessage.split(":")[1];

        }catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        //Set awsCredential
        AWSCredentialsProvider awsCredentialsProvider= new ClasspathPropertiesFileCredentialsProvider();
        AWSCredentials awsCredentials=awsCredentialsProvider.getCredentials();


        // Set a DynamoDBClient and a SESClient
        AmazonDynamoDB mydb= AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
        AmazonSimpleEmailService emailSender= AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(Regions.US_EAST_1).build();


        //Set a send email request
        String FROM="cheng.li@husky.neu.edu";
        String TO=userEmail;
        String BODY="http://csye6225-fall2018-chengli.me/reset?email="+userEmail+"$token="+userPassToken;
        String SUBJECT="RestPasswordLink";
        Destination destination=new Destination().withToAddresses(TO);
        Content subject = new Content().withData(SUBJECT);
        Content textBody= new Content().withData(BODY);
        Body body= new Body().withText(textBody);
        Message message= new Message().withSubject(subject).withBody(body);
        SendEmailRequest emailRequest= new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);


        //Set a get item request
        HashMap<String,AttributeValue> key_to_get=new HashMap<String, AttributeValue>();
        key_to_get.put("userName",new AttributeValue(userEmail));
        GetItemRequest getItemRequest= new GetItemRequest().withKey(key_to_get).withTableName("UserTable");


        //Set a put new item request
        HashMap<String, AttributeValue> item_value= new HashMap<String, AttributeValue>();
        item_value.put("userName",new AttributeValue(userEmail));
        item_value.put("password",new AttributeValue(userPassToken));
        Long timeToLive= Instant.now().getEpochSecond()+1200;
        item_value.put("TimeToLive",new AttributeValue(timeToLive.toString()));

        try
        {
            Map<String,AttributeValue> item=mydb.getItem(getItemRequest).getItem();

            if(item==null)
            {
                mydb.putItem("UserTable",item_value);
                System.out.println("uploaded new active user");
                System.out.println("Sending ResetPassword link to "+userEmail);
                emailSender.sendEmail(emailRequest);
                System.out.print("Successfully sent to "+userEmail);
            }
            else
            {
                System.out.println("Duplicate request from user "+userEmail);
            }

        }
        catch(Exception e)
        {

            System.out.println(e.getMessage()+" "+e.toString());
        }

        return null;

    }



}