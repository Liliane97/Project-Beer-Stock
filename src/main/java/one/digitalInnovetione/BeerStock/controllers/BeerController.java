package one.digitalInnovetione.BeerStock.controllers;

import lombok.AllArgsConstructor;
import one.digitalInnovetione.BeerStock.dto.BeerDTO;
import one.digitalInnovetione.BeerStock.dto.QuantityDTO;
import one.digitalInnovetione.BeerStock.exception.BeerAlreadyRegisteredException;
import one.digitalInnovetione.BeerStock.exception.BeerNotFoundException;
import one.digitalInnovetione.BeerStock.exception.BeerStockExceededException;
import one.digitalInnovetione.BeerStock.services.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController{
    private final BeerService beerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDTO createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        return  beerService.createBeer(beerDTO);
    }

    @GetMapping
    public List<BeerDTO> listBeers() {
        return beerService.listAll();
    }

    @GetMapping("/{name}")
    public BeerDTO findByName (@PathVariable String name) throws BeerNotFoundException {
        return beerService.findByName(name);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteById(@PathVariable Long id) throws BeerNotFoundException {
        beerService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public BeerDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceededException {
        return beerService.increment(id, quantityDTO.getQuantity());
    }

    @PatchMapping("/{id}/decrement")
    public BeerDTO decrement(@PathVariable Long id,@RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceededException {
        return  beerService.decrement(id, quantityDTO.getQuantity());
    }
}
