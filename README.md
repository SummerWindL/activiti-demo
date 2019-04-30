# activiti-demo
activiti


## 初始化表

```
public class InitTable {

	@Test
	public void createTable() {
	  // 创建一个数据源
	  DriverManagerDataSource dataSource = new DriverManagerDataSource();
	  dataSource.setDriverClass("com.mysql.jdbc.Driver");
	  dataSource.setJdbcUrl("jdbc:mysql://192.168.0.108:3306/activiti-demo?useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8");
	  dataSource.setUser("root");
	  dataSource.setPassword("root");

	  // 创建流程引擎配置
	  ProcessEngineConfiguration configuration = ProcessEngineConfiguration
	      .createStandaloneInMemProcessEngineConfiguration();
	  // 设置数据源
	  //    configuration.setDataSource(dataSource);
	  // 如果不使用数据源, 可以通过配置连接信息来连接数据库
	  configuration.setJdbcDriver("com.mysql.jdbc.Driver");
	  configuration.setJdbcUrl("jdbc:mysql://192.168.0.108:3306/activiti-demo?useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8");
	  configuration.setJdbcUsername("root");
	  configuration.setJdbcPassword("root");

	  // 设置创建表的一个规则,有三种
	  // DB_SCHEMA_UPDATE_FALSE = "false" 如果数据库里没有acti相关的表, 也不会创建
	  // DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop" 不管数据库里有没acti的相关表, 都会先删除旧表再创建新表, 不推荐在生产中使用
	  // DB_SCHEMA_UPDATE_TRUE = "true" 如果数据库里没有acti相关的表, 会自动创建
	  // 仔细看看, 是不是有些类似于hibernate里的ddl-auto :)
	  configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP);

	  // 构建流程引擎, 这一步就会创建好表, 但基本上表内都是空的, 因为还没有部署, 再没有流程实例
	  ProcessEngine processEngine = configuration.buildProcessEngine();
	  // 可以获取流程引擎的一些信息, 不过这个东西没啥用..
	  System.out.println(processEngine.getName());
	}
	
	// 获取流程引擎
	private ProcessEngine getProcessEngine() {
	  // 创建流程引擎配置
	  ProcessEngineConfiguration configuration = ProcessEngineConfiguration
	      .createStandaloneInMemProcessEngineConfiguration();
	  // 设置数据源
	  //    configuration.setDataSource(dataSource);
	  // 如果不使用数据源, 可以通过配置连接信息来连接数据库
	  configuration.setJdbcDriver("com.mysql.jdbc.Driver");
	  configuration.setJdbcUrl("jdbc:mysql://192.168.0.108:3306/activiti-demo?useSSL=false&characterEncoding=utf8&serverTimezone=GMT%2B8");
	  configuration.setJdbcUsername("root");
	  configuration.setJdbcPassword("root");

	  // 设置创建表的一个规则,有三种
	  // DB_SCHEMA_UPDATE_FALSE = "false" 如果数据库里没有acti相关的表, 也不会创建
	  // DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop" 不管数据库里有没acti的相关表, 都会先删除旧表再创建新表, 不推荐在生产中使用
	  // DB_SCHEMA_UPDATE_TRUE = "true" 如果数据库里没有acti相关的表, 会自动创建
	  // 我这是做测试, 就选择每次先删除旧的表再创建新的表的规则了
	  // 仔细看看, 是不是有些类似于hibernate里的ddl-auto :)
	  configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

	  // 构建流程引擎, 这一步就会创建好表, 但基本上表内都是空的, 因为还没有部署, 再没有流程实例
	  ProcessEngine processEngine = configuration.buildProcessEngine();
	  return processEngine;
	}
	
	@Test
	public void activitiApi() {
	  ProcessEngine processEngine = getProcessEngine();
	  RepositoryService repositoryService = processEngine.getRepositoryService();
	  FormService formService = processEngine.getFormService();
	  HistoryService historyService = processEngine.getHistoryService();
	  IdentityService identityService = processEngine.getIdentityService();
	  ManagementService managementService = processEngine.getManagementService();
	  RuntimeService runtimeService = processEngine.getRuntimeService();
	  TaskService taskService = processEngine.getTaskService();
	}
	
	@Test
	public void deployProcess() {
	  ProcessEngine processEngine = getProcessEngine();
	  RepositoryService repositoryService = processEngine.getRepositoryService();
	  Deployment deploy = repositoryService.createDeployment()
	      // 给流程起一个名字
	      .name("请假流程")
	      // 添加流程图资源文件
	      .addClasspathResource("AskLeave.bpmn")
	      // 添加流程图片资源文件
	      .addClasspathResource("AskLeave.png")
	      // 部署
	      .deploy();
	  System.out.println("ID: " + deploy.getId());
	}
	
	@Test
	public void startProcess() {
	  ProcessEngine processEngine = getProcessEngine();
	  RuntimeService runtimeService = processEngine.getRuntimeService();
	  // 在画流程图的时候,给流程图起的名字
	  String processDefinitionKey = "AskLeave";
	  // 业务逻辑中的id
	  String businessKey = "1";
	  ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
	  System.out.println("ID: " + instance.getId());
	}

}
```
