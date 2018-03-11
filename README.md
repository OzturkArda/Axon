# Axon Practice Purposed Project Hub


Contains following test projects;

  - monolithicAxonExample
  - twoServicesAxonExample

## monolithicAxonExample

This program is design to provide basic understanding of implemanting Axon framework to a Spring Project.

Key components:
  - [com.tower.axonbank.Account.java][df1] - Axon Aggregator Entity class 
  - [ com.tower.axonbank.coreapi.AccountApi.kt ][df1] - Contains Comman and Event class definitions
  - [com.tower.axonbank.Application.java][df1] - Example for basic manuel Axon configuration
  - [com.tower.axonbank.AxobankApplication.java][df1] - Axon configurations via annotations
  

Basic configuration options can be tested via Application.java class. AxobankApplication class can also be altered to change some configuration like using a synchron or asynchron messaging by overwriting beans from Spring context.

Axon dependencies:
```sh
    <properties>
		<axon.version>3.1.3</axon.version>
	</properties>

	<dependency>
		<groupId>org.axonframework</groupId>
		<artifactId>axon-test</artifactId>
		<version>${axon.version}</version>
		<scope>test</scope>
	</dependency>
		<dependency>
		<groupId>org.axonframework</groupId>
		<artifactId>axon-spring</artifactId>
		<version>${axon.version}</version>
	</dependency>
```

## twoServicesAxonExample

### demoComplaints

This program is design to populate and also list "Complaint" entity instances. Built-in Axon configurations are used to achieve this. Also [HSQLDB][df1] is used for date persistancey.

Key components:
  - [com.tower.democomplaints.ComplaintFiledEvent.java][df1] - Event definition to applied after FileComplaintCommand 
  - [ com.tower.democomplaints.ComplaintQueryObjectRepository.java ][df1] - Interface for providind JPA persitencey
  - [com.tower.democomplaints.DemoComplaintsApplication.java][df1] - The main class which serves both starting point of th program and also contains all other Axon related configurations, entities and method definitions.
  - [src\main\resources\application.properties][df1] - Contains Rabbitmq Exchange configuration of Axon

Streaming event to a remote exchange/queu is provided by adding following codes to [DemoComplaintsApplication.java][df1] :
```sh
    @Bean
	public Queue queue(){
		return QueueBuilder.durable("ComplaintEvents").build();
	}

	@Bean
	public Exchange exchange(){
		return ExchangeBuilder.fanoutExchange("ComplaintEvents").build();
	}

	@Bean
	public Binding binding(){
		return BindingBuilder.bind(queue()).to(exchange()).with("*").noargs();
	}

	@Autowired
	public void configure(AmqpAdmin admin){
		admin.declareQueue(queue());
		admin.declareExchange(exchange());
		admin.declareBinding(binding());
	}
```	
Axon dependencies:
```sh
       <dependency>
            <groupId>org.axonframework</groupId>
            <artifactId>axon-spring-boot-starter</artifactId>
            <version>3.1.3</version>
        </dependency>

		<dependency>
			<groupId>org.axonframework</groupId>
			<artifactId>axon-amqp</artifactId>
			<version>3.1.3</version>
		</dependency>
```		

To stream data you also need a running RabbitMQ instance on your machine. You can either install [RabbitMQ][rabbit] or start a related docker instance.

Sample Complaint generation request:
```sh
curl -H "Content-Type:application/json" -d '{"company":"apple","description":"Broken Speaker is not cool!"}' localhost:8080 
```	



### demoComplaintsStatistics

This program is design to comsume events that streamed from demo-complaints program. Simple event and application.properties definitions enable us to consume queued evetns from local RabbitMQ exchange.

Key components:
  - [com.tower.democomplaints.ComplaintFiledEvent.java][df1] - This event directly copied from demo-complaint service including the path it is stored
  

   [df1]: <http://daringfireball.net/projects/markdown/>
   [rabbit]:<https://www.rabbitmq.com/install-windows.html>
  
