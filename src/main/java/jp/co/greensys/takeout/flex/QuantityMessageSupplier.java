package jp.co.greensys.takeout.flex;

import static java.util.Arrays.asList;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.util.function.Supplier;

public class QuantityMessageSupplier implements Supplier<FlexMessage> {

    @Override
    public FlexMessage get() {
        Bubble bubble = Bubble.builder().body(createFooterBlock(1, "001")).build();
        Bubble bubble2 = Bubble.builder().body(createFooterBlock(2, "002")).build();
        final Carousel carousel = Carousel.builder().contents(asList(bubble, bubble2)).build();
        return new FlexMessage("Quantity", carousel);
    }

    private Box createFooterBlock(int index, String itemId) {
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction(index + "個", "type=order&item=" + itemId + "&quantity=" + index, index + "個"))
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(asList(addToCartEnableButton)).build();
    }
}
