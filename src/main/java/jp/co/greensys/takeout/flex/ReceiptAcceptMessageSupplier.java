package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
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
import jp.co.greensys.takeout.service.dto.OrderItemDTO;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.util.DateTimeUtil;
import jp.co.greensys.takeout.util.FlexComponentUtil;
import org.springframework.util.CollectionUtils;

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
        List<FlexComponent> listComponent = new ArrayList<>();

        if (!CollectionUtils.isEmpty(orderedDTO.getOrderItems())) {
            listComponent.add(FlexComponentUtil.createSeparator());
            // 商品情報
            for (OrderItemDTO itemDTO : orderedDTO.getOrderItems()) {
                final Text itemBlock = FlexComponentUtil.createText(String.format("商品: %s", itemDTO.getName()), null, FlexFontSize.LG);
                final Text totalFeeBlock = FlexComponentUtil.createText(
                    String.format("金額: %s円 × %s個 = %s", itemDTO.getPrice(), itemDTO.getQuantity(), itemDTO.getTotalFee()),
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
            }
            final Text totalBlock = FlexComponentUtil.createText(
                String.format("合計金額: %s円", orderedDTO.getTotalFee()),
                null,
                FlexFontSize.LG
            );
            listComponent.add(totalBlock);
            final Text deliveryDateBlock = FlexComponentUtil.createText(
                String.format("受取日時: %s", orderedDTO.getDeliveryDate()),
                null,
                FlexFontSize.LG
            );
            listComponent.add(deliveryDateBlock);
        }

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(listComponent).build();
    }
}
