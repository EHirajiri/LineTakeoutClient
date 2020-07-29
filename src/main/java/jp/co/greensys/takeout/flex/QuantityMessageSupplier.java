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
import jp.co.greensys.takeout.flex.dto.CartDTO;
import jp.co.greensys.takeout.util.FlexComponentUtil;
import jp.co.greensys.takeout.util.JsonUtil;
import jp.co.greensys.takeout.util.QueryStringParser;

public class QuantityMessageSupplier implements Supplier<FlexMessage> {
    private final Long itemId;
    private final String carts;

    public QuantityMessageSupplier(QueryStringParser parser) {
        this.itemId = Long.parseLong(parser.getParameterValue("item"));
        this.carts = parser.getUrlQuery("cart");
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("個数選択", bubble1);
    }

    private Bubble createBubble() {
        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock(itemId);
        return Bubble.builder().body(bodyBlock).footer(footerBlock).build();
    }

    private Box createBodyBlock() {
        final Text titleBlock = FlexComponentUtil.createText("いくつ注文する？", null, Text.TextWeight.BOLD, FlexFontSize.XL);
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.XL).content(titleBlock).build();
    }

    private Box createFooterBlock(Long itemId) {
        List list = new ArrayList();
        for (int i = 1; i <= 5; i++) {
            final String postData = String.format("type=cart&cart=%s%s", JsonUtil.convert(new CartDTO(itemId, i)), carts);
            final Button selectQuantityButton = FlexComponentUtil.createButton(
                Button.ButtonStyle.PRIMARY,
                new PostbackAction(i + "個", postData, null)
            );
            list.add(selectQuantityButton);
        }
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(list).build();
    }
}
