import com.aliyun.sms.MessageSendException;
import com.aliyun.sms.MessageSender;

public class TestMessageHandler {

    public static void main(String[] args) throws MessageSendException {
        MessageSender sender = MessageSender.newMessageSender("5b4095f2955341ec855dc584883756f7",
                "LTAI4FiWcYe6DYpJhM3fFYXY", "eUQ1AhxHB3c0I3H9UwJLlgUgf5gH5D");
        boolean response = sender.signName("特惠学", "SMS_177246466")
                .sendMessage("13909181850", "12345");
        System.out.println(response);
    }

}
