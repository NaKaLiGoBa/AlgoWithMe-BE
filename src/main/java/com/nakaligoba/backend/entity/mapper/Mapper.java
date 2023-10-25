package com.nakaligoba.backend.entity.mapper;

public interface Mapper <D, E> {

    D entityToDomain(E entity);

    E domainToEntity(D domain);
}
