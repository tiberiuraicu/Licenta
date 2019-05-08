package com.server.devicesInstructionsSender;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.constants.Constants;
import com.server.database.repositories.CircuitRepository;
import com.server.database.repositories.ConsumerRepository;
import com.server.database.repositories.ScenarioRepository;
import com.server.entites.Circuit;
import com.server.entites.Consumer;
import com.server.entites.Instruction;

@Component
public class InstructionsSender {
	@Autowired
	RabbitTemplate template;
	@Autowired
	DirectExchange exchange;
	@Autowired
	ScenarioRepository scenarioRepository;
	@Autowired
	ConsumerRepository consumerRepository;
	@Autowired
	CircuitRepository circuitRepository;

	ObjectMapper mapper = new ObjectMapper();

	public void sendInstruction(Instruction instruction) {

		// transform Object to JSON
		String instructionAsJSON = null;
		try {
			instructionAsJSON = mapper.writeValueAsString(instruction);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// send the JSON to devices
		// TODO
		String callBackMessage = (String) this.template.convertSendAndReceive(exchange.getName(),
				Constants.INSTRUCTION_KEY, instructionAsJSON.getBytes());

		 //System.out.println(callBackMessage);
	}

	public void turnOffTheLight(String sensorName) {
		String switchName = scenarioRepository.getScenarioBySensorName(sensorName).getSwitchName();

		Double powerConsumed = consumerRepository.findTopByNameOrderByIdDesc(switchName).getPowerConsumed();

		if (powerConsumed > 0) {

			Instruction switchStop = new Instruction();
			switchStop.setType("OnOff");
			switchStop.setDeviceName(switchName);
			switchStop.setOnOffValue("0");

			sendInstruction(switchStop);
		}
	}

	public void turnOnOffTheDevice(String deviceName, String onOffValue) {

		Instruction outletStop = new Instruction();
		outletStop.setType("OnOff");
		outletStop.setDeviceName(deviceName);
		outletStop.setOnOffValue(onOffValue);
		Consumer consumer = consumerRepository.findTopByNameOrderByIdDesc(deviceName);
		consumer.setState(Integer.parseInt(onOffValue));
		consumerRepository.save(consumer);
		sendInstruction(outletStop);
	}

	public void changeCircuitPowerSource() {

		// for every circuit from home
		for (Circuit circuit : circuitRepository.findAll()) {

			// create a new instruction
			Instruction instruction = new Instruction();

			// with the type PowerSourceChange
			instruction.setType("PowerSourceChange");

			// set the device name = the circuit ID
			instruction.setDeviceName(circuit.getId().toString());

			// if the circuit is powerd by solar panel
			if (circuit.getPowerSource().getType().equals("solarPanel")) {
				instruction.setPowerSource("solarPowerSource");

				// if the circuit is powerd by the normal power source
			} else if (circuit.getPowerSource().getType().equals("normalPowerSource")) {
				instruction.setPowerSource("normalPowerSource");
			}

			sendInstruction(instruction);
		}
	}
}
