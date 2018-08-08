package github.hotstu.maotai.engine;

/**
 * @author hglf
 * @since 2018/8/6
 */
public enum MDSourceType {

    ASSETS(0),
    FOLDER_A(1),
    FOLDER_B(2);

    private static final String ASSET_PATH = "/android_asset/widget";
    private final int value;

    MDSourceType(int i) {
        this.value = i;
    }

    public String getSourcePath() {
        //TODO supprot other sourcefolder path;
        switch (value) {
            case 0:
                return ASSET_PATH;
            case 1:
                return ASSET_PATH;
            case 2:
                return ASSET_PATH;
            default:
                return ASSET_PATH;
        }
    }

    public static MDSourceType fromValue(int value) {
        switch (value) {
            case 0:
                return MDSourceType.ASSETS;
            case 1:
                return MDSourceType.FOLDER_A;
            case 2:
                return MDSourceType.FOLDER_B;
            default:
                return MDSourceType.ASSETS;
        }
    }
}
