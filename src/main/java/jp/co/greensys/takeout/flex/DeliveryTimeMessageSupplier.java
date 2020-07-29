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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import jp.co.greensys.takeout.util.DateTimeUtil;
import jp.co.greensys.takeout.util.FlexComponentUtil;
import jp.co.greensys.takeout.util.QueryStringParser;

public class DeliveryTimeMessageSupplier implements Supplier<FlexMessage> {
    private final String carts;
    private final String deliveryDate;

    public DeliveryTimeMessageSupplier(QueryStringParser parser) {
        this.carts = parser.getUrlQuery("cart");
        this.deliveryDate = parser.getUrlQuery("delivery-date");
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("受取時間選択", bubble1);
    }

    private Bubble createBubble() {
        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        return Bubble.builder().body(bodyBlock).footer(footerBlock).build();
    }

    private Box createBodyBlock() {
        final Text titleBlock = FlexComponentUtil.createText("何時に受け取る？", null, Text.TextWeight.BOLD, FlexFontSize.XL);
        return Box.builder().layout(FlexLayout.VERTICAL).margin(FlexMarginSize.XXL).content(titleBlock).build();
    }

    private Box createFooterBlock() {
        List list = new ArrayList();
        for (String date : DateTimeUtil.getDeliveryTime()) {
            final Button addToCartEnableButton = Button
                .builder()
                .style(Button.ButtonStyle.PRIMARY)
                .action(new PostbackAction(date, String.format("type=order%s&delivery-time=%s%s", deliveryDate, date, carts), null))
                .build();
            list.add(addToCartEnableButton);
        }

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(list).build();
    }
}
