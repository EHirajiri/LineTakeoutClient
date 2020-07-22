package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.util.DateTimeUtil;
import jp.co.greensys.takeout.util.FlexComponentUtil;
import jp.co.greensys.takeout.util.QueryStringParser;

public class OrderMessageSupplier implements Supplier<FlexMessage> {
    private final ItemDTO itemDTO;
    private final int quantity;
    private final ZonedDateTime deliveryDate;
    private final int totalFee;
    private final String orderId;

    public OrderMessageSupplier(ItemDTO itemDTO, QueryStringParser parser) {
        this.itemDTO = itemDTO;
        this.quantity = Integer.parseInt(parser.getParameterValue("quantity"));
        this.deliveryDate = DateTimeUtil.parseZonedDateTime(parser.getParameterValue("deliveryDate"));
        this.totalFee = itemDTO.getPrice() * this.quantity;
        this.orderId = parser.getParameterValue("orderId");
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("Order", bubble1);
    }

    private Bubble createBubble() {
        return Bubble.builder().hero(createHeroBox()).body(createBodyBlock()).footer(createFooterBlock()).build();
    }

    private Box createHeroBox() {
        final Text titleBlock = FlexComponentUtil.createText("レジ", "#1DB446", FlexFontSize.XL);
        final Image imageBlock = createImageBlock(
            "https://2.bp.blogspot.com/-IcQD1H8lx5c/VnKNfpw47BI/AAAAAAAA2EY/iVffCXI9_ug/s400/food_zei3_takeout.png"
        );

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(Arrays.asList(titleBlock, imageBlock)).build();
    }

    private Box createBodyBlock() {
        // 商品情報
        final Text itemBlock = FlexComponentUtil.createText(String.format("商品: %s", itemDTO.getName()), null, FlexFontSize.LG);
        final Text calcBlock = FlexComponentUtil.createText(
            String.format("[%s円 × %s個]", itemDTO.getPrice(), quantity),
            "#555555",
            FlexFontSize.SM
        );
        final Box itemBox = Box.builder().layout(FlexLayout.VERTICAL).contents(Arrays.asList(itemBlock, calcBlock)).build();

        // 合計金額
        final Text totalFeeBlock = FlexComponentUtil.createText(String.format("合計: %s円", totalFee), null, FlexFontSize.LG);

        // 受け取り日時
        final Text deliveryDateBlock = FlexComponentUtil.createText(
            String.format("受取日時： %s", DateTimeUtil.parseString(deliveryDate)),
            null,
            FlexFontSize.LG
        );

        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(itemBox, totalFeeBlock, deliveryDateBlock))
            .build();
    }

    private Image createImageBlock(String imageURL) {
        return Image
            .builder()
            .size(Image.ImageSize.FULL_WIDTH)
            .aspectRatio(Image.ImageAspectRatio.R1TO1)
            .aspectMode(Image.ImageAspectMode.Cover)
            .url(URI.create(imageURL))
            .build();
    }

    private Box createFooterBlock() {
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(
                new PostbackAction(
                    "注文を確定する",
                    String.format(
                        "type=ordered&item=%s&unitPrice=%s&quantity=%s&totalFee=%s&deliveryDate=%s&orderId=%s",
                        itemDTO.getId(),
                        itemDTO.getPrice(),
                        quantity,
                        totalFee,
                        deliveryDate,
                        orderId
                    ),
                    null
                )
            )
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).content(addToCartEnableButton).build();
    }
}
