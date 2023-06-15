package local.dsjsantos.pdf_qrcode_gson_tests.model;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("unused")
public class ItemsVO implements Serializable {

    private static final long serialVersionUID = 6306654391279211432L;

    private Integer itemId;
    private String description;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemsVO itemsVO = (ItemsVO) o;
        return Objects.equals(itemId, itemsVO.itemId) && Objects.equals(description, itemsVO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, description);
    }
}
