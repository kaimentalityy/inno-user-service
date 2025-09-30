package com.innowise.orderservice.data.specification;

import com.innowise.orderservice.data.entity.Items;
import org.springframework.data.jpa.domain.Specification;

public class ItemsSpecifications {

    public static Specification<Items> hasName(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Items> hasExactName(String name) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("name")), name.toLowerCase());
    }

    public static Specification<Items> hasPrice(String price) {
        return (root, query, cb) ->
                cb.equal(root.get("price"), price);
    }
}
