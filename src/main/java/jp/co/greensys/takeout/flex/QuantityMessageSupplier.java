package jp.co.greensys.takeout.flex;

import static java.util.Arrays.asList;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.util.function.Supplier;

public class QuantityMessageSupplier implements Supplier<FlexMessage> {

    @Override
    public FlexMessage get() {
        Bubble bubble = Bubble.builder().header(createHeroBlock()).body(createFooterBlock("001")).build();
        return new FlexMessage("Quantity", bubble);
    }

    private Box createHeroBlock() {
        final Text titleBlock = Text.builder().text("title").wrap(true).weight(Text.TextWeight.BOLD).size(FlexFontSize.XL).build();
        return Box.builder().contents(titleBlock).build();
    }

    private Box createFooterBlock(String itemId) {
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction("注文を確定する", "type=order&item=" + itemId, "注文を確定する"))
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(asList(addToCartEnableButton)).build();
    }
}
