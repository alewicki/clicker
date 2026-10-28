package com.alewicki.counter.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static com.alewicki.counter.service.KeyConstants.*;
import static com.alewicki.counter.service.KeyType.KEYBOARD;
import static com.alewicki.counter.service.KeyType.MOUSE;

@RequiredArgsConstructor
@Getter
public enum KeyPress {

    SPACE(KEY_SPACE_PRESS, KEYBOARD, KeyEvent.VK_SPACE),
    A(KEY_A_PRESS, KEYBOARD, KeyEvent.VK_A),
    S(KEY_S_PRESS, KEYBOARD, KeyEvent.VK_S),
    D(KEY_D_PRESS, KEYBOARD, KeyEvent.VK_D),
    W(KEY_W_PRESS, KEYBOARD, KeyEvent.VK_W),
    E(KEY_E_PRESS, KEYBOARD, KeyEvent.VK_E),
    Q(KEY_Q_PRESS, KEYBOARD, KeyEvent.VK_Q),
    R(KEY_R_PRESS, KEYBOARD, KeyEvent.VK_R),
    CTRL(KEY_CTRL_PRESS, KEYBOARD, KeyEvent.VK_CONTROL),
    SHIFT(KEY_SHIFT_PRESS, KEYBOARD, KeyEvent.VK_SHIFT),
    ALT(KEY_ALT_PRESS, KEYBOARD, KeyEvent.VK_ALT),
    LPM(KEY_LPM_PRESS, MOUSE, InputEvent.BUTTON1_DOWN_MASK),
    PPM(KEY_PPM_PRESS, MOUSE, InputEvent.BUTTON3_DOWN_MASK),
    SWING(null,MOUSE ,0 );

    private final byte[] keyCode;
    private final KeyType type;
    private final int robotCode;

}
