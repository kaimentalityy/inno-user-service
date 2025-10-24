package com.innowise.orderservice.dao.specification;

import com.innowise.orderservice.model.entity.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemsSpecifications {

    public static Specification<Item> hasName(String name) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Item> hasExactName(String name) {
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("name")), name.toLowerCase());
    }

    public static Specification<Item> hasPrice(String price) {
        return (root, query, cb) ->
                cb.equal(root.get("price"), price);
    }
}
