package com.server.test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Vector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.database.repositories.CircuitRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Outlet;
import com.server.entites.PowerSource;
import com.server.entites.Sensor;
import com.server.entites.Switch;
import com.server.processing.Database.DatabaseFunctions;
import com.server.processing.receiver.ReceiverFunctions;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerApplicationTests {

	@Autowired
	ReceiverFunctions receiverFunctions;

	@Autowired
	CircuitRepository circuitRepository;

	@Autowired
	DatabaseFunctions databaseFunctions;

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

			outletAsString = mapper.writeValueAsString(outlet);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals("Consumer message received -> outlet.",
				receiverFunctions.consumerDataProcess(outletAsString.getBytes()));
	}

	@Test
	public void testSwitchReceiverFunctions() {

		String switcherAsString = null;

		Consumer switcher = new Switch();
		switcher.setPowerConsumed(55.6);
		switcher.setState(1);
		switcher.setName("switch1");

		try {

			switcherAsString = mapper.writeValueAsString(switcher);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals("Consumer message received -> switch.",
				receiverFunctions.consumerDataProcess(switcherAsString.getBytes()));
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

			sensorAsString = mapper.writeValueAsString(sensor);

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals("Sensor message received -> movement.",
				receiverFunctions.sensorDataProcess(sensorAsString.getBytes()));
	}

	@Test
	public void testMakeConsumerAndCircuitConnectionFunction() {

		Consumer consumer = new Consumer();
		consumer.setName("sensor1");

		Circuit circuit = new Circuit();

		circuit = databaseFunctions.makeConsumerAndCircuitConnection(consumer, circuit);
		assertEquals(consumer, circuit.getConsumers().get(0));

		Consumer consumerNew = new Consumer();
		consumer.setName("sensor1");
		consumer.setPowerConsumed(50.0);
		circuit = databaseFunctions.makeConsumerAndCircuitConnection(consumerNew, circuit);
		assertEquals(50.0, circuit.getConsumers().get(0).getPowerConsumed(), 0);
	}

	@Test
	public void testMakeCircuitAndPowerSourceConnectionFunction() {

		PowerSource powerSource = new PowerSource();
        powerSource.setId(5);
		Circuit circuit = new Circuit();
		circuit.setPowerConsumed(50.0);;

		powerSource = databaseFunctions.makeCircuitAndPowerSourceConnection(circuit, powerSource);

		assertEquals(50.0, powerSource.getCircuits().get(0).getPowerConsumed(),0);

	}
	
//	@Test
//	public void testSetNewSetOfCircuitsToPowerSourceFunction() {
//
//		PowerSource powerSource = new PowerSource();
//        powerSource.setId(5);
//        
//        List<Circuit> circuits = new Vector<Circuit>();
//        
//		Circuit circuitOne = new Circuit();
//		circuitOne.setPowerConsumed(50.0);
//		
//		Circuit circuitTwo = new Circuit();
//		circuitTwo.setPowerConsumed(51.0);
//		
//		Circuit circuitThree = new Circuit();
//		circuitThree.setPowerConsumed(52.0);
//
//		circuits.add(circuitOne);
//		circuits.add(circuitTwo);
//		circuits.add(circuitThree);
//		
//		powerSource = databaseFunctions.setNewSetOfCircuitsToPowerSource( powerSource,circuits);
//		
//		
//		assertEquals(50.0, powerSource.getCircuits().get(0).getPowerConsumed(),0);
//		assertEquals(51.0, powerSource.getCircuits().get(1).getPowerConsumed(),0);
//		assertEquals(52.0, powerSource.getCircuits().get(2).getPowerConsumed(),0);
//
//	}
	
	

}
