package com.sender.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
public class SendMain
{
	
  public static void main(String[] args)
    throws Exception
  {
    while(true)
    {
      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(new Class[] { RabbitConfiguration.class });
      DataInfoSender dataInfoSender= (DataInfoSender)context.getBean("procesorMessage");
      
      dataInfoSender.sendData();
      Thread.sleep(1000);
    }
  }
}
