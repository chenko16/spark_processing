package ru.mephi.spark.banchmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MetricDto implements Serializable {

    private Long id;

    private Date time;

    private Integer value;

    /**
     * Returns a string representation of the object
     * @return String representation
     */
    @Override
    public String toString() {
        return "TestDataDto [id=" + id + ", time=" + time + ", value=" + value + "]";
    }
}
