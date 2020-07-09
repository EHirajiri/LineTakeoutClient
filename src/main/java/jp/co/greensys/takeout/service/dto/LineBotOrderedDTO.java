package jp.co.greensys.takeout.service.dto;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class LineBotOrderedDTO implements Serializable {
    private String app;
    private Record record;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.DEFAULT_STYLE);
    }
}

class Record {
    private Value orderId;
    private Value userId;
    private Value itemId;
    private Value itemName;
    private IntValue unitPrice;
    private Value quantity;

    public Value getOrderId() {
        return orderId;
    }

    public void setOrderId(Value orderId) {
        this.orderId = orderId;
    }

    public Value getUserId() {
        return userId;
    }

    public void setUserId(Value userId) {
        this.userId = userId;
    }

    public Value getItemId() {
        return itemId;
    }

    public void setItemId(Value itemId) {
        this.itemId = itemId;
    }

    public Value getItemName() {
        return itemName;
    }

    public void setItemName(Value itemName) {
        this.itemName = itemName;
    }

    public IntValue getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(IntValue unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Value getQuantity() {
        return quantity;
    }

    public void setQuantity(Value quantity) {
        this.quantity = quantity;
    }
}

class Value {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class IntValue {
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
