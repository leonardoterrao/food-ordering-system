package com.leonardoterrao.ordering.system.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity<ID> {

    protected ID id;

}
