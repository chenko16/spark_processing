package ru.mephi.spark.rdd.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "metric", indexes = {@Index(name="TIME_INDEX",columnList = "time")})
public class MetricEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long groupId;

    private Date time;

    private Float value;

    public MetricEntity() {
    }

    public MetricEntity(Long groupId, Date time, Float value) {
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

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
