package ru.mephi.spark.banchmark.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class LatencyKey {

    private final Long id;
    private final Date time;
    private final Integer order;

}
