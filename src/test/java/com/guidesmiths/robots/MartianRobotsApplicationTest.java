package com.guidesmiths.robots;

import com.guidesmiths.robots.engine.EngineService;
import com.guidesmiths.robots.engine.RobotReport;
import com.guidesmiths.robots.exception.UnparseableTokenException;
import com.guidesmiths.robots.tokenizer.InitToken;
import com.guidesmiths.robots.tokenizer.RobotInstructionToken;
import com.guidesmiths.robots.tokenizer.RobotToken;
import com.guidesmiths.robots.tokenizer.ScannerWrapper;
import com.guidesmiths.robots.tokenizer.TokenizerService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class MartianRobotsApplicationTest {

	@Mock
	private ScannerWrapper scannerWrapper;
	@Mock
	private TokenizerService tokenizerService;
	@Mock
	private EngineService engineService;
	@InjectMocks
	private MartianRobotsApplication martianRobotsApplication;

	@Test
	public void should_run_the_application() {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));

		when(scannerWrapper.nextLine()).thenReturn("5 5", "1 1 S", "L");
		when(scannerWrapper.hasNext()).thenReturn(true, false);

		when(tokenizerService.parseInitToken("5 5")).thenReturn(new InitToken(5, 5));
		when(tokenizerService.parseRobotToken("1 1 S")).thenReturn(new RobotToken(Pair.of(1, 1), 'S'));
		when(tokenizerService.parseRobotInstructionToken("L")).thenReturn(new RobotInstructionToken("L"));

		when(engineService.run()).thenReturn(Collections.singletonList(new RobotReport(Pair.of(1, 1), 'N', false)));

		martianRobotsApplication.run();

		assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
				.isEqualTo("1 1 N\n");

		verify(scannerWrapper, times(3)).nextLine();
		verify(scannerWrapper, times(2)).hasNext();

		verify(engineService).init(new InitToken(5, 5), Arrays.asList(Pair.of(new RobotToken(Pair.of(1, 1 ), 'S'), new RobotInstructionToken("L"))));
		verify(engineService).run();

		verify(tokenizerService).parseInitToken("5 5");
		verify(tokenizerService).parseRobotToken("1 1 S");
		verify(tokenizerService).parseRobotInstructionToken("L");

	}

	@Test
	public void should_run_the_application_with_no_robots() {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));

		when(scannerWrapper.nextLine()).thenReturn("5 5");
		when(scannerWrapper.hasNext()).thenReturn(false);

		when(tokenizerService.parseInitToken("5 5")).thenReturn(new InitToken(5, 5));

		martianRobotsApplication.run();

		assertThat(byteArrayOutputStream.toString())
				.isEqualTo("");

		verify(scannerWrapper).nextLine();
		verify(scannerWrapper).hasNext();

		verify(engineService).init(new InitToken(5, 5), Collections.emptyList());
		verify(engineService).run();

		verify(tokenizerService).parseInitToken("5 5");

	}

	@Test
	public void should_print_error_if_initToken_is_unparseable() {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setErr(new PrintStream(byteArrayOutputStream));

		when(scannerWrapper.nextLine()).thenReturn("5 a");
		when(tokenizerService.parseInitToken("5 a")).thenThrow(new UnparseableTokenException("5 a"));

		martianRobotsApplication.run();

		assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
				.isEqualTo("Error at line 1. Input '5 a' cannot be parsed\n");

		verify(scannerWrapper).nextLine();
		verify(tokenizerService).parseInitToken("5 a");

	}

	@Test
	public void should_print_error_if_robotToken_is_unparseable() {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setErr(new PrintStream(byteArrayOutputStream));

		when(scannerWrapper.nextLine()).thenReturn("5 5", "1 a S");
		when(scannerWrapper.hasNext()).thenReturn(true);
		when(tokenizerService.parseInitToken("5 5")).thenReturn(new InitToken(5, 5));
		when(tokenizerService.parseRobotToken("1 a S")).thenThrow(new UnparseableTokenException("1 a S"));

		martianRobotsApplication.run();

		assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
				.isEqualTo("Error at line 2. Input '1 a S' cannot be parsed\n");

		verify(scannerWrapper, times(2)).nextLine();
		verify(scannerWrapper).hasNext();
		verify(tokenizerService).parseInitToken("5 5");
		verify(tokenizerService).parseRobotToken("1 a S");

	}

	@Test
	public void should_print_error_if_robotTokenInstruction_is_unparseable() {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setErr(new PrintStream(byteArrayOutputStream));

		when(scannerWrapper.nextLine()).thenReturn("5 5", "1 1 S", "A");
		when(scannerWrapper.hasNext()).thenReturn(true);
		when(tokenizerService.parseInitToken("5 5")).thenReturn(new InitToken(5, 5));
		when(tokenizerService.parseRobotToken("1 1 S")).thenReturn(new RobotToken(Pair.of(1, 1), 'S'));
		when(tokenizerService.parseRobotInstructionToken("A")).thenThrow(new UnparseableTokenException("A"));

		martianRobotsApplication.run();

		assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
				.isEqualTo("Error at line 3. Input 'A' cannot be parsed\n");

		verify(scannerWrapper, times(3)).nextLine();
		verify(scannerWrapper).hasNext();
		verify(tokenizerService).parseInitToken("5 5");
		verify(tokenizerService).parseRobotToken("1 1 S");
		verify(tokenizerService).parseRobotInstructionToken("A");

	}

	@AfterEach
	public void tearDown() {
		verifyNoMoreInteractions(scannerWrapper, tokenizerService, engineService);
	}

}
