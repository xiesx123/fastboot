package com.xiesx.fastboot.controller.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.xiesx.fastboot.core.fastjson.annotation.GoDesensitized;
import com.xiesx.fastboot.db.jpa.entity.JpaPlusEntity;

import cn.hutool.core.util.DesensitizedUtil.DesensitizedType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * @title Simple.java
 * @description
 * @author xiesx
 * @date 2021-04-03 17:45:34
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants(innerTypeName = "FIELDS")
@Table(name = "xx_user")
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Where(clause = "is_del=0")
@SQLDelete(sql = "update xx_user set delete=1 where id = ?")
@SQLDeleteAll(sql = "update xx_user set delete=1 where id = ?")
public class User extends JpaPlusEntity<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.xiesx.fastboot.db.jpa.identifier.IdWorkerGenerator", parameters = {@Parameter(name = "workerId", value = "0"), @Parameter(name = "datacenterId", value = "0")})
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Date createDate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(nullable = false)
    private Date updateDate;

    /**
     * 帐号
     */
    @Column
    private String username;

    /**
     * 密码
     */
    @Column
    private String password;

    /**
     * 昵称
     */
    @GoDesensitized(type = DesensitizedType.CHINESE_NAME)
    @Column
    private String nickname;

    /**
     * 类型(1:系统、2:机构、3:商家)
     */
    @Column
    private Integer type;

    /**
     * 拓展数据
     */
    @Column
    private String extData;

    /**
     * 登录失败次数
     */
    @Column
    private Integer lastLoginFailures;

    /**
     * 最后登录时间
     */
    @Column
    private Date lastLoginDate;

    /**
     * 是否禁用（true:启用、false:禁用）
     */
    @Column
    private Boolean isEnable;

    /**
     * 是否删除（0:正常、1:删除）
     */
    @Column
    private Integer isDel;
}
