package com.guidesmiths.robots.engine;

import com.guidesmiths.robots.rule.Action;
import com.guidesmiths.robots.rule.RuleEngineService;
import com.guidesmiths.robots.tokenizer.InitToken;
import com.guidesmiths.robots.tokenizer.RobotInstructionToken;
import com.guidesmiths.robots.tokenizer.RobotToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EngineService {

    private List<Robot> robotList;
    private final RuleEngineService ruleEngineService;
    private final MarsService marsService;

    public void init(InitToken initToken, List<Pair<RobotToken, RobotInstructionToken>> robotTokenList) {
        marsService.init(initToken.getX(), initToken.getY(), new HashMap<>());
        robotList = robotTokenList.stream()
                .map(pair -> toRobot(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toList());
    }
    private static Robot toRobot(RobotToken robotToken, RobotInstructionToken robotInstructionToken) {
        return Robot.builder()
                .position(Pair.of(robotToken.getPosition().getLeft(), robotToken.getPosition().getRight()))
                .direction(robotToken.getDirection())
                .instructions(robotInstructionToken.getInstructions())
                .build();
    }

    public List<RobotReport> run() {
        return robotList.
                stream()
                .map(this::moveRobot)
                .collect(Collectors.toList());
    }

    private RobotReport moveRobot(Robot robot) {
        boolean robotCrashed = false;
        while(robot.hasNextInstruction() && !robotCrashed) {
            var actionList = ruleEngineService.calculateActions(robot.nextInstruction(), robot.getDirection());

            for(int i=0; i< actionList.size(); i++) {

                if(isDisplacement(actionList.get(i))) {
                    var currentPosition = robot.getPosition();
                    var nextPosition = calculateNextPosition(currentPosition, actionList.get(i));
                    if(marsService.checkField(nextPosition, robot.getDirection()) != MarsService.FIELD_STATUS.CRASHED) {
                        MarsService.FIELD_STATUS field_status = marsService.explore(nextPosition, robot.getDirection());
                        robotCrashed = field_status == MarsService.FIELD_STATUS.CRASHED;

                        if(!robotCrashed) {
                            robot.setPosition(nextPosition);
                        }
                    }

                } else {
                    robot.setDirection(actionList.get(i).getValue().charAt(0));
                }

            }
        }
        return new RobotReport(robot.getPosition(), robot.getDirection(), robotCrashed);

    }

    private static boolean isDisplacement(Action action) {
        return action == Action.INCREASE_X || action == Action.DECREASE_X || action == Action.INCREASE_Y || action == Action.DECREASE_Y;
    }

    private static Pair<Integer, Integer> calculateNextPosition(Pair<Integer, Integer> position, Action action) {
        if ( action == Action.INCREASE_X ) {
            return Pair.of(position.getLeft() + 1, position.getRight());
        } else if ( action == Action.DECREASE_X ) {
            return Pair.of(position.getLeft() - 1, position.getRight());
        } else if ( action == Action.INCREASE_Y ) {
            return Pair.of(position.getLeft(), position.getRight() + 1);
        } else {
            return Pair.of(position.getLeft(), position.getRight() - 1);
        }
    }
}
