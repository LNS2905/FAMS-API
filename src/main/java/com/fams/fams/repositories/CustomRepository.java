package com.fams.fams.repositories;

import java.util.List;

public interface CustomRepository {
    List<Object[]> findSelectedFields(List<String> selectedFields);
}
