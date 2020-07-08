package jp.co.greensys.takeout.service.mapper;

import jp.co.greensys.takeout.domain.*;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    ItemDTO toDto(Item item);

    @Mapping(target = "ordereds", ignore = true)
    @Mapping(target = "removeOrdered", ignore = true)
    Item toEntity(ItemDTO itemDTO);

    default Item fromId(Long id) {
        if (id == null) {
            return null;
        }
        Item item = new Item();
        item.setId(id);
        return item;
    }
}
