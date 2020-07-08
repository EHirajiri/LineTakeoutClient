package jp.co.greensys.takeout.service.mapper;

import jp.co.greensys.takeout.domain.*;
import jp.co.greensys.takeout.service.dto.PayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pay} and its DTO {@link PayDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderedMapper.class, CustomerMapper.class })
public interface PayMapper extends EntityMapper<PayDTO, Pay> {
    @Mapping(source = "ordered.id", target = "orderedId")
    @Mapping(source = "ordered.orderId", target = "orderedOrderId")
    @Mapping(source = "ordered.createdDate", target = "orderedCreatedDate")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.userId", target = "customerUserId")
    PayDTO toDto(Pay pay);

    @Mapping(source = "orderedId", target = "ordered")
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
