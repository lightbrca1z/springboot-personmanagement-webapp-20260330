package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entry.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByOwner_IdOrderByIdAsc(Long ownerId);

    Page<Person> findAllByOwner_IdOrderByIdAsc(Long ownerId, Pageable pageable);

    List<Person> findAllByOwner_IdAndNameContainingIgnoreCaseOrderByIdAsc(Long ownerId, String name);

    Page<Person> findAllByOwner_IdAndNameContainingIgnoreCaseOrderByIdAsc(Long ownerId, String name, Pageable pageable);

    List<Person> findAllByOwner_IdAndEmailContainingIgnoreCaseOrderByIdAsc(Long ownerId, String email);

    Page<Person> findAllByOwner_IdAndEmailContainingIgnoreCaseOrderByIdAsc(Long ownerId, String email, Pageable pageable);

    List<Person> findAllByOwner_IdAndNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdAsc(
            Long ownerId, String name, String email);

    Page<Person> findAllByOwner_IdAndNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdAsc(
            Long ownerId, String name, String email, Pageable pageable);

    Optional<Person> findByIdAndOwner_Id(Long id, Long ownerId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Person p WHERE p.owner.id = :ownerId")
    void deleteAllByOwner_Id(@Param("ownerId") Long ownerId);

    List<Person> findByNameLike(String name);

    List<Person> findByIdIsNotNullOrderByIdDesc();

    List<Person> findByAgeGreaterThan(Integer age);

    List<Person> findByAgeBetween(Integer age1, Integer age2);
}
