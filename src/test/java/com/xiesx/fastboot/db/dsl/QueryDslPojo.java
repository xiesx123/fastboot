package com.xiesx.fastboot.db.dsl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public class QueryDslPojo {

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogRecordPojo {

        public Long id;

        public String ip;

        public Long min;

        public Long max;
    }
}
