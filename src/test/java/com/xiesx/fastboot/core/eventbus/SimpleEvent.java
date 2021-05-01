package com.xiesx.fastboot.core.eventbus;

import com.xiesx.fastboot.core.eventbus.base.BaseEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEvent implements BaseEvent {

    @NonNull
    private String name;

    private Boolean sleep;
}
