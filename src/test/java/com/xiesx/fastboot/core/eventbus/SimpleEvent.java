package com.xiesx.fastboot.core.eventbus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @title SimpleEvent.java
 * @description
 * @author xiesx
 * @date 2021-06-06 23:19:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEvent {

    @NonNull
    private String name;

    private Boolean sleep;
}
