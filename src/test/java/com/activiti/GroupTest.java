package com.activiti;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupTest {

	@Autowired
	private IdentityService identityService;
	
	/**
	 * 创建组
	 */
	@Test
	public void createGroup() {
	  Group group = identityService.newGroup("1");
	  group.setName("研发部");
	  Group group1 = identityService.newGroup("2");
	  group1.setName("运营部");
	  Group group2 = identityService.newGroup("3");
	  group2.setName("商务部");
	  identityService.saveGroup(group);
	  identityService.saveGroup(group1);
	  identityService.saveGroup(group2);
	}


	/**
	 * 创建用户
	 */
	@Test
	public void createUser() {
	  User user = identityService.newUser("1");
	  user.setFirstName("三");
	  user.setLastName("张");
	  user.setEmail("zhangsan@aa.com");
	  User user1 = identityService.newUser("2");
	  user1.setFirstName("四");
	  user1.setLastName("李");
	  user1.setEmail("lisi@aa.com");
	  User user2 = identityService.newUser("3");
	  user2.setFirstName("五");
	  user2.setLastName("王");
	  user2.setEmail("wangwu@aa.com");
	  User user3 = identityService.newUser("4");
	  user3.setFirstName("六");
	  user3.setLastName("赵");
	  user3.setEmail("zhaoliu@aa.com");

	  identityService.saveUser(user);
	  identityService.saveUser(user1);
	  identityService.saveUser(user2);
	  identityService.saveUser(user3);
	}
	
	/**
	 * 创建关联
	 */
	@Test
	public void createAssociationUser() {
	  // void createMembership(String userId, String groupId);
	  // 第一个参数是userId, 第二个参数是 groupId
	  identityService.createMembership("1", "1");
	  identityService.createMembership("2", "1");
	  identityService.createMembership("3", "2");
	  identityService.createMembership("4", "3");
	}
	
	/**
	 * 删除用户/组
	 */
	@Test
	public void deleteTest() {
	  identityService.deleteUser("1");
	  identityService.deleteGroup("1");
	}
	
	/**
	 * 查询
	 */
	//查询用户
	@Test
	public void queryUser() {
	  List<User> users = identityService.createUserQuery().list();
	  for (User user : users) {
	    System.out.println("userName: " + user.getLastName() + user.getFirstName() + " email: " + user.getEmail());
	  }
	}
	
	//查询组
	@Test
	public void queryGroup() {
	  List<Group> groups = identityService.createGroupQuery().list();
	  for (Group group : groups) {
	    System.out.println("id: " + group.getId() + " name: " + group.getName());
	  }
	}
	
	//关联查询
	@Test
	public void queryMemberShip() {
	  // 查询id为1的组里关联的用户
	  List<User> users = identityService.createUserQuery().memberOfGroup("1").list();
	  for (User user : users) {
	    System.out.println("userName: " + user.getLastName() + user.getFirstName() + " email: " + user.getEmail());
	  }
	  System.out.println("======================================");
	  // 查询id为1的用户所在的组
	  List<Group> groups = identityService.createGroupQuery().groupMember("1").list();
	  for (Group group : groups) {
	    System.out.println("id: " + group.getId() + " name: " + group.getName());
	  }
	}
}
