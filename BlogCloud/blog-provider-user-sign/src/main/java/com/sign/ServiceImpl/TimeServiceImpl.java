package com.sign.ServiceImpl;

import com.sign.Service.TimeService;
import com.sign.util.SignDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



@Service
public class TimeServiceImpl implements TimeService {

    //失败
    private static final  int FAILED=0;
    //成功
    private static final int SUCCESS=1;

    //控制台输出看时间
    private Logger logger = LoggerFactory.getLogger(TimeServiceImpl.class);

    //设置时间凭证
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int setTime(Long id){
        //先判定是否redis中是否存在限时凭证
        Jedis jedis = new Jedis("localhost", 6379);
        if (jedis.get("Sign-"+id) == null) {
            //获取明天0点的时间并且设置限时凭证
            try {
                //与明日凌晨的时间秒数差
                int seconds= SignDate.getSeconds().intValue();
                //设置redis中的签到过期时间
                jedis.set("Sign-"+id, "今天签到已经完成!");
                jedis.expire("Sign-"+id, seconds);
                return SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return FAILED;
    }
}