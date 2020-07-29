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
import java.util.Arrays;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.util.FlexComponentUtil;

public class ReceiptCancelMessageSupplier implements Supplier<FlexMessage> {
    private final Long id;

    public ReceiptCancelMessageSupplier(OrderedDTO orderedDTO) {
        this.id = orderedDTO.getId();
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("ReceiptCancel", bubble1);
    }

    private Bubble createBubble() {
        return Bubble.builder().body(createBodyBlock()).footer(createFooterBlock()).build();
    }

    private Box createBodyBlock() {
        final Text messageBlock = FlexComponentUtil.createText(
            "申し訳ありませんが、仕入れ状況によりご提供できません。\n他のメニューをご注文ください。",
            null,
            FlexFontSize.Md
        );
        final Image imageBlock = FlexComponentUtil.createImageBlock(
            "https://1.bp.blogspot.com/-BB1YlBhjCvQ/WUdZI7dUY5I/AAAAAAABFDE/QXMI5AsMw8oxihIkMS5S4tlg6d-6cHGFACLcBGAs/s450/ojigi_tenin_woman.png"
        );

        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(messageBlock, imageBlock))
            .build();
    }

    private Box createFooterBlock() {
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction("準備状況を確認する", String.format("type=readiness&order=%s", id), null))
            .build();
        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(FlexComponentUtil.createSeparator(), addToCartEnableButton))
            .build();
    }
}
