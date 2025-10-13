package com.xiesx.fastboot.db.jpa;

import lombok.Data;
import lombok.experimental.Accessors;

public class JpaPlusPojo {

    @Data
    @Accessors(chain = true)
    public static class LogRecordPojo {

        public Long id;

        public String ip;

        public String type;

        public Double time;
    }

    @Data
    public static class CommonPojo {

        public Long ct;
    }
}
