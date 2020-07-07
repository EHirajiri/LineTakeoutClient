package jp.co.greensys.takeout.service.mapper;

import jp.co.greensys.takeout.domain.*;
import jp.co.greensys.takeout.service.dto.PayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pay} and its DTO {@link PayDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public interface PayMapper extends EntityMapper<PayDTO, Pay> {
    @Mapping(source = "customer.id", target = "customerId")
    PayDTO toDto(Pay pay);

    @Mapping(target = "ordered", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    Pay toEntity(PayDTO payDTO);

    default Pay fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pay pay = new Pay();
        pay.setId(id);
        return pay;
    }
}
