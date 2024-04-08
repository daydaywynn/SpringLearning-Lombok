package guru.springframework.spring6restmvc.Controller;

import guru.springframework.spring6restmvc.Model.Beer;
import guru.springframework.spring6restmvc.Service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@AllArgsConstructor
@Controller
@Slf4j
public class BeerController {
    private final BeerService beerService;


    public Beer getBeerById(UUID id){
        log.debug("Get beer by Id - in Controller");

        return beerService.getBeerById(id);
    }
}
