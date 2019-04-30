package com.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestParallelGateway {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

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
	public void startProcess() {
	  ProcessInstance instance = runtimeService.startProcessInstanceByKey("OrderProcess");
	  System.out.println(instance.getId());
	}
	
	@Test
	public void queryTask() {
	  List<Task> tasks = taskService.createTaskQuery().taskAssignee("seller")
	      // 分页查询
	      // .listPage(firstResult, maxResults)
	      // 排序
	      // .orderByTaskCreateTime().desc()
	      .list();
	  for (Task task : tasks) {
	    System.out.println("任务ID: " + task.getId());
	    System.out.println("代理人: " + task.getAssignee());
	    System.out.println("任务名: " + task.getName());
	    System.out.println("-------------------------------");
	    // 任务ID: 45006
	    //代理人: seller
	    //任务名: 发货（卖家）
	  }
	}
	
	//处理卖家任务
	
	@Test
	public void completeTask() {
	  String taskId = "57502";
	  taskService.complete(taskId);
	}
	
	//卖家结束任务，买家有两条任务，付款和收货
	
	@Test
	public void queryTaskUser() {
	  List<Task> tasks = taskService.createTaskQuery().taskAssignee("user")
	      // 分页查询
	      // .listPage(firstResult, maxResults)
	      // 排序
	      // .orderByTaskCreateTime().desc()
	      .list();
	  for (Task task : tasks) {
	    System.out.println("任务ID: " + task.getId());
	    System.out.println("代理人: " + task.getAssignee());
	    System.out.println("任务名: " + task.getName());
	    System.out.println("-------------------------------");
	    // 任务ID: 45006
	    //代理人: seller
	    //任务名: 发货（卖家）
	  }
	}
}
