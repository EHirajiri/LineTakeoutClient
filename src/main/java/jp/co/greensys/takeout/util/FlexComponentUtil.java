package jp.co.greensys.takeout.util;

import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;

public class FlexComponentUtil {

    public static Text createText(String text, String color, FlexFontSize fontSize) {
        final Text textBlock = Text.builder().text(text).wrap(true).color(color).weight(Text.TextWeight.BOLD).size(fontSize).build();
        return textBlock;
    }
}
