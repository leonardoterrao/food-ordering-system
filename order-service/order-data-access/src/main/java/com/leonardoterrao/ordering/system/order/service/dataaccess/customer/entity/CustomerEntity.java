package com.leonardoterrao.ordering.system.order.service.dataaccess.customer.entity;

import com.leonardoterrao.ordering.system.domain.valueobject.OrderStatus;
import com.leonardoterrao.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.leonardoterrao.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Table(name = "order_customer_m_view", schema = "customer")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CustomerEntity {

    @Id
    private UUID id;

}
