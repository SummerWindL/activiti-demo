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
public class CandidateTest {
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
	
	/**
	 * 启动流程
	 */
	@Test
	public void startProcess() {
	  ProcessInstance instance = runtimeService.startProcessInstanceByKey("CandidateProcess");
	  System.out.println(instance.getId());
	}
	
	/**
	 * 查询
	 */
	@Test
	public void queryTaskByCandidateUser() {
	  // 查询候选人user1的任务, 使用user2, user3 都可以查到这条任务
	  List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("user1")
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
	  }
	}
	
	/**
	 * 分配任务，也可以是候选人自己领取任务, 就是给任务设置一个代理人
	 */
	@Test
	public void claimTask() {
	  // 上面查询到user1的候选任务id为2505
	  String taskId = "10005";
	  // 将2505任务分配给user1处理
	  taskService.claim(taskId, "user1");
	}
	
	/**
	 * 在使用代理人查询就可以查询到任务
	 */
	@Test
	public void queryTask() {
	  List<Task> tasks = taskService.createTaskQuery().taskAssignee("user1")
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
	  }
	}
//	任务ID: 10005
//	代理人: user1
//	任务名: User Task
	
	/**
	 * 放弃任务，当放弃任务后, 这时候又可以使用 user1, user2, user3 去查询候选人任务了
	 */
	@Test
	public void claimTaskUndo() {
	  // 上面查询到user1的候选任务id为2505
	  String taskId = "10005";
	  // 将2505任务分配给user1处理
	  // taskService.claim(taskId, "user1");
	  // 放弃任务
	  taskService.setAssignee(taskId, null);
	}
}
