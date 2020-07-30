package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.util.FlexComponentUtil;
import jp.co.greensys.takeout.util.QueryStringParser;
import org.springframework.util.CollectionUtils;

public class RegisterMessageSupplier implements Supplier<FlexMessage> {
    private final String carts;
    private final List<ItemDTO> itemDTOList;
    private final String deliveryDate;

    public RegisterMessageSupplier(QueryStringParser parser, List<ItemDTO> itemDTOList) {
        this.carts = parser.getUrlQuery("cart");
        this.itemDTOList = itemDTOList;
        this.deliveryDate = String.format("%s %s", parser.getParameterValue("delivery-date"), parser.getParameterValue("delivery-time"));
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("レジ", bubble1);
    }

    private Bubble createBubble() {
        return Bubble.builder().hero(createHeroBlock()).body(createBodyBlock()).footer(createFooterBlock()).build();
    }

    private Box createHeroBlock() {
        final Text titleBlock = FlexComponentUtil.createText("レジ", "#1DB446", FlexFontSize.XL);
        return Box.builder().layout(FlexLayout.VERTICAL).margin(FlexMarginSize.XXL).content(titleBlock).build();
    }

    private Box createHeroBox() {
        final Text titleBlock = FlexComponentUtil.createText("レジ", "#1DB446", FlexFontSize.XL);
        final Image imageBlock = FlexComponentUtil.createImageBlock(
            "https://2.bp.blogspot.com/-IcQD1H8lx5c/VnKNfpw47BI/AAAAAAAA2EY/iVffCXI9_ug/s400/food_zei3_takeout.png"
        );

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(Arrays.asList(titleBlock, imageBlock)).build();
    }

    private Box createBodyBlock() {
        List<FlexComponent> listComponent = new ArrayList<>();

        if (!CollectionUtils.isEmpty(itemDTOList)) {
            // カート内の商品情報
            int total = 0;
            for (ItemDTO itemDTO : itemDTOList) {
                int totalFee = itemDTO.getPrice() * itemDTO.getQuantity();
                final Text itemBlock = FlexComponentUtil.createText(String.format("商品: %s", itemDTO.getName()), null, FlexFontSize.LG);
                final Text totalFeeBlock = FlexComponentUtil.createText(
                    String.format("金額: %s円 × %s個 = %s", itemDTO.getPrice(), itemDTO.getQuantity(), totalFee),
                    null,
                    FlexFontSize.LG
                );
                listComponent.add(
                    Box
                        .builder()
                        .layout(FlexLayout.VERTICAL)
                        .spacing(FlexMarginSize.SM)
                        .contents(itemBlock, totalFeeBlock, FlexComponentUtil.createSeparator())
                        .build()
                );
                total += totalFee;
            }
            final Text totalBlock = FlexComponentUtil.createText(String.format("合計金額: %s円", total), null, FlexFontSize.LG);
            listComponent.add(totalBlock);
            final Text deliveryDateBlock = FlexComponentUtil.createText(String.format("受取日時: %s", deliveryDate), null, FlexFontSize.LG);
        }

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(listComponent).build();
    }

    private Box createFooterBlock() {
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(
                new PostbackAction(
                    "注文を確定する",
                    String.format("type=order&delivery=%s%s&orderId=%s", deliveryDate, carts, UUID.randomUUID()),
                    null
                )
            )
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).content(addToCartEnableButton).build();
    }
}
