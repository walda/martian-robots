package com.guidesmiths.robots;

import com.guidesmiths.robots.engine.EngineService;
import com.guidesmiths.robots.tokenizer.InitToken;
import com.guidesmiths.robots.tokenizer.RobotInstructionToken;
import com.guidesmiths.robots.tokenizer.RobotToken;
import com.guidesmiths.robots.tokenizer.TokenizerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

@SpringBootApplication
@RequiredArgsConstructor
public class MartianRobotsApplication implements CommandLineRunner {

	private final Scanner scanner;
	private final TokenizerService tokenizerService;
	private final EngineService engineService;

	@Override
	public void run(String... args) {

		var robotTokenList = new ArrayList<Pair<RobotToken, RobotInstructionToken>>();
		var initToken = tokenizerService.parseInitToken(scanner.nextLine());

		while (scanner.hasNext()) {
			var robotToken = tokenizerService.parseRobotToken(scanner.nextLine());
			var robotInstructionToken = tokenizerService.parseRobotInstructionToken(scanner.nextLine());
			robotTokenList.add(Pair.of(robotToken, robotInstructionToken));
		}

		engineService.initMars(initToken, Collections.unmodifiableList(robotTokenList));
		engineService.run()
				.stream()
				.forEach( robotReport -> System.out.println(robotReport));
	}

	public static void main(String[] args) {
		SpringApplication.run(MartianRobotsApplication.class, args);
	}
}
