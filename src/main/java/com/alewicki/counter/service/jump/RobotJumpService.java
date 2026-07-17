package com.alewicki.counter.service.jump;

import com.alewicki.counter.service.KeyPress;
import com.alewicki.counter.service.KeyType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Random;

@Service
@Slf4j
@ConditionalOnProperty(
        name = "keys.mode",
        havingValue = "ROBOT"
)
public class RobotJumpService implements JumpService {
    @Value("${keys.click.time.min}")
    private Integer min;
    @Value("${keys.click.time.delta}")
    private Integer delta;
    @Override
    @SneakyThrows
    public synchronized void jump(KeyPress keyPress) {
        KeyType type = keyPress.getType();

        Robot robot = new Robot();

        int robotCode = keyPress.getRobotCode();
        int time = min+new Random().nextInt(delta);
        log.info("Clicking key {}, with time: {}" ,robotCode,time);
        if (type == KeyType.KEYBOARD) {
            robot.keyPress(robotCode);
            Thread.sleep(time);
            robot.keyRelease(robotCode);
        } else {
            robot.mousePress(robotCode);
            Thread.sleep(time);
            robot.mouseRelease(robotCode);
        }

    }
}
