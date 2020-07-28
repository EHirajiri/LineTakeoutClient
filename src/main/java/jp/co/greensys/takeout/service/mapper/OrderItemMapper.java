package jp.co.greensys.takeout.service.mapper;

import jp.co.greensys.takeout.domain.*;
import jp.co.greensys.takeout.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { ItemMapper.class })
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(source = "item.id", target = "itemId")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(target = "ordereds", ignore = true)
    @Mapping(target = "removeOrdered", ignore = true)
    @Mapping(source = "itemId", target = "item")
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    default OrderItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setId(id);
        return orderItem;
    }
}
