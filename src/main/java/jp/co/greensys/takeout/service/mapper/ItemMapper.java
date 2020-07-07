package jp.co.greensys.takeout.service.mapper;

import jp.co.greensys.takeout.domain.*;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderedMapper.class })
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(source = "ordered.id", target = "orderedId")
    ItemDTO toDto(Item item);

    @Mapping(source = "orderedId", target = "ordered")
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
