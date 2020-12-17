package ru.mephi.spark.rdd.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class MetricDto implements Serializable {

    private Long id;

    private Date time;

    private Float value;

    public MetricDto() {
    }

    public MetricDto(Long id, Date time, Float value) {
        this.id = id;
        this.time = time;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    /**
     * Returns a string representation of the object
     * @return String representation
     */
    @Override
    public String toString() {
        return "TestDataDto [id=" + id + ", time=" + time + ", value=" + value + "]";
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o Object compare with
     * @return Equals flag
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricDto metricDto = (MetricDto) o;
        return id.equals(metricDto.id) &&
                time.equals(metricDto.time) &&
                value.equals(metricDto.value);
    }

    /**
     * Returns a hash code value for the object.
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, time, value);
    }
}
