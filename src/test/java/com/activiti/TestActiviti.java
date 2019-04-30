package com.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestActiviti {
	
	@Autowired
	private RuntimeService runtimeService;
	
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
	  // 用户1发起了一个请假流程
//	  ProcessInstance instance = runtimeService.startProcessInstanceByKey("AskLeave", "1");
	  //创建一个Map存放变量
	  Map<String,Object> params = new HashMap<>();
	  params.put("username", "user1");
	  //启动流程时调用的方法是三个参数，第三个参数是用来存放变量
	  ProcessInstance instance = runtimeService.startProcessInstanceByKey("AskLeave", "1",params);
	  System.out.println("Id: " + instance.getId());
	}
	
	@Autowired
	private TaskService taskService;

	@Test
	public void queryTask() {
	  List<Task> tasks = taskService.createTaskQuery().taskAssignee("user1")
	    // 分页查询
	    // .listPage(firstResult, maxResults)
	    // 排序
	    // .orderByTaskCreateTime().desc()
	    // 如果你知道这个查询是一条记录的话, 可以使用 .singleResult() 方法来获取单一的记录
	    // .singleResult()
	    .list();
	  for (Task task : tasks) {
	    System.out.println(task.toString()); // Task[id=2505, name=提交请假]
	  }
	}
	
	@Test
	public void completeTask() {
		// 通过查询可以拿到user2的任务id是7502
		  String taskId = "17505";
		  // 选通过taskId查询任务
		  Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		  // 从任务里拿到流程实例id
		  String processInstanceId = task.getProcessInstanceId();
		  // 批注信息
		  String message = "同意";
		  Authentication.setAuthenticatedUserId("user1");
		  // 给任务添加批注
		  taskService.addComment(taskId, processInstanceId, message);
		  // 处理任务
		  taskService.complete(taskId);
	}
	
	@Test
	public void queryTaskComment() {
	  List<Task> tasks = taskService.createTaskQuery().taskAssignee("user1")
	      // 分页查询
	      // .listPage(firstResult, maxResults)
	      // 排序
	      // .orderByTaskCreateTime().desc()
	      .list();
	  for (Task task : tasks) {
	    List<Comment> comments = taskService.getProcessInstanceComments(task.getProcessInstanceId());
	    System.out.println("任务ID: " + task.getId());
	    for (Comment comment : comments) {
	      System.out.println("批注人: " + comment.getUserId());
	      System.out.println("批注信息: " + comment.getFullMessage());
	      System.out.println("批注时间: " + comment.getTime());
	    }
	    System.out.println("-------------------------------");
	  }
	}
	
	@Test
	public void getBusinessKey() {
	  // user2 处理了任务, 任务转到 user3 任务id也变成了 10003, 可以使用user3来查询任务获取到
	  String taskId = "20005";
	  // 通过任务id来查询任务
	  Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
	  // 通过任务里的流程实例id获取当前任务所属的流程实例信息
	  ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(task
	      .getProcessInstanceId()).singleResult();
	  // 从流程实例信息中拿到 businessKey
	  String businessKey = instance.getBusinessKey();
	  System.out.println("businessKey: " + businessKey);
	}
	
}
