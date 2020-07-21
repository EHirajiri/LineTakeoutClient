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
import jp.co.greensys.takeout.util.QueryStringParser;

public class QuantityMessageSupplier implements Supplier<FlexMessage> {
    private final String itemId;
    private final String orderId;

    public QuantityMessageSupplier(QueryStringParser parser) {
        this.itemId = parser.getParameterValue("item");
        this.orderId = parser.getParameterValue("orderId");
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("Quantity", bubble1);
    }

    private Bubble createBubble() {
        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock(itemId);
        return Bubble.builder().body(bodyBlock).footer(footerBlock).build();
    }

    private Box createBodyBlock() {
        final Text titleBlock = Text
            .builder()
            .text("いくつ注文する？")
            .wrap(true)
            .weight(Text.TextWeight.BOLD)
            .size(FlexFontSize.XL)
            .build();

        FlexComponent[] flexComponents = { titleBlock };
        List<FlexComponent> listComponent = new ArrayList<>(Arrays.asList(flexComponents));

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(listComponent).build();
    }

    private Box createFooterBlock(String itemId) {
        List list = new ArrayList();
        for (int i = 1; i < 6; i++) {
            final Button addToCartEnableButton = Button
                .builder()
                .style(Button.ButtonStyle.PRIMARY)
                .action(
                    new PostbackAction(i + "個", String.format("type=delivery&item=%s&quantity=%s&orderId=%s", itemId, i, orderId), null)
                )
                .build();
            list.add(addToCartEnableButton);
        }

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(list).build();
    }
}
