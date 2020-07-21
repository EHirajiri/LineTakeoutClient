package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Supplier;

public class OrderMessageSupplier implements Supplier<FlexMessage> {
    private final String itemId;
    private final String quantity;
    private final TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd(E) HH:mm");
    private final List<String> deliveryDate = Arrays.asList("12:00", "12:30", "13:00", "13:00");

    public OrderMessageSupplier(String itemId, String quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("Order", bubble1);
    }

    private Bubble createBubble() {
        final Image heroBlock = createHeroBlock(
            "https://2.bp.blogspot.com/-IcQD1H8lx5c/VnKNfpw47BI/AAAAAAAA2EY/iVffCXI9_ug/s400/food_zei3_takeout.png"
        );
        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        return Bubble.builder().hero(heroBlock).body(bodyBlock).footer(footerBlock).build();
    }

    private Image createHeroBlock(String imageURL) {
        return Image
            .builder()
            .size(Image.ImageSize.FULL_WIDTH)
            .aspectRatio(Image.ImageAspectRatio.R1TO1)
            .aspectMode(Image.ImageAspectMode.Cover)
            .url(URI.create(imageURL))
            .build();
    }

    private Box createBodyBlock() {
        final Text titleBlock = Text.builder().text("レジ").wrap(true).weight(Text.TextWeight.BOLD).size(FlexFontSize.XL).build();

        FlexComponent[] flexComponents = { titleBlock };
        List<FlexComponent> listComponent = new ArrayList<>(Arrays.asList(flexComponents));

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(listComponent).build();
    }

    private Box createFooterBlock() {
        List list = new ArrayList();
        Calendar calendar = Calendar.getInstance(timeZone);
        for (String date : deliveryDate) {
            String[] split = date.split(":");
            calendar.set(Calendar.HOUR, Integer.parseInt(split[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(split[1]));
            calendar.set(Calendar.SECOND, 0);

            final Button addToCartEnableButton = Button
                .builder()
                .style(Button.ButtonStyle.PRIMARY)
                .action(
                    new PostbackAction(
                        dateFormat.format(calendar.getTime()),
                        String.format("type=order&item=%s&quantity" + "=%s," + "deliveryDate=%s", itemId, quantity, calendar.getTime()),
                        null
                    )
                )
                .build();
            list.add(addToCartEnableButton);
        }

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(list).build();
    }
}
