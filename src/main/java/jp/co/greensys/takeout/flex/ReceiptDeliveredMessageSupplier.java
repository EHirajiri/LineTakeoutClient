package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.net.URI;
import java.util.Arrays;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.OrderedDTO;
import jp.co.greensys.takeout.util.FlexComponentUtil;

public class ReceiptDeliveredMessageSupplier implements Supplier<FlexMessage> {
    private final Long id;

    public ReceiptDeliveredMessageSupplier(OrderedDTO orderedDTO) {
        this.id = orderedDTO.getId();
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("ReceiptDelivered", bubble1);
    }

    private Bubble createBubble() {
        return Bubble.builder().body(createBodyBlock()).build();
    }

    private Box createBodyBlock() {
        final Text messageBlock = FlexComponentUtil.createText(
            "ご利用ありがとうございました。\nまたのご利用をお待ちしております。",
            null,
            FlexFontSize.Md
        );
        final Image imageBlock = createImageBlock(
            "https://3.bp.blogspot.com/-nrwPWwcAaOA/VRE4WqEKBSI/AAAAAAAAsV0/8D61LcHzjAk/s400/aisatsu_arigatou.png"
        );

        return Box
            .builder()
            .layout(FlexLayout.VERTICAL)
            .spacing(FlexMarginSize.SM)
            .contents(Arrays.asList(messageBlock, imageBlock))
            .build();
    }

    private Image createImageBlock(String imageURL) {
        return Image
            .builder()
            .size(Image.ImageSize.FULL_WIDTH)
            .aspectRatio(Image.ImageAspectRatio.R1TO1)
            .aspectMode(Image.ImageAspectMode.Cover)
            .url(URI.create(imageURL))
            .build();
    }
}
