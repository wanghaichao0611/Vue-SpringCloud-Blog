import com.article.Config.RedisSonConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisSonConfig.class)
public class TestRedisSon {

    //看下Redisson运行日志
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisSonConfig.class);

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testRedisSon() throws Exception{
        LOGGER.info("redisson的配置"+redissonClient.getConfig().toYAML());
    }
}
