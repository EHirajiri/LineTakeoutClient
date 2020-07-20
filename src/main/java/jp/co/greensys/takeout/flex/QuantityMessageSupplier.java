package jp.co.greensys.takeout.flex;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.flex.component.Button;
import java.util.function.Supplier;

public class QuantityMessageSupplier implements Supplier<Button[]> {

    @Override
    public Button[] get() {
        Button button = Button.builder().action(new PostbackAction("1個", "type=order&item=001&quantity=1", "1個")).build();
        Button button2 = Button.builder().action(new PostbackAction("2個", "type=order&item=001&quantity=2", "2個")).build();
        return new Button[] { button, button2 };
    }
}
