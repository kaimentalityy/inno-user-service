package com.innowise.orderservice.data.specification;

import com.innowise.orderservice.data.entity.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecifications {

    public static Specification<Order> hasUserId(String userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<Order> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Order> createdAfter(LocalDateTime date) {
        return (root, query, cb) -> cb.greaterThan(root.get("createdDate"), date);
    }

    public static Specification<Order> createdBefore(LocalDateTime date) {
        return (root, query, cb) -> cb.lessThan(root.get("createdDate"), date);
    }
}
