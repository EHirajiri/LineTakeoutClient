package jp.co.greensys.takeout.service.mapper;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import jp.co.greensys.takeout.domain.*;
import jp.co.greensys.takeout.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "ordereds", ignore = true)
    @Mapping(target = "removeOrdered", ignore = true)
    @Mapping(target = "pays", ignore = true)
    @Mapping(target = "removePay", ignore = true)
    Customer toEntity(CustomerDTO customerDTO);

    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }

    default CustomerDTO toCustomerDTO(Map body) {
        CustomerDTO dto = new CustomerDTO();
        dto.setUserId((String) ((Map) body.get("user_id")).get("value"));
        dto.setNickname((String) ((Map) body.get("nickname")).get("value"));
        dto.setLanguage((String) ((Map) body.get("language")).get("value"));
        return dto;
    }

    default Map toMap(CustomerDTO dto) {
        return ImmutableMap.of(
            "user_id",
            ImmutableMap.of("value", dto.getUserId()),
            "nickname",
            ImmutableMap.of("value", dto.getNickname()),
            "language",
            ImmutableMap.of("value", dto.getLanguage())
        );
    }
}
