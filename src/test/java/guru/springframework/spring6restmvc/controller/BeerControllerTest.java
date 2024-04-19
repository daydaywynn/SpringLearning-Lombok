package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    public static final String BEER_PATH = "/api/v1/beer";
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;


    /*The argument Captor sits on the mock and listens for anything that's passed in*/
    @Captor
    ArgumentCaptor<UUID> argumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> argumentCaptorBeer;


    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();

    @Test
    void GetBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID,UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
    @Test
    void testPatchbeers() throws Exception{
        BeerDTO beer = beerServiceImpl.listBeers().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(argumentCaptor.capture(), argumentCaptorBeer.capture());

        assertThat(beer.getId()).isEqualTo(argumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(argumentCaptorBeer.getValue().getBeerName());
    }
    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getBeerById() throws Exception {
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BEER_PATH + "/" + testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void tesUpdateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(put(BEER_PATH + "/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))).andExpect(status().isNoContent());

        verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }
    @Test
    void createNewBeer() throws Exception {

        BeerDTO beer = beerServiceImpl.listBeers().get(0);
        System.out.println(objectMapper.writeValueAsString(beer));

        beer.setVersion(null);
        beer.setCreatedDate(null);
        beer.setUpdateDate(null);

        given(beerService.saveNewBeer(beer)).willReturn(beerServiceImpl.listBeers().get(1));


        mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void TestDeleteBeer() throws Exception{
        BeerDTO beer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(delete(BEER_PATH + "/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteById(argumentCaptor.capture());
        //Get hte value that was passed into the mock
        assertThat(beer.getId()).isEqualTo(argumentCaptor.getValue());
    }
}