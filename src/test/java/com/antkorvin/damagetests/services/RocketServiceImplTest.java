package com.antkorvin.damagetests.services;

import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.repositories.RocketRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.Repeat;

import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Korovin Anatolii on 11.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class RocketServiceImplTest {

    @InjectMocks
    private RocketServiceImpl rocketService;

    @Mock
    private RocketRepository rocketRepository;

    private ArgumentCaptor<Rocket> rocketCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        rocketCaptor = ArgumentCaptor.forClass(Rocket.class);
    }


    @Test
    public void create() {
        // Arrange
        Rocket rocket = mock(Rocket.class);
        String boom = "boom";
        when(rocketRepository.save(any(Rocket.class))).thenReturn(rocket);

        // Act
        Rocket result = rocketService.create(boom);

        // Asserts
        Assertions.assertThat(result)
                  .isNotNull()
                  .isEqualTo(rocket);

        ArgumentCaptor<Rocket> captor = ArgumentCaptor.forClass(Rocket.class);
        verify(rocketRepository).save(captor.capture());

        Assertions.assertThat(captor.getValue())
                  .extracting(Rocket::getName)
                  .contains(boom);
    }

    @Test
    public void checkLaunchCodeUniqueAfterCreate() {

        // Act
        rocketService.create("Spring");
        rocketService.create("Java");

        // Asserts
        verify(rocketRepository, times(2)).save(rocketCaptor.capture());

        Stream<String> codes = rocketCaptor.getAllValues()
                                           .stream()
                                           .map(Rocket::getLaunchCode)
                                           .peek(System.out::println)
                                           .distinct();

        Assertions.assertThat(codes)
                  .hasSize(2);
    }


    @Test
    public void checkCodeFormat() {

        // Arrange
        int rocketCounter = 100;

        // Act
        IntStream.range(0, rocketCounter)
                 .mapToObj(String::valueOf)
                 .parallel()
                 .forEach(rocketService::create);

        // Asserts
        verify(rocketRepository, times(rocketCounter)).save(rocketCaptor.capture());

        Assertions.assertThat(rocketCaptor.getAllValues())
                  .extracting(Rocket::getLaunchCode)
                  .allMatch(s -> {
                      System.out.println(s);
                      return true;
                  })
                  .allMatch(s -> Pattern.matches("^[a-f0-9]{5}$", s));
    }


    @Test
    public void testGetRocket() {
        // Arrange
        UUID id = UUID.randomUUID();
        Rocket rocket = mock(Rocket.class);
        when(rocketRepository.findOne(eq(id))).thenReturn(rocket);

        // Act
        Rocket result = rocketService.get(id);

        // Asserts
        Assertions.assertThat(result)
                  .isNotNull()
                  .isEqualTo(rocket);

        verify(rocketRepository).findOne(eq(id));
    }


    //TODO: add except cases with Guard
}