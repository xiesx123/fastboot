package com.xiesx.fastboot.test.log;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.xiesx.fastboot.db.jpa.JpaPlusEntity;
import com.xiesx.fastboot.db.jpa.identifier.GeneratedIdWorker;

import jakarta.persistence.*;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@FieldNameConstants(innerTypeName = "FIELDS")
@Table(name = "xx_log")
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@SQLRestriction("del=0")
@SQLDelete(sql = "update xx_log set del=1 where id = ?")
public class LogRecord extends JpaPlusEntity<LogRecord> {

    private static final long serialVersionUID = 1L;

    /** 主键，例（1976241098078683136） */
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GeneratedIdWorker(prefix = "", centerId = 1, workerId = 0)
    @JSONField(ordinal = 1)
    private Long id;

    /** 创建时间 */
    @CreatedDate
    @Column(updatable = false, nullable = false)
    @JSONField(ordinal = 2)
    private Date createDate;

    /** 修改时间 */
    @LastModifiedDate
    @Column(nullable = false)
    @JSONField(ordinal = 3)
    private Date updateDate;

    /** 请求IP */
    @Column
    @JSONField(ordinal = 4)
    private String ip;

    /** 方法 */
    @Column
    @JSONField(ordinal = 5)
    private String method;

    /** 方式 */
    @Column
    @JSONField(ordinal = 6)
    private String type;

    /** 地址 */
    @Column
    @JSONField(ordinal = 7)
    private String url;

    /** 请求参数 */
    @JSONField(serialize = false)
    @Lob
    private String req;

    /** 响应结果 */
    @Lob
    @JSONField(serialize = false)
    private String res;

    /** 执行时间（毫秒） */
    @Column
    @JSONField(ordinal = 10)
    private Long time;

    /** 是否删除 */
    @Column
    @JSONField(serialize = false)
    private Integer del = 0;

    // ======================

    @JSONField(serialize = false, ordinal = 8)
    public Object getParams() {
        return JSON.parse(req);
    }

    @JSONField(serialize = false, ordinal = 9)
    public Object getResult() {
        return JSON.parse(res);
    }
}
