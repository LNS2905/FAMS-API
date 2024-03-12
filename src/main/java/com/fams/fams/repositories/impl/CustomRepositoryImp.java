package com.fams.fams.repositories.impl;

import com.fams.fams.repositories.CustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomRepositoryImp implements CustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findSelectedFields(List<String> selectedFields) {
        String selectedFieldsString = String.join(",", selectedFields);
        Query query = entityManager.createNativeQuery("SELECT " + selectedFieldsString + " FROM students");
        return ( List<Object[]> ) query.getResultList();
    }

}
