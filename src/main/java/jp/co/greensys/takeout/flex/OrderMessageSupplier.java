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
import java.util.Arrays;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.ItemDTO;
import jp.co.greensys.takeout.util.FlexComponentUtil;

public class OrderMessageSupplier implements Supplier<FlexMessage> {
    private final ItemDTO itemDTO;
    private final int quantity;
    private final String deliveryDate;

    public OrderMessageSupplier(ItemDTO itemDTO, String quantity, String deliveryDate) {
        this.itemDTO = itemDTO;
        this.quantity = Integer.parseInt(quantity);
        this.deliveryDate = deliveryDate;
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
        final Text titleBlock = FlexComponentUtil.createText("レジ", FlexFontSize.XL);
        final Image imageBlock = createImageBlock(
            "https://2.bp.blogspot.com/-IcQD1H8lx5c/VnKNfpw47BI/AAAAAAAA2EY/iVffCXI9_ug/s400/food_zei3_takeout.png"
        );

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(Arrays.asList(titleBlock, imageBlock)).build();
    }

    private Box createBodyBlock() {
        // 商品情報
        final Text itemBlock = FlexComponentUtil.createText(String.format("商品: %s", itemDTO.getName()), FlexFontSize.LG);
        final Text calcBlock = Text
            .builder()
            .text(String.format("[%s円 × %s個]", itemDTO.getPrice(), quantity))
            .color("#555555")
            .wrap(true)
            .size(FlexFontSize.SM)
            .build();
        final Box itemBox = Box.builder().layout(FlexLayout.HORIZONTAL).contents(Arrays.asList(itemBlock, calcBlock)).build();

        // 合計金額
        final Text totalFeeBlock = FlexComponentUtil.createText(
            String.format("合計: %s円", itemDTO.getPrice() * quantity),
            FlexFontSize.LG
        );

        // 受け取り日時
        final Text deliveryDateBlock = FlexComponentUtil.createText(String.format("受取日時： %s", deliveryDate), FlexFontSize.LG);

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
            .action(new PostbackAction("注文を確定する", String.format("type=ordered&item={}", itemDTO.getId()), null))
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).content(addToCartEnableButton).build();
    }
}
