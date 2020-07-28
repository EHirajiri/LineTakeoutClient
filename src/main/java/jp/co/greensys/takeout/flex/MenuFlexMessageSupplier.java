package jp.co.greensys.takeout.flex;

import static java.util.Arrays.asList;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.util.FlexComponentUtil;
import jp.co.greensys.takeout.util.QueryStringParser;

public class MenuFlexMessageSupplier implements Supplier<FlexMessage> {
    private final List<ItemDTO> itemDTOS;
    private final String carts;

    public MenuFlexMessageSupplier(List<ItemDTO> itemDTOS, QueryStringParser parser) {
        this.itemDTOS = itemDTOS;
        if (parser != null) {
            this.carts = parser.getUrlQuery("cart");
        } else {
            this.carts = new String();
        }
    }

    @Override
    public FlexMessage get() {
        List<Bubble> bubbles = new ArrayList();
        for (ItemDTO itemDTO : itemDTOS) {
            Bubble bubble = createBubble(itemDTO);
            bubbles.add(bubble);
        }
        final Carousel carousel = Carousel.builder().contents(bubbles).build();
        return new FlexMessage("Menu", carousel);
    }

    private Bubble createBubble(ItemDTO itemDTO) {
        final Image heroBlock = createHeroBlock(itemDTO.getImageUrl());
        final Box bodyBlock = createBodyBlock(itemDTO);
        final Box footerBlock = createFooterBlock(itemDTO.getId());
        return Bubble.builder().hero(heroBlock).body(bodyBlock).footer(footerBlock).build();
    }

    private Image createHeroBlock(String imageURL) {
        return Image
            .builder()
            .size(Image.ImageSize.FULL_WIDTH)
            .aspectRatio(Image.ImageAspectRatio.R1TO1)
            .aspectMode(Image.ImageAspectMode.Cover)
            .url(URI.create(imageURL))
            .build();
    }

    private Box createBodyBlock(ItemDTO itemDTO) {
        final Text titleBlock = FlexComponentUtil.createText(itemDTO.getName(), null, FlexFontSize.XL);
        final Text priceBlock = FlexComponentUtil.createText("￥" + itemDTO.getPrice(), null, FlexFontSize.XL);
        final Text descriptionBlock = FlexComponentUtil.createText(itemDTO.getDescription(), null, FlexFontSize.XXS);
        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(titleBlock, priceBlock, descriptionBlock))
            .build();
    }

    private Box createFooterBlock(Long itemId) {
        final String postData = String.format("type=quantity&item=%s%s", itemId, carts);
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction("カートに追加する", postData, null))
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(asList(addToCartEnableButton)).build();
    }
}
