package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.util.Arrays;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.util.DateTimeUtil;
import jp.co.greensys.takeout.util.FlexComponentUtil;

public class ReceiptAcceptMessageSupplier implements Supplier<FlexMessage> {
    private final OrderedDTO orderedDTO;

    public ReceiptAcceptMessageSupplier(OrderedDTO orderedDTO) {
        this.orderedDTO = orderedDTO;
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("ReceiptAccept", bubble1);
    }

    private Bubble createBubble() {
        return Bubble.builder().hero(createHeroBox()).body(createBodyBlock()).footer(createFooterBlock()).build();
    }

    private Box createHeroBox() {
        final Text titleBlock = FlexComponentUtil.createText("受領書", "#1DB446", FlexFontSize.XL);
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(Arrays.asList(titleBlock)).build();
    }

    private Box createBodyBlock() {
        final Text messageBlock = FlexComponentUtil.createText(
            "お買い上げありがとうございます。\n店舗窓口でスタッフにこのメッセージをお見せください",
            null,
            FlexFontSize.Md
        );
        final Text orderBlock = FlexComponentUtil.createTextDecoration(Long.toString(orderedDTO.getId()), null, FlexFontSize.XXXXXL);

        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(messageBlock, orderBlock))
            .build();
    }

    private Box createFooterBlock() {
        final Separator separator = Separator.builder().margin(FlexMarginSize.SM).color("#c0c0c0").build();
        // 商品情報
        final Text itemBlock = FlexComponentUtil.createText(String.format("商品: %s", orderedDTO.getItemName()), null, FlexFontSize.LG);
        final Text calcBlock = FlexComponentUtil.createText(
            String.format("[%s円 × %s個]", orderedDTO.getUnitPrice(), orderedDTO.getQuantity()),
            "#555555",
            FlexFontSize.SM
        );
        final Box itemBox = Box.builder().layout(FlexLayout.VERTICAL).contents(Arrays.asList(itemBlock, calcBlock)).build();

        // 合計金額
        final Text totalFeeBlock = FlexComponentUtil.createText(
            String.format("合計: %s円", orderedDTO.getTotalFee()),
            null,
            FlexFontSize.LG
        );

        // 受け取り日時
        final Text deliveryDateBlock = FlexComponentUtil.createText(
            String.format("受取日時： %s", DateTimeUtil.toString(orderedDTO.getDeliveryDate().toEpochMilli())),
            null,
            FlexFontSize.LG
        );
        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(separator, itemBox, totalFeeBlock, deliveryDateBlock))
            .build();
    }
}
