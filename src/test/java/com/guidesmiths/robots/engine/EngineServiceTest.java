package com.guidesmiths.robots.engine;

import com.guidesmiths.robots.rule.Action;
import com.guidesmiths.robots.rule.RuleEngineService;
import com.guidesmiths.robots.tokenizer.InitToken;
import com.guidesmiths.robots.tokenizer.RobotInstructionToken;
import com.guidesmiths.robots.tokenizer.RobotToken;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class EngineServiceTest {

    private InitToken initToken;
    private List<Pair<RobotToken, RobotInstructionToken>> robotTokenList;
    @Mock
    private MarsService marsService;
    @Mock
    private RuleEngineService ruleEngineService;
    @InjectMocks
    private EngineService engineService;


    @Test
    public void shouldInitialize() throws IllegalAccessException {

        initializeEngineService("L", 'N');

        verify(marsService).init(2, 2, new HashMap<>());

        List<Robot> robotList = (List<Robot>) FieldUtils.readDeclaredField(engineService, "robotList", true);

        assertThat(robotList)
                .isNotNull()
                .containsExactly(Robot.builder()
                        .position(Pair.of(1, 1))
                        .direction('N')
                        .instructions("L")
                        .build());

    }

    @Test
    public void shouldReturnRobotsReportRotateL() {
        shouldReturnRobotsReportWhenRotate('N', "L", Action.DIRECTION_W,'W', 1, 1);
    }

    @Test
    public void shouldReturnRobotsReportRotateR() {
        shouldReturnRobotsReportWhenRotate('N', "R", Action.DIRECTION_E,'E', 1, 1);
    }

    @Test
    public void shouldReturnRobotsReportMoveNorth() {
        shouldReturnRobotsReportWhenMove('N', "F", Action.INCREASE_Y,'N', 1, 2);
    }

    @Test
    public void shouldReturnRobotsReportMoveSouth() {
        shouldReturnRobotsReportWhenMove('S', "F", Action.DECREASE_Y,'S', 1, 0);
    }

    @Test
    public void shouldReturnRobotsReportMoveEast() {
        shouldReturnRobotsReportWhenMove('E', "F", Action.INCREASE_X,'E', 2, 1);
    }

    @Test
    public void shouldReturnRobotsReportMoveWest() {
        shouldReturnRobotsReportWhenMove('W', "F", Action.INCREASE_X,'W', 2, 1);
    }

    @Test
    public void shouldReturnRobotsReportCrashedWest() {
        shouldReturnRobotsReportWhenMoveAndCrash('W', "FF",
                Action.INCREASE_X,'W', Pair.of(1, 1), Pair.of(2,1), Pair.of(3, 1));
    }

    @Test
    public void shouldReturnRobotsReportCrashedEast() {
        shouldReturnRobotsReportWhenMoveAndCrash('E', "FF", Action.DECREASE_X, 'E',
                Pair.of(1, 1), Pair.of(0,1), Pair.of(-1, 1));
    }

    @Test
    public void shouldReturnRobotsReportCrashedNorth() {
        shouldReturnRobotsReportWhenMoveAndCrash('N', "FF", Action.INCREASE_Y,'N',
                Pair.of(1, 1), Pair.of(1,2), Pair.of(1, 3));
    }

    @Test
    public void shouldReturnRobotsReportCrashedSouth() {
        shouldReturnRobotsReportWhenMoveAndCrash('S', "FF", Action.DECREASE_Y,'S',
                Pair.of(1, 1), Pair.of(1,0), Pair.of(1, -1));
    }

    @Test
    public void shouldAvoidCrashing() {
        var robotToken = Arrays.asList(new RobotToken(Pair.of(0, 0), 'W'), new RobotToken(Pair.of(0, 0), 'W'));
        var robotInstructionToken = Arrays.asList(new RobotInstructionToken("F"), new RobotInstructionToken("F"));
        robotTokenList = Arrays.asList(Pair.of(robotToken.get(0), robotInstructionToken.get(0)), Pair.of(robotToken.get(1), robotInstructionToken.get(1)));
        initToken = new InitToken(0,0);

        when(ruleEngineService.calculateActions('F', 'W'))
                .thenReturn(Collections.singletonList(Action.DECREASE_X));
        when(marsService.checkField(Pair.of(-1, 0), 'W'))
                .thenReturn(MarsService.FIELD_STATUS.EXPLORED, MarsService.FIELD_STATUS.CRASHED);
        when(marsService.explore(Pair.of(-1, 0), 'W')).thenReturn(MarsService.FIELD_STATUS.CRASHED);

        engineService.init(initToken, robotTokenList);

        assertThat(engineService.run())
                .isNotNull()
                .containsExactlyInAnyOrder(new RobotReport(Pair.of(0, 0), 'W', true),
                        new RobotReport(Pair.of(0, 0), 'W', false));

        verify(marsService).init(0, 0, new HashMap<>());
        verify(ruleEngineService, times(2)).calculateActions('F', 'W');
        verify(marsService, times(2)).checkField(Pair.of(-1, 0), 'W');
        verify(marsService).explore(Pair.of(-1, 0), 'W');
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(ruleEngineService, marsService);
    }

    private void initializeEngineService(String instructions, char direction) {
        var robotToken = new RobotToken(Pair.of(1, 1), direction);
        var robotInstructionToken = new RobotInstructionToken(instructions);
        robotTokenList = Collections.singletonList(Pair.of(robotToken, robotInstructionToken));
        initToken = new InitToken(2,2);

        engineService.init(initToken, robotTokenList);
    }

    private void shouldReturnRobotsReportWhenRotate(char direction, String instruction, Action action, char directionEnd, int x, int y) {

        initializeEngineService(instruction, direction);

        when(ruleEngineService.calculateActions(instruction.charAt(0), direction))
                .thenReturn(Collections.singletonList(action));

        assertThat(engineService.run())
                .isNotNull()
        .containsExactly(new RobotReport(Pair.of(x, y), directionEnd, false));

         verify(ruleEngineService, times(instruction.length())).calculateActions(instruction.charAt(0), direction);
         verify(marsService).init(2, 2, new HashMap<>());
    }

    private void shouldReturnRobotsReportWhenMove(char direction, String instruction, Action action, char directionEnd, int x, int y) {

        initializeEngineService(instruction, direction);

        when(ruleEngineService.calculateActions(instruction.charAt(0), direction))
                .thenReturn(Collections.singletonList(action));
        when(marsService.checkField(Pair.of(x, y), direction)).thenReturn(MarsService.FIELD_STATUS.UNKNOWN);
        when(marsService.explore(Pair.of(x, y), direction)).thenReturn(MarsService.FIELD_STATUS.EXPLORED);

        assertThat(engineService.run())
                .isNotNull()
                .containsExactly(new RobotReport(Pair.of(x, y), directionEnd, false));

        verify(ruleEngineService, times(instruction.length())).calculateActions(instruction.charAt(0), direction);

         verify(marsService).checkField(Pair.of(x, y), direction);
         verify(marsService).explore(Pair.of(x, y), direction);
        verify(marsService).init(2, 2, new HashMap<>());
    }

    private void shouldReturnRobotsReportWhenMoveAndCrash(char direction, String instruction, Action action,
                                                          char directionEnd, Pair<Integer, Integer> initialPosition,
                                                          Pair<Integer, Integer> firstField,
                                                          Pair<Integer, Integer> secondField) {

        initializeEngineService(instruction, direction);

        when(ruleEngineService.calculateActions(instruction.charAt(0), direction))
                .thenReturn(Arrays.asList(action, action));
        when(marsService.checkField(Pair.of(firstField.getLeft(), firstField.getRight()), direction)).thenReturn(MarsService.FIELD_STATUS.EXPLORED);
        when(marsService.checkField(Pair.of(secondField.getLeft(), secondField.getRight()), direction)).thenReturn(MarsService.FIELD_STATUS.EXPLORED);
        when(marsService.explore(Pair.of(firstField.getLeft(), firstField.getRight()), direction)).thenReturn(MarsService.FIELD_STATUS.EXPLORED);
        when(marsService.explore(Pair.of(secondField.getLeft(), secondField.getRight()), direction)).thenReturn(MarsService.FIELD_STATUS.CRASHED);

        assertThat(engineService.run())
                .isNotNull()
                .containsExactly(new RobotReport(Pair.of(firstField.getLeft(), firstField.getRight()), directionEnd, true));

        verify(ruleEngineService).calculateActions('F', direction);
        verify(marsService).checkField(Pair.of(firstField.getLeft(), firstField.getRight()), direction);
        verify(marsService).checkField(Pair.of(secondField.getLeft(), secondField.getRight()), direction);
        verify(marsService).explore(Pair.of(firstField.getLeft(), firstField.getRight()), direction);
        verify(marsService).explore(Pair.of(secondField.getLeft(), secondField.getRight()), direction);
        verify(marsService).init(2, 2, new HashMap<>());
    }
}
