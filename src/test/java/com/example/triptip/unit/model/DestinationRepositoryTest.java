package com.example.triptip.unit.model;

import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.destination.DestinationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
class DestinationRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    DestinationRepository repository;

    Destination destination;
    Long id = 11L;
    String name = "Jakarta";
    String description = """
            Jakarta (/dʒəˈkɑːrtə/; Indonesian pronunciation: [dʒaˈkarta] (listen), Betawi: Jakarte, formerly Dutch: Batavia), 
            officially the Special Capital Region of Jakarta (Indonesian: Daerah Khusus Ibukota Jakarta), is the capital and 
            largest city of Indonesia. Lying on the north-west coast of Java, the world's most populous island, Jakarta is the 
            largest city in Southeast Asia, and serves as the diplomatic capital of ASEAN.\s""";
    Set<Tag> tagList = EnumSet.of(Tag.MUSEUMS, Tag.HIKING);

    @BeforeEach
    void setUp() {
        destination = new Destination();
        destination.setName(name);
        destination.setDescription(description);
        destination.addTag(Tag.MUSEUMS);
        destination.addTag(Tag.HIKING);
        entityManager.merge(destination);
    }

    @Test
    void givenCorrectId_whenFindById_returnDestination() {
        Optional<Destination> test = repository.findByName(destination.getName());

        if(test.isEmpty()) Assertions.fail();
        Destination testDestination = test.get();

        assertThat(testDestination.getName(), equalTo(name));
        assertThat(testDestination.getDescription(), equalTo(description));
        assertThat(testDestination.getTags(), containsInAnyOrder(Tag.MUSEUMS, Tag.HIKING));
    }

    @Test
    void whenSetMethodsRun_thenReturnDifferentValues() {
        destination.addTag(Tag.BEACHES);
        destination.removeTag(Tag.HIKING);
        destination.setName("Los Angeles");
        destination.setDescription("California dreaming...");

        repository.saveAndFlush(destination);

        Optional<Destination> test = repository.findByName(destination.getName());

        if(test.isEmpty()) Assertions.fail();
        Destination testDestination = test.get();

        assertThat(testDestination.getName(), equalTo("Los Angeles"));
        assertThat(testDestination.getDescription(), equalTo("California dreaming..."));
        assertThat(testDestination.getTags(), containsInAnyOrder(Tag.BEACHES, Tag.MUSEUMS));
    }

    @Test
    void givenNewDestination_whenFindAll_returnAllDestinations() {
        Destination newDestination = new Destination();
        newDestination.setId(12L);
        newDestination.setName("notNullName");
        repository.save(newDestination);

        assertThat(repository.findAll(), hasSize(2));
    }
}
