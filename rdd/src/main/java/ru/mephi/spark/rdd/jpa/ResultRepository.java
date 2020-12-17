package ru.mephi.spark.rdd.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ResultRepository extends CrudRepository<ResultEntity, Long> {

    @Query("delete from ResultEntity where time BETWEEN :start AND :end")
    List<MetricEntity> deleteAllByTimeBetween(@Param("start") Date start, @Param("end") Date end);
}
