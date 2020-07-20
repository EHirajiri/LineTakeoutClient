package jp.co.greensys.takeout.flex;

import static java.util.Arrays.asList;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.Carousel;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class MenuFlexMessageSupplier implements Supplier<FlexMessage> {

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble(
            "チーズバーガー",
            "500",
            "https://2.bp.blogspot.com/-V6VfiVDMitg/WC5efOCqmFI/AAAAAAAA_5A/P97lsAxzB5kUDdJYLIz_DvdferdNRl6aACLcB/s400/food_hamburger_cheese.png"
        );
        final Bubble bubble2 = createBubble(
            "テリヤキバーガー",
            "600",
            "https://1.bp.blogspot.com/-ccmRa-W5FdQ/WGnPWhQSnzI/AAAAAAABA4w/krKcel6z1hobC87K1Vj9bG_Me_AfBo15QCLcB/s400/hamburger_teriyaki_burger.png"
        );
        final Carousel carousel = Carousel.builder().contents(asList(bubble1, bubble2)).build();
        return new FlexMessage("Menu", carousel);
    }

    private Bubble createBubble(String title, String price, String imageURL) {
        final Image heroBlock = createHeroBlock(imageURL);
        final Box bodyBlock = createBodyBlock(title, price);
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

    private Box createBodyBlock(String title, String price) {
        final Text titleBlock = Text.builder().text(title).wrap(true).weight(Text.TextWeight.BOLD).size(FlexFontSize.XL).build();
        final Box priceBlock = Box
            .builder()
            .layout(FlexLayout.BASELINE)
            .contents(
                asList(Text.builder().text("￥" + price).wrap(true).weight(Text.TextWeight.BOLD).size(FlexFontSize.XL).flex(0).build())
            )
            .build();

        FlexComponent[] flexComponents = { titleBlock, priceBlock };
        List<FlexComponent> listComponent = new ArrayList<>(Arrays.asList(flexComponents));

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(listComponent).build();
    }

    private Box createFooterBlock() {
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.PRIMARY)
            .action(new PostbackAction("注文する", "type=select&item=001", "チーズバーガー"))
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(asList(addToCartEnableButton)).build();
    }
}
