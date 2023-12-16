package helloworld;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class App implements RequestHandler<Object, String> {

    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);
        String json = ""+input;
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonElement state = element.getAsJsonObject().get("state");
        JsonElement reported = state.getAsJsonObject().get("reported");
        String ratio = reported.getAsJsonObject().get("ratio").getAsString();
        double dis = Double.valueOf(ratio);

        String name = reported.getAsJsonObject().get("name").getAsString();
        String name1 = String.valueOf(name);

        String uid = reported.getAsJsonObject().get("uid").getAsString();
        String userid = String.valueOf(uid);

        String uid_id = reported.getAsJsonObject().get("uid_id").getAsString();
        String userid_id = String.valueOf(uid_id);

        final String AccessKey="AKIAXQLYBILHX344COWY\n";
        final String SecretKey="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\n";
        final String topicArn="arn:aws:sns:ap-northeast-2:516184687311:distance_warning_topic";

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(AccessKey, SecretKey);
        AmazonSNS sns = AmazonSNSClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials( new AWSStaticCredentialsProvider(awsCreds) )
                .build();

        final String Amsg0 ="비었습니다!";
        final String Bmsg0 ="비었습니다!";

        final String Amsg50 = "*You need to clean trashcan A*\n" + "Used capacity is " + dis + "%";
        final String Amsg70 = "*You need to clean trashcan A*\n" + "Used capacity is " + dis + "%";
        final String Amsg90 = "*You need to clean trashcan A*\n" + "Used capacity is " + dis + "%";
        final String Bmsg50 = "*You need to clean trashcan B*\n" + "Used capacity is " + dis + "%";
        final String Bmsg70 = "*You need to clean trashcan B*\n" + "Used capacity is " + dis + "%";
        final String Bmsg90 = "*You need to clean trashcan B*\n" + "Used capacity is " + dis + "%";
        final String Afull = "*You need to clean trashcan A*\n" + "A trashcan is full now!!";
        final String Bfull = "*You need to clean trashcan B*\n" + "A trashcan is full now!!";


        final String subject = "Used Capacity";
        if(name1.equals("A")) {
            sendmessage(dis, topicArn, sns,Amsg0, Amsg50, Amsg70, Amsg90, Afull, subject);
        }
        else if(name1.equals("B")) {
            sendmessage(dis, topicArn, sns,Bmsg0, Bmsg50, Bmsg70, Bmsg90, Bfull, subject);
        }
        return subject+ " ratio = " + dis + "!";
    }

    private void sendmessage(double dis, String topicArn, AmazonSNS sns,String amsg0, String amsg50, String amsg70, String amsg90,String Afull, String subject) {
        if (dis == 50) {
            PublishRequest publishRequest = new PublishRequest(topicArn, amsg50, subject);
            PublishResult publishResponse = sns.publish(publishRequest);
        } else if (dis == 70) {
            PublishRequest publishRequest = new PublishRequest(topicArn, amsg70, subject);
            PublishResult publishResponse = sns.publish(publishRequest);
        } else if (dis == 90) {
            PublishRequest publishRequest = new PublishRequest(topicArn, amsg90, subject);
            PublishResult publishResponse = sns.publish(publishRequest);
        } else if(dis == 100){
            PublishRequest publishRequest = new PublishRequest(topicArn, Afull, subject);
            PublishResult publishResponse = sns.publish(publishRequest);
        }
        else if(dis == 0){
            PublishRequest publishRequest = new PublishRequest(topicArn, amsg0, subject);
            PublishResult publishResponse = sns.publish(publishRequest);
        }
    }
}