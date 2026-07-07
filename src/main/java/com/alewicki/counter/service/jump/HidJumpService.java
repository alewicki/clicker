package com.alewicki.counter.service.jump;

import com.alewicki.counter.service.KeyPress;
import com.alewicki.counter.service.KeyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.nio.file.Files;

@Service
@Slf4j
@ConditionalOnProperty(
        name = "keys.mode",
        havingValue = "HID"
)
public class HidJumpService implements JumpService {


    @Override
    public synchronized void jump(
            final KeyPress keyPress
    ) {

        KeyType type = keyPress.getType();
        try (OutputStream os = Files.newOutputStream(type.getPath())) {
            os.write(keyPress.getKeyCode());
            os.flush();

            Thread.sleep(20);

            os.write(type.getReleaseCode());
            os.flush();

        } catch (Exception e) {
            log.error("Failed to send HID event", e);
        }

    }
}
