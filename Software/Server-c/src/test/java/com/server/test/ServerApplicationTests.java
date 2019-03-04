package com.server.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.database.repositories.CircuitRepository;
import com.server.entites.Consumer;
import com.server.entites.Outlet;
import com.server.entites.Sensor;
import com.server.entites.Switch;
import com.server.processing.receiver.ReceiverFunctions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerApplicationTests {

	@Autowired
	ReceiverFunctions receiverFunctions;

	@Autowired
	CircuitRepository circuitRepository;

	ObjectMapper mapper = new ObjectMapper();

	@Test
	public void contextLoads() {

	}
	
	@Test
	public void testOutletReceiverFunctions() {
		
		String outletAsString = null;
			
		Consumer outlet = new Outlet();
		outlet.setPowerConsumed(0.2);
		outlet.setState(1);
		outlet.setName("outlet1");
		
		try {
			
		outletAsString=mapper.writeValueAsString(outlet);
		
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals( "Consumer message received -> outlet.", receiverFunctions.consumerDataProcess(outletAsString.getBytes()));
	}
	
	@Test
	public void testSwitchReceiverFunctions() {
		
		String switcherAsString = null;
		
		Consumer switcher = new Switch();
		switcher.setPowerConsumed(55.6);
		switcher.setState(1);
		switcher.setName("switch1");
	
		try {
		
		switcherAsString=mapper.writeValueAsString(switcher);
		
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		assertEquals( "Consumer message received -> switch.", receiverFunctions.consumerDataProcess(switcherAsString.getBytes()));	
	}
	
	@Test
	public void testSensorReceiverFunctions() {
		
		String sensorAsString = null;
		
		Sensor sensor = new Sensor();
		sensor.setName("sensor1");
		sensor.setPowerConsumed(2.0);
		sensor.setState(1);
		sensor.setType("movement");
		
		try {
		
		sensorAsString=mapper.writeValueAsString(sensor);
		System.out.println(sensorAsString);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals( "Sensor message received -> movement.", receiverFunctions.sensorDataProcess(sensorAsString.getBytes()));	
	}

}
