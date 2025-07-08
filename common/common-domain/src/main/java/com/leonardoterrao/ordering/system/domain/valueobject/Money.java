package com.leonardoterrao.ordering.system.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@EqualsAndHashCode(of = "amount")
@RequiredArgsConstructor
public class Money {

    private final BigDecimal amount;

    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGraterThan(final Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
    }

    public Money add(final Money money) {
        return new Money(setScale(this.amount.add(money.getAmount())));
    }

    public Money subtract(final Money money) {
        return new Money(setScale(this.amount.subtract(money.getAmount())));
    }

    public Money multiply(final BigDecimal multiplier) {
        return new Money(setScale(this.amount.multiply(multiplier)));
    }

    private static BigDecimal setScale(final BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

}
