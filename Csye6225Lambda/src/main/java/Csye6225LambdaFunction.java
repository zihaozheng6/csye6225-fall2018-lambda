import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.Context;


public class Csye6225LambdaFunction implements RequestHandler<SNSEvent,Object>{


    public Object handleRequest(SNSEvent request, Context context)
    {
        String time=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());

        context.getLogger().log("Invocation started: "+time);
        context.getLogger().log("Number of records: "+request.getRecords().size());
        context.getLogger().log("Record Message: "+request.getRecords().get(0).getSNS().getMessage());

        time= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());

        context.getLogger().log("Invocation Complete at: "+ time);

        return null;

    }



}