import com.message.BlogUserMessage;
import com.message.Config.WebSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogUserMessage.class)
public class TestWebSocket {

    @Autowired
    WebSocket webSocket;

    @Test
    public void testWebSocket() throws Exception{
        for (int i = 0; i < 10; i++){
            String userId=Integer.toString(i);
            Thread.sleep(1000);
            webSocket.sendTextMessage(userId, ""+i);
        }
    }
}
