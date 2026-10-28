package com.alewicki.counter.service.jump;

import com.alewicki.counter.service.KeyPress;
import com.alewicki.counter.service.KeyType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
        int time = min + new Random().nextInt(delta);
        log.info("Clicking key {}, with time: {}", robotCode, time);
        if (keyPress == KeyPress.SWING) {
            moveMouse(time);
        } else {
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

    public static void moveMouse(long durationMs) throws Exception {
        Robot robot = new Robot();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        Point start = MouseInfo.getPointerInfo().getLocation();
        double x = start.x;
        double y = start.y;

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        double angle = random.nextDouble(0, Math.PI * 2);
        double speed = random.nextDouble(2, 30);
        double targetSpeed = speed;

        long end = System.nanoTime() + durationMs * 3 * 1_000_000L;
        long nextSpeedChange = System.nanoTime();

        while (System.nanoTime() < end) {
            // Losuj nową docelową prędkość co 200-800 ms
            if (System.nanoTime() >= nextSpeedChange) {
                targetSpeed = random.nextDouble(2, 30);
                nextSpeedChange = System.nanoTime()
                        + random.nextLong(200, 801) * 1_000_000L;
            }

            // Płynne przyspieszanie i hamowanie
            speed += (targetSpeed - speed) * 0.03;

            // Płynna zmiana kierunku
            angle += random.nextDouble(-0.08, 0.08);

            x += Math.cos(angle) * speed;
            y += Math.sin(angle) * speed;

            // Odbicie od krawędzi ekranu
            if (x < 0 || x >= screen.width) {
                angle = Math.PI - angle;
                x = Math.max(0, Math.min(screen.width - 1, x));
            }

            if (y < 0 || y >= screen.height) {
                angle = -angle;
                y = Math.max(0, Math.min(screen.height - 1, y));
            }

            robot.mouseMove((int) x, (int) y);
            Thread.sleep(10);
        }
    }
}
