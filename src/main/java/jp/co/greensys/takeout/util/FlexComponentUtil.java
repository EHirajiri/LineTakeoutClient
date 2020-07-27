package jp.co.greensys.takeout.util;

import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;

public class FlexComponentUtil {

    public static Text createText(String text, String color, FlexFontSize fontSize) {
        final Text textBlock = Text.builder().text(text).wrap(true).color(color).size(fontSize).build();
        return textBlock;
    }

    public static Text createText(String text, String color, Text.TextWeight textWeight, FlexFontSize fontSize) {
        final Text textBlock = Text.builder().text(text).wrap(true).color(color).weight(textWeight).size(fontSize).build();
        return textBlock;
    }

    public static Text createTextDecoration(String text, String color, FlexFontSize fontSize) {
        final Text textBlock = Text
            .builder()
            .text(text)
            .wrap(true)
            .color(color)
            .weight(Text.TextWeight.BOLD)
            .size(fontSize)
            .decoration(Text.TextDecoration.UNDERLINE)
            .build();
        return textBlock;
    }
}
