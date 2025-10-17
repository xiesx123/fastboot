package com.xiesx.fastboot.db.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;

public class JpaPlusPojo {

    @Data
    @AllArgsConstructor
    public static class LogRecordPojo {

        public Long id;

        public String ip;

        public Long min;

        public Long max;
    }
}
