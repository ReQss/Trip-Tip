package com.example.triptip.unit.service;

import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.destination.DestinationRepository;
import com.example.triptip.service.DestinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DestinationServiceTest {

    @Mock(lenient = true)
    DestinationRepository repository;

    @InjectMocks
    DestinationService service;

    long id1 = 111L;
    String name1 = "Jakarta";
    String description1 = """
            Jakarta (/dʒəˈkɑːrtə/; Indonesian pronunciation: [dʒaˈkarta] (listen), Betawi: Jakarte, formerly Dutch: Batavia), 
            officially the Special Capital Region of Jakarta (Indonesian: Daerah Khusus Ibukota Jakarta), is the capital and 
            largest city of Indonesia. Lying on the north-west coast of Java, the world's most populous island, Jakarta is the 
            largest city in Southeast Asia, and serves as the diplomatic capital of ASEAN.\s""";

    Set<Tag> tagSet1 = EnumSet.of(Tag.MUSEUMS, Tag.HIKING);
    Destination destination1;

    long id2 = 112L;
    String name2 = "Porto";
    String description2 = "short description";

    Set<Tag> tagSet2 = EnumSet.of(Tag.MOUNTAINS, Tag.HIKING);
    Destination destination2;

    List<Destination> destinationList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        //destination1 = new Destination(id1, name1, description1, tagSet1);
        //destination2 = new Destination(id2, name2, description2, tagSet2);

        destinationList.add(destination1);
        destinationList.add(destination2);

        when(repository.findByName(name1)).thenReturn(Optional.of(destination1));
        when(repository.findByName(name2)).thenReturn(Optional.of(destination2));
        when(repository.existsByName(and(AdditionalMatchers.not(Mockito.eq(name1)), AdditionalMatchers.not(Mockito.eq(name2))))).thenReturn(false);
        when(repository.existsByName(name1)).thenReturn(true);
        when(repository.existsByName(name2)).thenReturn(true);
        when(repository.findAll()).thenReturn(destinationList);
    }

    @Test
    void givenNameAndDescription_whenCreateDestination_thenReturnDestination() {
        String newName = "newName";
        String newDescription = "description";

        //Destination destination = new Destination(1L, newName, newDescription, null);

        //when(repository.saveAndFlush(Mockito.any())).thenReturn(destination);

        //assertThat(service.createDestination("newName", "newDescription"), equalTo(destination));
    }

    @Test
    void givenWrongNameAndDescription_whenCreateDestination_thenReturnNull() {

        when(repository.saveAndFlush(Mockito.any())).thenReturn(null);

        //assertThat(service.createDestination(name1, description1), nullValue());
    }

    @Test
    void givenExistentName_whenReadDestination_thenReturnOptionalOfDestination() {
        assertThat(service.readDestination(name1), equalTo(Optional.of(destination1)));
    }

    @Test
    void givenNonExistentName_whenReadDestination_thenReturnOptionalOfNull() {
        assertThat(service.readDestination("null"), equalTo(Optional.empty()));
    }

    @Test
    void whenReadAllDestination_thenReturnListOfDestinations() {
        assertThat(service.readAllDestinations(), containsInAnyOrder(destination1, destination2));
    }

    @Test
    void givenNewName_whenUpdateName_thenReturnTrue() {
        assertThat(service.updateName(name1, "newName"), equalTo(true));
    }

    @Test
    void givenWrongCurrentName_whenUpdateName_thenReturnFalse() {
        assertThat(service.updateName("wrong", "nothing"), equalTo(false));
    }

    @Test
    void givenAlreadyExistentNewName_whenUpdateName_thenReturnFalse() {
        assertThat(service.updateName(name1, name2), equalTo(false));
    }

    @Test
    void givenExistentDestinationName_whenUpdateDescription_thenReturnTrue() {
        assertThat(service.updateDescription(name1, "new description"), equalTo(true));
    }

    @Test
    void givenWrongDestinationName_whenUpdateDescription_thenReturnFalse() {
        assertThat(service.updateDescription("wrong", "doesn't matter"), equalTo(false));
    }

    @Test
    void givenCorrectName_whenAddDestinationTag_thenReturnTrue() {
        assertThat(service.addDestinationTag(name1, Tag.MOUNTAINS), equalTo(true));
    }

    @Test
    void givenWrongName_whenAddDestinationTag_thenReturnFalse() {
        assertThat(service.addDestinationTag("wrong", Tag.MOUNTAINS), equalTo(false));
    }

    @Test
    void givenCorrectName_whenDeleteDestinationTag_thenReturnTrue() {
        assertThat(service.deleteDestinationTag(name1, Tag.HIKING), equalTo(true));
    }

    @Test
    void givenWrongName_whenDeleteDestinationTag_thenReturnFalse() {
        assertThat(service.deleteDestinationTag("wrong", Tag.HIKING), equalTo(false));
    }

    @Test
    void givenCorrectName_whenDeleteDestination_thenReturnTrue() {
        assertThat(service.deleteDestination(name1), equalTo(true));
    }

    @Test
    void givenWrongName_whenDeleteDestination_thenReturnFalse() {
        assertThat(service.deleteDestination("wrong"), equalTo(false));
    }
}