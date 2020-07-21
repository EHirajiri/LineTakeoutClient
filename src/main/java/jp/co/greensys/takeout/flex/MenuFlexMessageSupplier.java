package jp.co.greensys.takeout.flex;

import static java.util.Arrays.asList;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
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

public class MenuFlexMessageSupplier implements Supplier<FlexMessage> {
    private final List<ItemDTO> itemDTOS;

    public MenuFlexMessageSupplier(List<ItemDTO> itemDTOS) {
        this.itemDTOS = itemDTOS;
    }

    @Override
    public FlexMessage get() {
        List<Bubble> bubbles = new ArrayList();
        for (ItemDTO itemDTO : itemDTOS) {
            Bubble bubble = createBubble(itemDTO.getName(), itemDTO.getPrice(), itemDTO.getImageUrl(), itemDTO.getId());
            bubbles.add(bubble);
        }
        final Carousel carousel = Carousel.builder().contents(bubbles).build();
        return new FlexMessage("Menu", carousel);
    }

    private Bubble createBubble(String title, Integer price, String imageURL, Long itemId) {
        final Image heroBlock = createHeroBlock(imageURL);
        final Box bodyBlock = createBodyBlock(title, price);
        final Box footerBlock = createFooterBlock(itemId);
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

    private Box createBodyBlock(String title, Integer price) {
        final Text titleBlock = Text.builder().text(title).wrap(true).weight(Text.TextWeight.BOLD).size(FlexFontSize.XL).build();
        final Box priceBlock = Box
            .builder()
            .layout(FlexLayout.BASELINE)
            .contents(
                asList(Text.builder().text("￥" + price).wrap(true).weight(Text.TextWeight.BOLD).size(FlexFontSize.XL).flex(0).build())
            )
            .build();

        FlexComponent[] flexComponents = { titleBlock, priceBlock };
        List<FlexComponent> listComponent = new ArrayList<>(Arrays.asList(flexComponents));

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(listComponent).build();
    }

    private Box createFooterBlock(Long itemId) {
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction("注文する", "type=select&item=" + itemId, null))
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(asList(addToCartEnableButton)).build();
    }
}
