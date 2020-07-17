package jp.co.greensys.takeout.service.mapper;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import jp.co.greensys.takeout.domain.Pay;
import jp.co.greensys.takeout.domain.enumeration.DeliveryState;
import jp.co.greensys.takeout.domain.enumeration.PayState;
import jp.co.greensys.takeout.service.dto.PayDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Pay} and its DTO {@link PayDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class, OrderedMapper.class })
public interface PayMapper extends EntityMapper<PayDTO, Pay> {
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.userId", target = "customerUserId")
    @Mapping(source = "ordered.id", target = "orderedId")
    @Mapping(source = "ordered.orderId", target = "orderedOrderId")
    PayDTO toDto(Pay pay);

    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "orderedId", target = "ordered")
    Pay toEntity(PayDTO payDTO);

    default Pay fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pay pay = new Pay();
        pay.setId(id);
        return pay;
    }

    default PayDTO toPayDTO(Map body) {
        PayDTO dto = new PayDTO();
        Map record = (Map) body.get("record");
        dto.setOrderedOrderId((String) ((Map) record.get("order_id")).get("value"));
        dto.setCustomerUserId((String) ((Map) record.get("user_id")).get("value"));
        dto.setTitle((String) ((Map) record.get("title")).get("value"));
        dto.setAmount((Integer) ((Map) record.get("amount")).get("value"));
        dto.setTransactionId(Long.parseLong((String) ((Map) record.get("transaction_id")).get("value")));
        dto.setCurrency((String) ((Map) record.get("currency")).get("value"));
        dto.setPayState(PayState.valueOf((String) ((Map) record.get("pay_state")).get("value")));
        dto.setDeliveryState(DeliveryState.valueOf((String) ((Map) record.get("delivery_state")).get("value")));
        return dto;
    }

    default Map toMap(PayDTO dto) {
        Map map = new HashMap();
        map.put("order_id", ImmutableMap.of("value", dto.getOrderedOrderId()));
        map.put("user_id", ImmutableMap.of("value", dto.getCustomerUserId()));
        map.put("title", ImmutableMap.of("value", dto.getTitle()));
        map.put("amount", ImmutableMap.of("value", dto.getAmount()));
        map.put("transaction_id", ImmutableMap.of("value", dto.getTransactionId()));
        map.put("currency", ImmutableMap.of("value", dto.getCurrency()));
        map.put("pay_state", ImmutableMap.of("value", dto.getPayState()));
        map.put("delivery_state", ImmutableMap.of("value", dto.getDeliveryState()));
        return map;
    }
}
