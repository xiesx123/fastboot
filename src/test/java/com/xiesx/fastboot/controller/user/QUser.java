package com.xiesx.fastboot.controller.user;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 2044921628L;

    public static final QUser user = new QUser("user");

    public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

    public final StringPath extData = createString("extData");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isBlack = createBoolean("isBlack");

    public final NumberPath<Integer> isDel = createNumber("isDel", Integer.class);

    public final DateTimePath<java.util.Date> lastLoginDate = createDateTime("lastLoginDate", java.util.Date.class);

    public final NumberPath<Integer> lastLoginFailures = createNumber("lastLoginFailures", Integer.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

