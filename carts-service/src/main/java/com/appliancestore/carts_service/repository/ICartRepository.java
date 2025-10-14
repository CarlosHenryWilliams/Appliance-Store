package com.appliancestore.carts_service.repository;

import com.appliancestore.carts_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
}
