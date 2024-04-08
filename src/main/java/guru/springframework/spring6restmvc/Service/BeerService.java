package guru.springframework.spring6restmvc.Service;

import guru.springframework.spring6restmvc.Model.Beer;

import java.util.UUID;

public interface BeerService {

    Beer getBeerById(UUID id);
}
