package com.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.activiti.util.JsonAdaptor;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseApplicationTest {

	@Autowired(required=true)
    protected JsonAdaptor jsonAdaptor;

	@Autowired(required=true)
	protected RuntimeService runtimeService;
	@Autowired(required=true)
	protected TaskService taskService;
	
	@Autowired
	protected IdentityService identityService;
	
	protected List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	@Before
	public void init() {
	  for (int i = 0; i < 10; i++) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("id", i + 1);
	    map.put("username", "员工" + i);
	    map.put("title", "请假" + i);
	    map.put("day", i);
	    data.add(map);
	  }
	}

    @Test
    public void contextLoads() {
    }


    public void printResult(Object obj) {
        if (obj != null) {
            String str = "";
            try {
                str = jsonAdaptor.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                System.out.println("json解析错误");
            }
            System.out.println("测试结果：" + str);
        } else {
            System.out.println("测试结果为空");
        }
    }
}
