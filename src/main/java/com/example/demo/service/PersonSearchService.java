package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.entry.Person;
import com.example.demo.repository.PersonRepository;

@Service
public class PersonSearchService {

    private final PersonRepository personRepository;

    public PersonSearchService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * ログインユーザー所有の Person を、名前・メールアドレスの部分一致（どちらかまたは両方）で検索する。
     * 両方とも空なら全件（ID 昇順）。ページングは {@code pageable} に従う。
     */
    public Page<Person> search(Long ownerId, String nameQuery, String emailQuery, Pageable pageable) {
        String name = StringUtils.hasText(nameQuery) ? nameQuery.trim() : "";
        String email = StringUtils.hasText(emailQuery) ? emailQuery.trim() : "";

        if (name.isEmpty() && email.isEmpty()) {
            return personRepository.findAllByOwner_IdOrderByIdAsc(ownerId, pageable);
        }
        if (email.isEmpty()) {
            return personRepository.findAllByOwner_IdAndNameContainingIgnoreCaseOrderByIdAsc(ownerId, name, pageable);
        }
        if (name.isEmpty()) {
            return personRepository.findAllByOwner_IdAndEmailContainingIgnoreCaseOrderByIdAsc(ownerId, email, pageable);
        }
        return personRepository.findAllByOwner_IdAndNameContainingIgnoreCaseAndEmailContainingIgnoreCaseOrderByIdAsc(
                ownerId, name, email, pageable);
    }
}
