package ChatClient.Chat;

import Interfaces.IMessage;
import javafx.geometry.VPos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CustomListCell extends ListCell<IMessage> {
    private String username;
    private double width;
    public CustomListCell(String username, double width) {
        this.username = username;
        this.width = width;
    }

    @Override
    protected void updateItem(IMessage item, boolean empty) {
        super.updateItem(item, empty);

        Pane pane = null;
        if (!empty) {
            pane = new Pane();
            if (item.getReceiver()) {
                final Text leftText = new Text(username + ": " + item.getContent());
                leftText.setFont(Font.font("STYLE_BOLD",16));
                //leftText.setFont(_itemFont);
                leftText.setTextOrigin(VPos.TOP);
                leftText.relocate(10, 0);
                pane.getChildren().add(leftText);
            }
            else
            {
                // right-aligned text at position 8em
                final Text rightText = new Text(item.getContent());
                //rightText.setFont(_itemFont);
                rightText.setFont(Font.font(14));
                rightText.setTextOrigin(VPos.TOP);
                double text = rightText.getLayoutBounds().getWidth();
                rightText.relocate(width - text - 30, 0);
                pane.getChildren().add(rightText);
            }
        }
        setText("");
        setGraphic(pane);
    }
}
