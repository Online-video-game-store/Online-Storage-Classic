package mr.demonid.notification.service.domain;

public enum NotifyType {
    INFO("Информация"), WARNING("Предупреждение"), ERROR("Ошибка");

    private String type;

    NotifyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
