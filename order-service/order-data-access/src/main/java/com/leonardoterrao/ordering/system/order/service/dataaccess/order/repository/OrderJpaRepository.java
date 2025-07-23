package com.leonardoterrao.ordering.system.order.service.dataaccess.order.repository;

import com.leonardoterrao.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.leonardoterrao.ordering.system.order.service.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    Optional<OrderEntity> findByTrackingId(UUID trackingId);

}
