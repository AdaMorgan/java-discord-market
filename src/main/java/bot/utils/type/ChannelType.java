package bot.utils.type;

public enum ChannelType {
    AUCTION("auction"),
    MARKET("market"),
    TRADE("trade");

    private final String name;

    ChannelType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
