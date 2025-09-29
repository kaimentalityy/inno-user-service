package com.innowise.userservice.data.specification;

import com.innowise.userservice.data.entity.CardInfo;
import org.springframework.data.jpa.domain.Specification;

public class CardInfoSpecifications {

    public static Specification<CardInfo> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<CardInfo> hasCardNumber(String cardNumber) {
        return (root, query, cb) -> cb.equal(root.get("cardNumber"), cardNumber);
    }

    public static Specification<CardInfo> hasCardHolder(String cardHolder) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("cardHolder")), "%" + cardHolder.toLowerCase() + "%");
    }
}
