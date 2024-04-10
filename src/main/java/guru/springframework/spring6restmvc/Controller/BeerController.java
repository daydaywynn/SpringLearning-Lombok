package guru.springframework.spring6restmvc.Controller;

import guru.springframework.spring6restmvc.Model.Beer;
import guru.springframework.spring6restmvc.Service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController //makes this a Rest Controller here
@Slf4j
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @PostMapping//Another way of doing it
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handlePost(@RequestBody Beer beer){ //Need @RequestBody to assign the body of the post!
        Beer savedBeer = beerService.saveNewBeer(beer);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> listBeers(){
        return beerService.getListOfBeers();
    }

    @RequestMapping(value ="{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId){
        log.debug("Get beer by Id - in Controller");

        return beerService.getBeerById(beerId);
    }

}
