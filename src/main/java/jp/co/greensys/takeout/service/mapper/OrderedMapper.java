package jp.co.greensys.takeout.service.mapper;

import jp.co.greensys.takeout.domain.*;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ordered} and its DTO {@link OrderedDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class, ItemMapper.class })
public interface OrderedMapper extends EntityMapper<OrderedDTO, Ordered> {
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "item.id", target = "itemId")
    OrderedDTO toDto(Ordered ordered);

    @Mapping(target = "pays", ignore = true)
    @Mapping(target = "removePay", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "itemId", target = "item")
    Ordered toEntity(OrderedDTO orderedDTO);

    default Ordered fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ordered ordered = new Ordered();
        ordered.setId(id);
        return ordered;
    }
}
