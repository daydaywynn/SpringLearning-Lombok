package guru.springframework.spring6restmvc.Service;

import guru.springframework.spring6restmvc.Model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<Beer> getListOfBeers();

    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);
}
