package ru.mephi.spark.rdd.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "result", indexes = {@Index(name="TIME_RESULT_INDEX",columnList = "time")})
public class ResultEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long groupId;

    private Date time;

    private Integer value;

    public ResultEntity() {
    }

    public ResultEntity(Long groupId, Date time, Integer value) {
        this.groupId = groupId;
        this.time = time;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
