package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.ItemDTO;

public class OrderMessageSupplier implements Supplier<FlexMessage> {
    private final ItemDTO itemDTO;
    private final int quantity;
    private final TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd(E) HH:mm");

    public OrderMessageSupplier(ItemDTO itemDTO, String quantity) {
        this.itemDTO = itemDTO;
        this.quantity = Integer.parseInt(quantity);
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("Order", bubble1);
    }

    private Bubble createBubble() {
        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        return Bubble.builder().body(bodyBlock).footer(footerBlock).build();
    }

    private Box createBodyBlock() {
        final Text titleBlock = Text.builder().text("レジ").wrap(true).weight(Text.TextWeight.BOLD).size(FlexFontSize.XL).build();
        final Image imageBlock = createImageBlock(
            "https://2.bp.blogspot.com/-IcQD1H8lx5c/VnKNfpw47BI/AAAAAAAA2EY/iVffCXI9_ug/s400/food_zei3_takeout.png"
        );
        final Text itemBlock = Text.builder().text(itemDTO.getName()).wrap(true).weight(Text.TextWeight.BOLD).size(FlexFontSize.XL).build();
        final Text totalFeeBlock = Text
            .builder()
            .text(String.format("%s円 × %s個", itemDTO.getPrice(), quantity))
            .wrap(true)
            .size(FlexFontSize.XS)
            .text(String.format("%s円", itemDTO.getPrice() * quantity))
            .weight(Text.TextWeight.BOLD)
            .wrap(true)
            .size(FlexFontSize.XL)
            .build();

        FlexComponent[] flexComponents = { titleBlock, imageBlock, itemBlock, totalFeeBlock };
        List<FlexComponent> listComponent = new ArrayList<>(Arrays.asList(flexComponents));

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(listComponent).build();
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
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction("注文を確定する", "type=ordered&item=" + itemDTO.getId(), null))
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).content(addToCartEnableButton).build();
    }
}
