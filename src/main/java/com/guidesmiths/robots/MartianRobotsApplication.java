package com.guidesmiths.robots;

import com.guidesmiths.robots.engine.EngineService;
import com.guidesmiths.robots.tokenizer.RobotInstructionToken;
import com.guidesmiths.robots.tokenizer.RobotToken;
import com.guidesmiths.robots.tokenizer.ScannerWrapper;
import com.guidesmiths.robots.tokenizer.TokenizerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootApplication
@RequiredArgsConstructor
public class MartianRobotsApplication implements CommandLineRunner {

	private final ScannerWrapper scannerWrapper;
	private final TokenizerService tokenizerService;
	private final EngineService engineService;

	@Override
	public void run(String... args) {

		var robotTokenList = new ArrayList<Pair<RobotToken, RobotInstructionToken>>();
		var initToken = tokenizerService.parseInitToken(scannerWrapper.nextLine());

		while (scannerWrapper.hasNext()) {
			var robotToken = tokenizerService.parseRobotToken(scannerWrapper.nextLine());
			var robotInstructionToken = tokenizerService.parseRobotInstructionToken(scannerWrapper.nextLine());
			robotTokenList.add(Pair.of(robotToken, robotInstructionToken));
		}

		engineService.init(initToken, Collections.unmodifiableList(robotTokenList));
		engineService.run()
				.stream()
				.forEach( robotReport -> System.out.println(robotReport));
	}

	public static void main(String[] args) {
		SpringApplication.run(MartianRobotsApplication.class, args);
	}
}
