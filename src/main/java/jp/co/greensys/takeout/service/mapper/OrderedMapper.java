package jp.co.greensys.takeout.service.mapper;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import jp.co.greensys.takeout.domain.Ordered;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Ordered} and its DTO {@link OrderedDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class, ItemMapper.class, PayMapper.class })
public interface OrderedMapper extends EntityMapper<OrderedDTO, Ordered> {
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.userId", target = "customerUserId")
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "itemName")
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

    default OrderedDTO toOrderedDTO(Map body) {
        OrderedDTO dto = new OrderedDTO();
        Map record = (Map) body.get("record");
        dto.setOrderId((String) ((Map) record.get("order_id")).get("value"));
        dto.setCustomerUserId((String) ((Map) record.get("user_id")).get("value"));
        dto.setItemId(Long.parseLong((String) ((Map) record.get("item_id")).get("value")));
        dto.setUnitPrice((Integer) ((Map) record.get("unit_price")).get("value"));
        dto.setQuantity(Integer.parseInt((String) ((Map) record.get("quantity")).get("value")));
        return dto;
    }

    default Map toMap(OrderedDTO dto) {
        Map map = new HashMap();
        map.put("order_id", ImmutableMap.of("value", dto.getOrderId()));
        map.put("user_id", ImmutableMap.of("value", dto.getCustomerUserId()));
        map.put("item_id", ImmutableMap.of("value", dto.getItemId()));
        map.put("item_name", ImmutableMap.of("value", dto.getItemName()));
        map.put("unit_price", ImmutableMap.of("value", dto.getUnitPrice()));
        map.put("quantity", ImmutableMap.of("value", dto.getQuantity()));
        return map;
    }
}
