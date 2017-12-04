package ChatClient.Chat;

import Interfaces.IMessage;
import javafx.geometry.VPos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CustomListCell extends ListCell<IMessage> {
    private Font _itemFont = Font.font("arial");

    @Override
    protected void updateItem(IMessage item, boolean empty) {
        super.updateItem(item, empty);

        Pane pane = null;
        if (!empty) {
            pane = new Pane();
            if (item.getReceiver()) {
                final Text leftText = new Text(item.getContent());
                leftText.setFont(_itemFont);
                leftText.setTextOrigin(VPos.TOP);
                leftText.relocate(0, 0);
                pane.getChildren().add(leftText);
            }
            else
            {
                // right-aligned text at position 8em
                final Text rightText = new Text(item.getContent());
                rightText.setFont(_itemFont);
                rightText.setTextOrigin(VPos.TOP);
                final double width = rightText.getLayoutBounds().getWidth();
                rightText.relocate(8 *  width, 0);
                pane.getChildren().add(rightText);
            }
        }
        setText("");
        setGraphic(pane);
    }
}
