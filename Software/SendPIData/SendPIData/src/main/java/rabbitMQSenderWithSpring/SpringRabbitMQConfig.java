package rabbitMQSenderWithSpring;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringRabbitMQConfig
{
  @Bean
  public DirectExchange exchange()
  {
    return new DirectExchange("direct_messages");
  }
  
  @Bean
  public CachingConnectionFactory connectionFactory()
  {
    CachingConnectionFactory conn = new CachingConnectionFactory();
    conn.setHost("localhost");
    conn.setPort(5672);
    conn.setUsername("test");
    conn.setPassword("test");
    return conn;
  }
  
  @Bean
  public RabbitTemplate templateGet()
  {
    RabbitTemplate template = new RabbitTemplate();
    template.setConnectionFactory(connectionFactory());
    return template;
  }
  
  @Bean
  public DataInfoSender procesorMessage()
  {
    return new DataInfoSender();
  }
}
