package com.innowise.orderservice.data.repository;

import com.innowise.orderservice.data.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Items, Long>, JpaSpecificationExecutor<Items> {
}
