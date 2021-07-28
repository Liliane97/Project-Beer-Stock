package one.digitalInnovetione.BeerStock.mapper;

import one.digitalInnovetione.BeerStock.dto.BeerDTO;
import one.digitalInnovetione.BeerStock.entities.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    Beer toModel (BeerDTO beerDTO);

    BeerDTO toDTO (Beer beer);

}
