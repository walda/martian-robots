package com.guidesmiths.robots;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MartianRobotsIT {

    private final static String FULL_EXECUTION_INPUT = "5 3\n1 1 E\nRFRFRFRF\n3 2 N\nFRRFLLFFRRFLL\n0 3 W\nLLFFFLFLFL";
    private final static String ROBOT_REMAINS_INSIDE_AFTER_MOVING_INPUT = "1 1\n0 0 N\nF";
    private final static String ROBOT_REMAINS_INSIDE_AFTER_ROTATE_INPUT = "1 1\n0 0 N\nL";
    private final static String ROBOT_CRASHES_INPUT = "1 1\n0 1 N\nF";
    private final static String ROBOT_AVOID_CRASHING_INPUT = "1 1\n0 1 N\nF\n0 1 N\nF";

    @Test
    public void should_run_application() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(FULL_EXECUTION_INPUT.getBytes()));
        System.setOut(new PrintStream(byteArrayOutputStream));

        MartianRobotsApplication.main(new String[0]);

        assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
                .isEqualTo("1 1 E\n3 3 N LOST\n2 3 S\n");
    }

    @Test
    public void should_run_with_robot_remaining_inside_after_moving() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(ROBOT_REMAINS_INSIDE_AFTER_MOVING_INPUT.getBytes()));
        System.setOut(new PrintStream(byteArrayOutputStream));

        MartianRobotsApplication.main(new String[0]);

        assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
                .isEqualTo("0 1 N\n");
    }

    @Test
    public void should_run_with_robot_remaining_inside_after_rotating() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(ROBOT_REMAINS_INSIDE_AFTER_ROTATE_INPUT.getBytes()));
        System.setOut(new PrintStream(byteArrayOutputStream));

        MartianRobotsApplication.main(new String[0]);

        assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
                .isEqualTo("0 0 W\n");
    }

    @Test
    public void should_run_with_robot_crashes() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(ROBOT_CRASHES_INPUT.getBytes()));
        System.setOut(new PrintStream(byteArrayOutputStream));

        MartianRobotsApplication.main(new String[0]);

        assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
                .isEqualTo("0 1 N LOST\n");
    }

    @Test
    public void should_run_with_robot_avoiding_crash() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(ROBOT_AVOID_CRASHING_INPUT.getBytes()));
        System.setOut(new PrintStream(byteArrayOutputStream));

        MartianRobotsApplication.main(new String[0]);

        assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n"))
                .isEqualTo("0 1 N LOST\n0 1 N\n");
    }

}
