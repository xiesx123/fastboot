package com.xiesx.fastboot.app.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.xiesx.fastboot.db.jpa.entity.JpaPlusEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * @title LogRecord.java
 * @description
 * @author xiesx
 * @date 2021-04-17 21:54:26
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@FieldNameConstants(innerTypeName = "FIELDS")
@Table(name = "xx_log")
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Where(clause = "is_del=0")
@SQLDelete(sql = "update xx_log set is_del=1 where id = ?")
@SQLDeleteAll(sql = "update xx_log set is_del=1 where id = ?")
public class LogRecord extends JpaPlusEntity<LogRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.xiesx.fastboot.db.jpa.identifier.IdWorkerGenerator")
    @JSONField(ordinal = 1)
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(updatable = false, nullable = false)
    @JSONField(ordinal = 2)
    private Date createDate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(nullable = false)
    @JSONField(ordinal = 3)
    private Date updateDate;

    /**
     * 请求IP
     */
    @Column
    @JSONField(ordinal = 4)
    private String ip;

    /**
     * 方法
     */
    @Column
    @JSONField(ordinal = 5)
    private String method;

    /**
     * 方式
     */
    @Column
    @JSONField(ordinal = 6)
    private String type;

    /**
     * 地址
     */
    @Column
    @JSONField(ordinal = 7)
    private String url;

    /**
     * 请求参数
     */
    @JSONField(serialize = false)
    private String req;

    /**
     * 响应结果
     */
    @JSONField(serialize = false)
    private String res;

    /**
     * 执行时间（毫秒）
     */
    @Column
    @JSONField(ordinal = 10)
    private Long time;

    /**
     * 是否删除
     */
    @Column
    @JSONField(serialize = false)
    private Boolean isDel = false;

    // ======================

    @JSONField(ordinal = 8)
    public Object getParams() {
        return JSON.parse(req);
    }

    @JSONField(ordinal = 9)
    public Object getResult() {
        return JSON.parse(res);
    }
}
