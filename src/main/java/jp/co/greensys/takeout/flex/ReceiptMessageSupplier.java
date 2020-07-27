package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.util.Arrays;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.util.FlexComponentUtil;

public class ReceiptMessageSupplier implements Supplier<FlexMessage> {
    private final Long id;

    public ReceiptMessageSupplier(OrderedDTO orderedDTO) {
        this.id = orderedDTO.getId();
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("Receipt", bubble1);
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
        final Text orderBlock = FlexComponentUtil.createTextDecoration(Long.toString(id), null, FlexFontSize.XXXXXL);

        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(messageBlock, orderBlock))
            .build();
    }

    private Box createFooterBlock() {
        final Separator separator = Separator.builder().margin(FlexMarginSize.SM).color("#c0c0c0").build();
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction("準備状況を確認する", String.format("type=readiness&order=%s", id), null))
            .build();
        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(separator, addToCartEnableButton))
            .build();
    }
}
