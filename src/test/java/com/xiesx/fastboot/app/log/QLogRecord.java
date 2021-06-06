package com.xiesx.fastboot.app.log;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;


/**
 * QLogRecord is a Querydsl query type for LogRecord
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLogRecord extends EntityPathBase<LogRecord> {

    private static final long serialVersionUID = -614643530L;

    public static final QLogRecord logRecord = new QLogRecord("logRecord");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ip = createString("ip");

    public final BooleanPath isDel = createBoolean("isDel");

    public final StringPath method = createString("method");

    public final StringPath req = createString("req");

    public final StringPath res = createString("res");

    public final NumberPath<Long> t = createNumber("t", Long.class);

    public final StringPath type = createString("type");

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public final StringPath url = createString("url");

    public QLogRecord(String variable) {
        super(LogRecord.class, forVariable(variable));
    }

    public QLogRecord(Path<? extends LogRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLogRecord(PathMetadata metadata) {
        super(LogRecord.class, metadata);
    }

}

