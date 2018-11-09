//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.SNSEvent;
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.simpleemail.model.*;
//import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
//import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
//
//
//
//public class App {
//
//
//    public static void main(String [] args)
//    {
//        //Set awsCredential
//        AWSCredentialsProvider awsCredentialsProvider= new ClasspathPropertiesFileCredentialsProvider();
//        AWSCredentials awsCredentials=awsCredentialsProvider.getCredentials();
//
//
//        // Set a DynamoDBClient
//        AmazonDynamoDB mydb= AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
//
//        //Set a get item request
//        HashMap<String,AttributeValue> key_to_get=new HashMap<String, AttributeValue>();
//        key_to_get.put("userName",new AttributeValue("abc"));
//
//      //  key_to_get.put("password",new AttributeValue("123456"));
//        GetItemRequest getItemRequest= new GetItemRequest().withKey(key_to_get).withTableName("UserTable");
//
//
//        //Set a put new item request
//        HashMap<String, AttributeValue> item_value= new HashMap<String, AttributeValue>();
//        item_value.put("userName",new AttributeValue("leo"));
//        item_value.put("password",new AttributeValue("leo38377"));
//
//        try {
//            //mydb.putItem("UserTable", item_value);
//            Map<String, AttributeValue> item = mydb.getItem(getItemRequest).getItem();
//            //System.out.println(item.get("username")+" "+item.get("password"));
//            if(item==null)
//            {
//                System.out.println("test");
//            }
//        }
//        catch(Exception e)
//        {
//            System.out.println(e.toString()+" "+e.getMessage());
//        }
//
//
//    }
//
//}
