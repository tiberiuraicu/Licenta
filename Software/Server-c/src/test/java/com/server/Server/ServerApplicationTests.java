package com.server.Server;

import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.server.cep.processing.FunctiiAjutor;
import com.server.database.repositories.CircuitRepository;
import com.server.entites.Circuit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerApplicationTests {
FunctiiAjutor functiiAjutor = new FunctiiAjutor();
@Autowired
CircuitRepository circuitRepository;
	@Test
	public void contextLoads() {
		Set<Circuit> set= new HashSet<Circuit>(circuitRepository.findAll());

		 Set<Set<Circuit>> result = Sets.powerSet(Sets.newHashSet(set));
		    for(Set<Circuit> token : result){
		        System.out.println(token);
		    }
	}

}

