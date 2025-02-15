package com.example.dto;

import java.util.List;

public class WhispirMessageRequest {

    private String to;
    private String messageTemplateId;
    private MessageAttributes messageattributes;

    public static class MessageAttributes {
        private List<Attribute> attribute;

        public List<Attribute> getAttribute() {
            return attribute;
        }

        public void setAttribute(List<Attribute> attribute) {
            this.attribute = attribute;
        }
    }

    public static class Attribute {
        private String name;
        private String value;

        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageTemplateId() {
        return messageTemplateId;
    }

    public void setMessageTemplateId(String messageTemplateId) {
        this.messageTemplateId = messageTemplateId;
    }

    public MessageAttributes getMessageattributes() {
        return messageattributes;
    }

    public void setMessageattributes(MessageAttributes messageattributes) {
        this.messageattributes = messageattributes;
    }
}