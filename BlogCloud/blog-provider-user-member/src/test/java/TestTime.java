import com.member.BlogUserMember;
import com.member.producter.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogUserMember.class)
public class TestTime {

    @Autowired
    Sender sender;

    //direct测试
    @Test
    public void testDirectMember(){
        sender.directSend();
    }

    //fanout测试
    @Test
    public void testFanoutMember(){
        sender.fanoutSend();
    }

    //topic测试
    @Test
    public void testTopicMember(){
        sender.topicSend();
    }

    //header测试
    @Test
    public void testHeaderMember(){
        sender.sendHeader();
    }
}
