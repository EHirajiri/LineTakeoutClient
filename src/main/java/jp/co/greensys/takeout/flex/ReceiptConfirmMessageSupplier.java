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

public class ReceiptConfirmMessageSupplier implements Supplier<FlexMessage> {
    private final Long id;

    public ReceiptConfirmMessageSupplier(OrderedDTO orderedDTO) {
        this.id = orderedDTO.getId();
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("ReceiptConfirm", bubble1);
    }

    private Bubble createBubble() {
        return Bubble.builder().body(createBodyBlock()).footer(createFooterBlock()).build();
    }

    private Box createBodyBlock() {
        final Text messageBlock = FlexComponentUtil.createText(
            "ご注文ありがとうございます。\n店舗スタッフが確認の上受領書を発行いたしますので、少々お待ちください。",
            null,
            FlexFontSize.Md
        );
        final Image imageBlock = createImageBlock(
            "https://1.bp.blogspot.com/-BB1YlBhjCvQ/WUdZI7dUY5I/AAAAAAABFDE/QXMI5AsMw8oxihIkMS5S4tlg6d-6cHGFACLcBGAs/s450/ojigi_tenin_woman.png"
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
