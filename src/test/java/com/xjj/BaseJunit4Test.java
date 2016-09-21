package com.xjj;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringJUnit4ClassRunner.class)  //使用Spring Junit4进行测试
@RunWith(SpringRunner.class)
@ContextConfiguration ("classpath:/spring/*.xml") //加载配置文件
public abstract class BaseJunit4Test {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
}
