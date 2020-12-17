package ru.mephi.spark.rdd.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MetricRepository extends CrudRepository<MetricEntity, Long> {

    long count();

    @Query("from MetricEntity where time BETWEEN :start AND :end")
    List<MetricEntity> findAllByTimeBetween(@Param("start") Date start, @Param("end") Date end);
}
