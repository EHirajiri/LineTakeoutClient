package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.util.FlexComponentUtil;
import jp.co.greensys.takeout.util.QueryStringParser;
import org.springframework.util.CollectionUtils;

public class CartMessageSupplier implements Supplier<FlexMessage> {
    private final String carts;
    private final List<ItemDTO> itemDTOList;

    public CartMessageSupplier(QueryStringParser parser, List<ItemDTO> itemDTOList) {
        this.carts = parser.getUrlQuery("cart");
        this.itemDTOList = itemDTOList;
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("カート", bubble1);
    }

    private Bubble createBubble() {
        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        return Bubble.builder().hero(createHeroBlock()).body(bodyBlock).footer(footerBlock).build();
    }

    private Box createHeroBlock() {
        final Text titleBlock = FlexComponentUtil.createText("カート", "#1DB446", FlexFontSize.XL);
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.LG).content(titleBlock).build();
    }

    private Box createBodyBlock() {
        List<FlexComponent> listComponent = new ArrayList<>();

        if (!CollectionUtils.isEmpty(itemDTOList)) {
            // カート内の商品情報
            for (ItemDTO itemDTO : itemDTOList) {
                final Text itemBlock = FlexComponentUtil.createText(String.format("商品: %s", itemDTO.getName()), null, FlexFontSize.LG);
                final Text totalFeeBlock = FlexComponentUtil.createText(
                    String.format(
                        "金額: %s円 × %s個 = %s",
                        itemDTO.getPrice(),
                        itemDTO.getQuantity(),
                        itemDTO.getPrice() * itemDTO.getQuantity()
                    ),
                    null,
                    FlexFontSize.LG
                );
                listComponent.add(
                    Box
                        .builder()
                        .layout(FlexLayout.VERTICAL)
                        .spacing(FlexMarginSize.SM)
                        .contents(FlexComponentUtil.getSeparator(), itemBlock, totalFeeBlock)
                        .build()
                );
            }
        }

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(listComponent).build();
    }

    private Box createFooterBlock() {
        final Button continueButton = Button
            .builder()
            .style(Button.ButtonStyle.SECONDARY)
            .action(new PostbackAction("さらに商品を追加する", String.format("type=re-menu%s", carts), null))
            .build();
        final Button proceedButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction("買い物を続ける", String.format("type=delivery%s", carts), null))
            .build();

        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(continueButton, proceedButton))
            .build();
    }
}
