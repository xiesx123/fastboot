package com.xiesx.fastboot.test.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEvent {

  @NonNull private String name;

  private boolean sleep;
}
