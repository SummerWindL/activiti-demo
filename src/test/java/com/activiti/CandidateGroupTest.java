package com.activiti;

import java.util.List;

import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class CandidateGroupTest extends BaseApplicationTest{

	/**
	 * 启动流程
	 */
	@Test
	public void startProcess() {
	  ProcessInstance instance = runtimeService.startProcessInstanceByKey("CandidateGroupProcess");
	  System.out.println(instance.getId());
	}
	
	@Test
	public void queryTaskByCandidateGroup() {
	  List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("1")
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
//	任务ID: 17505
//	代理人: null
//	任务名: User Task
	
//	任务ID: 20005
//	代理人: null
//	任务名: User Task	
	
	/**
	 * 分配组任务
	 */
	@Test
	public void claimTask() {
	  // 上面查询到user1的候选任务id为12524
	  String taskId1 = "17505";
	  // 将12524任务分配给userId为1的用户所在的组处理
	  taskService.claim(taskId1, "1");
	  // 放弃任务
	  //    taskService.setAssignee(taskId, null);
	  
	  // 上面查询到user1的候选任务id为12524
	  String taskId2 = "20005";
	  // 将12524任务分配给userId为1的用户所在的组处理
	  taskService.claim(taskId2, "2");
	  // 放弃任务
	  //    taskService.setAssignee(taskId, null);
	}
	
	/**
	 * 查询组任务
	 */
	@Test
	public void queryTask() {
	  List<Task> tasks = taskService.createTaskQuery().taskAssignee("2")
	      // 分页查询
	      // .listPage(firstResult, maxResults)
	      // 排序
	      // .orderByTaskCreateTime().desc()
	      .list();
	  for (Task task : tasks) {
	    String assignee = task.getAssignee();
	    User user = identityService.createUserQuery().userId(assignee).singleResult();
	    System.out.println("任务ID: " + task.getId());
	    System.out.println("代理人ID: " + task.getAssignee());
	    System.out.println("代理人名: " + user.getLastName() + user.getFirstName());
	    System.out.println("任务名: " + task.getName());
	    System.out.println("-------------------------------");
	  }
	}
//	任务ID: 17505
//	代理人ID: 1
//	代理人名: 张三
//	任务名: User Task
	
//	任务ID: 20005
//	代理人ID: 2
//	代理人名: 李四
//	任务名: User Task
}
