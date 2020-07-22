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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import jp.co.greensys.takeout.util.DateTimeUtil;
import jp.co.greensys.takeout.util.QueryStringParser;

public class DeliveryMessageSupplier implements Supplier<FlexMessage> {
    private final String itemId;
    private final String quantity;
    private final String orderId;
    private final List<String> deliveryDate = Arrays.asList("12:00", "12:30", "13:00", "13:00");

    public DeliveryMessageSupplier(QueryStringParser parser) {
        this.itemId = parser.getParameterValue("item");
        this.quantity = parser.getParameterValue("quantity");
        this.orderId = parser.getParameterValue("orderId");
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("Delivery", bubble1);
    }

    private Bubble createBubble() {
        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        return Bubble.builder().body(bodyBlock).footer(footerBlock).build();
    }

    private Box createBodyBlock() {
        final Text titleBlock = Text.builder().text("いつ受け取る？").wrap(true).weight(Text.TextWeight.BOLD).size(FlexFontSize.XL).build();

        FlexComponent[] flexComponents = { titleBlock };
        List<FlexComponent> listComponent = new ArrayList<>(Arrays.asList(flexComponents));

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(listComponent).build();
    }

    private Box createFooterBlock() {
        List list = new ArrayList();
        for (String date : deliveryDate) {
            String[] split = date.split(":");
            LocalDateTime deliveryDate = DateTimeUtil.getDateOfToday(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            final Button addToCartEnableButton = Button
                .builder()
                .style(Button.ButtonStyle.PRIMARY)
                .action(
                    new PostbackAction(
                        DateTimeUtil.parseString(deliveryDate),
                        String.format(
                            "type=order&item=%s&quantity=%s&deliveryDate=%s&orderId=%s",
                            itemId,
                            quantity,
                            deliveryDate.toString(),
                            orderId
                        ),
                        null
                    )
                )
                .build();
            list.add(addToCartEnableButton);
        }

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(list).build();
    }
}
