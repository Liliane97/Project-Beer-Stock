package one.digitalInnovetione.BeerStock.services;

import lombok.AllArgsConstructor;
import one.digitalInnovetione.BeerStock.dto.BeerDTO;
import one.digitalInnovetione.BeerStock.entities.Beer;
import one.digitalInnovetione.BeerStock.exception.BeerAlreadyRegisteredException;
import one.digitalInnovetione.BeerStock.exception.BeerNotFoundException;
import one.digitalInnovetione.BeerStock.exception.BeerStockExceededException;
import one.digitalInnovetione.BeerStock.mapper.BeerMapper;
import one.digitalInnovetione.BeerStock.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    private void verifyIsAlreadyRegistered(String name) throws  BeerAlreadyRegisteredException {
        Optional<Beer> optSavedBeer = beerRepository.findByName(name);
        if(optSavedBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }

    public BeerDTO createBeer ( BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIsAlreadyRegistered(beerDTO.getName());
        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);

        return beerMapper.toDTO((savedBeer));
    }
    public List<BeerDTO> listAll() {
        return beerRepository.findAll()
                .stream()
                .map((beerMapper::toDTO))
                .collect(Collectors.toList());
    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer foundBeer = beerRepository.findByName(name)
                .orElseThrow(()-> new BeerNotFoundException(name));

        return  beerMapper.toDTO((foundBeer));
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyIfExists(id);
        beerRepository.deleteById(id);
    }

    private Beer verifyIfExists(Long id) throws BeerNotFoundException {
        return beerRepository.findById(id)
                .orElseThrow(()->new BeerNotFoundException(id));
    }

    public BeerDTO increment(Long id , int quantityToIncrement) throws  BeerNotFoundException, BeerStockExceededException {
        Beer beerToIncrementStock = verifyIfExists(id);
        int quantityAfterIncrement = quantityToIncrement + beerToIncrementStock.getQuantity();

        if (quantityAfterIncrement <= beerToIncrementStock.getMax()) {
            beerToIncrementStock.setQuantity(beerToIncrementStock.getQuantity() + quantityToIncrement);
            Beer incrementBeerStock = beerRepository.save(beerToIncrementStock);
            return beerMapper.toDTO((incrementBeerStock));
        }
        throw new BeerStockExceededException(id,quantityToIncrement);
    }

    public BeerDTO decrement(Long id, int quantityToDecrement) throws  BeerNotFoundException, BeerStockExceededException  {
        Beer beerToDecrementStock = verifyIfExists(id);
        int beerStockAfterDecremented =beerToDecrementStock.getQuantity() - quantityToDecrement;

        if(beerStockAfterDecremented >= 0) {
            beerToDecrementStock.setQuantity(beerStockAfterDecremented);
            Beer decrementedBeerStock = beerRepository.save(beerToDecrementStock);
            return beerMapper.toDTO(decrementedBeerStock);
        }
        throw  new BeerStockExceededException(id,quantityToDecrement);
    }
}
