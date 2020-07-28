package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import jp.co.greensys.takeout.service.dto.InformationDTO;
import jp.co.greensys.takeout.util.FlexComponentUtil;

public class BusinessHourMessageSupplier implements Supplier<FlexMessage> {
    private final List<InformationDTO> informationList;

    public BusinessHourMessageSupplier(List<InformationDTO> informationList) {
        this.informationList = informationList;
    }

    @Override
    public FlexMessage get() {
        final Bubble bubble1 = createBubble();
        return new FlexMessage("BusinessHour", bubble1);
    }

    private Bubble createBubble() {
        return Bubble.builder().hero(createHeroBox()).body(createBodyBlock()).footer(createFooterBlock()).build();
    }

    private Box createHeroBox() {
        final Text titleBlock = FlexComponentUtil.createText("営業時間", "#aaaaaa", FlexFontSize.LG);
        final Image imageBlock = FlexComponentUtil.createImageBlock(
            "https://3.bp.blogspot.com/-jHdA_6VWQFw/WYAxqtZz54I/AAAAAAABFuk/rDsnpKBPpsQ1gMDDrJYRjh5IVdUBkd30QCLcBGAs/s400/building_fastfood.png"
        );

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.LG).contents(Arrays.asList(titleBlock, imageBlock)).build();
    }

    private Box createBodyBlock() {
        List businessHour = new ArrayList<>();
        for (InformationDTO informationDTO : informationList) {
            String[] split = informationDTO.getValue().split(",");
            final Text label = FlexComponentUtil.createText(split[0], null, FlexFontSize.SM);
            final Text date = FlexComponentUtil.createText(split[1], null, FlexFontSize.SM);
            businessHour.add(
                Box.builder().layout(FlexLayout.BASELINE).spacing(FlexMarginSize.SM).contents(Arrays.asList(label, date)).build()
            );
        }

        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(businessHour).build();
    }

    private Box createFooterBlock() {
        final Button addToCartEnableButton = Button
            .builder()
            .style(Button.ButtonStyle.LINK)
            .action(new URIAction("WEBSITE", URI.create("https://linecorp.com"), null))
            .build();
        return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.LG).content(addToCartEnableButton).build();
    }
}
