package com.alewicki.counter.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@RequiredArgsConstructor
@Getter
public enum KeyType {
    MOUSE(
            new byte[3],
            Path.of("/dev/hidg1")

    ),
    KEYBOARD(
            new byte[8],
            Path.of("/dev/hidg0")
    );

    private final byte[] releaseCode;
    private final Path path;

}
