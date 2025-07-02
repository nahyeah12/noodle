package com.ppi.utility.importer.repository;

import com.ppi.utility.importer.model.CaseMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository for the CaseMaster entity.
 * Provides standard CRUD operations and query capabilities.
 */
@Repository
public interface CaseMasterRepository extends JpaRepository<CaseMaster, String> {
    // JpaRepository provides methods like save(), findById(), findAll(), delete(), etc.
    // No custom methods are needed based on the current requirements.
}
