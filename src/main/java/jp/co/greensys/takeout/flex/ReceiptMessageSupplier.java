package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.util.Arrays;
import java.util.function.Supplier;
import jp.co.greensys.takeout.util.FlexComponentUtil;

public class ReceiptMessageSupplier implements Supplier<FlexMessage> {
    private final Long orderId;

    public ReceiptMessageSupplier(Long orderId) {
        this.orderId = orderId;
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
        // 商品情報
        final Text itemBlock = FlexComponentUtil.createText("お買い上げありがとうございます。", null, FlexFontSize.Md);
        final Box itemBox = Box.builder().layout(FlexLayout.VERTICAL).contents(Arrays.asList(itemBlock)).build();

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(Arrays.asList(itemBox)).build();
    }

    private Box createFooterBlock() {
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction("準備状況を確認する", String.format("type=readiness&orderId=%s", orderId), null))
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).content(addToCartEnableButton).build();
    }
}
