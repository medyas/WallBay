package ml.medyas.wallbay.utils;

public class Utils {

    public static final String PIXABAY_PROFILE_URL = "https://pixabay.com/en/users/";
    public static final String PEXELS_PROFILE_URL = "https://www.pexels.com/@";
    public static final String UNSPLASH_PROFILE_URL = "https://unsplash.com/";

    public static final int REQUEST_SIZE = 30;

    public enum webSite {
        PIXABAY(0), PEXELS(1), UNSPLASH(3), EMPTY(4), ERROR(5);

        private int code;

        webSite() {
        }

        webSite(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
