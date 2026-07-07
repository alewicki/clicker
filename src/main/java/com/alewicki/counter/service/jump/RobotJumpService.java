package com.alewicki.counter.service.jump;

import com.alewicki.counter.service.KeyPress;
import com.alewicki.counter.service.KeyType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@Slf4j
@ConditionalOnProperty(
        name = "keys.mode",
        havingValue = "ROBOT"
)
public class RobotJumpService implements JumpService {
    @Override
    @SneakyThrows
    public synchronized void jump(KeyPress keyPress) {
        KeyType type = keyPress.getType();

        Robot robot = new Robot();

        int robotCode = keyPress.getRobotCode();
        if (type == KeyType.KEYBOARD) {
            robot.keyPress(robotCode);
            Thread.sleep(20);
            robot.keyRelease(robotCode);
        } else {
            robot.mousePress(robotCode);
            Thread.sleep(20);
            robot.mouseRelease(robotCode);
        }

    }
}
