package com.innowise.userservice.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.innowise.userservice.data.entity.CardInfo;

import java.util.List;

/**
 * Repository interface for {@link CardInfo} entities.
 * <p>
 * Provides CRUD operations, as well as custom methods for querying cards
 * by user ID, multiple card IDs, or card number. Supports both JPQL and native SQL queries.
 * </p>
 */
@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> , JpaSpecificationExecutor<CardInfo> {

    /**
     * Finds all cards associated with a specific user by their user ID.
     *
     * @param userId the ID of the user
     * @return a list of {@link CardInfo} objects belonging to the user
     */
    List<CardInfo> findByUserId(Long userId);

    /**
     * Finds all cards with IDs in the specified list using JPQL.
     *
     * @param ids a list of card IDs
     * @return a list of {@link CardInfo} objects whose IDs match the given list
     */
    @Query("SELECT c FROM CardInfo c WHERE c.id IN :ids")
    List<CardInfo> findCardsByIds(@Param("ids") List<Long> ids);

    /**
     * Finds a card by its number using a native SQL query.
     *
     * @param cardNumber the card number to search for
     * @return the {@link CardInfo} object with the given card number, or null if not found
     */
    @Query(value = "SELECT * FROM card_info WHERE card_number = :cardNumber", nativeQuery = true)
    CardInfo findByCardNumberNative(@Param("cardNumber") String cardNumber);

}
