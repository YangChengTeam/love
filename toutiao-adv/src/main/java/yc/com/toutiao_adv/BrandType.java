package yc.com.toutiao_adv;

public enum BrandType {
    HUAWEI("huawei"), HONOR("honor"), XIAOMI("xiaomi"),
    VIVO("vivo"), OPPO("oppo"), GIONEE("gionee"), MEIZU("meizu");


    private String name;

    BrandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
