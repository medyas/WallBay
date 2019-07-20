package ml.medyas.wallbay.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoFilter;
import ml.medyas.wallbay.R;
import ml.medyas.wallbay.models.GetStartedEntity;

public class Utils {

    public static final String PIXABAY_PROFILE_URL = "https://pixabay.com/en/users/";
    public static final String PEXELS_PROFILE_URL = "https://www.pexels.com/@";
    public static final String UNSPLASH_PROFILE_URL = "https://unsplash.com/";

    public static final String PIXABAY_URL = "https://pixabay.com/";
    public static final String PEXELS_URL = "https://www.pexels.com/";
    public static final String UNSPLASH_URL = "https://unsplash.com/";

    public static final String INTEREST_CATEGORIES = "interest_categories";


    public static final String FRAGMENT_POSITION = "FRAGMENT_POSITION";
    public static final String SEARCH_QUERY = "SEARCH_QUERY";

    public static final int REQUEST_SIZE = 15;
    public static final int PREFETCH_DISTANCE = 10;

    public static PhotoFilter[] photoFilters = {PhotoFilter.NONE, PhotoFilter.AUTO_FIX,  PhotoFilter.BRIGHTNESS,PhotoFilter.CONTRAST, PhotoFilter.DOCUMENTARY, PhotoFilter.DUE_TONE,
            PhotoFilter.FILL_LIGHT,PhotoFilter.FISH_EYE, PhotoFilter.GRAIN, PhotoFilter.GRAY_SCALE, PhotoFilter.LOMISH,PhotoFilter.NEGATIVE, PhotoFilter.POSTERIZE,PhotoFilter.SATURATE,
    PhotoFilter.SEPIA,PhotoFilter.SHARPEN, PhotoFilter.TEMPERATURE, PhotoFilter.TINT, PhotoFilter.VIGNETTE, PhotoFilter.CROSS_PROCESS,
    PhotoFilter.BLACK_WHITE, PhotoFilter.FLIP_HORIZONTAL, PhotoFilter.FLIP_VERTICAL, PhotoFilter.ROTATE};


    public enum webSite {
        PIXABAY(0), PEXELS(1), UNSPLASH(2), EMPTY(4), ERROR(5);

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

    public static final int[] categoriesListImages = {R.drawable.fashion, R.drawable.nature, R.drawable.backgrounds, R.drawable.science, R.drawable.education,
            R.drawable.people, R.drawable.feelings, R.drawable.religon, R.drawable.health, R.drawable.places, R.drawable.animals, R.drawable.industry,
            R.drawable.food, R.drawable.computer, R.drawable.sports, R.drawable.transportation, R.drawable.travel, R.drawable.buildings, R.drawable.business, R.drawable.music};
    private static final String[] categoriesListName = {"fashion", "nature", "backgrounds", "science", "education", "people", "feelings", "religion",
            "health", "places", "animals", "industry", "food", "computer", "sports", "transportation", "travel", "buildings", "business", "music"};

    public static List<GetStartedEntity> getCategoriesList() {
        List<GetStartedEntity> list = new ArrayList<>();
        for (int i = 0; i < categoriesListName.length; i++) {
            GetStartedEntity getStartedEntity = new GetStartedEntity(categoriesListName[i], categoriesListImages[i], false);
            list.add(getStartedEntity);
        }

        return list;
    }

    public static int calculateItemWidth(Context context, int width) {
        if (calculateNoOfColumns(context, width) > 1) {
            return width;
        } else {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            return (int) (dpWidth / 2);
        }
    }


    public static int calculateNoOfColumns(Context context, int width) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / width);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getCategoriesFromList(List<GetStartedEntity> getStartedEntities) {
        StringBuilder categories = new StringBuilder();
        for (int i = 0; i < getStartedEntities.size(); i++) {
            categories.append(getStartedEntities.get(i).getCategoryName());
            if (i + 1 < getStartedEntities.size()) {
                categories.append("|");
            }
        }

        return categories.toString();
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static int convertPixelsToDp(float px, Context context) {
        return (int) (px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return Math.round(px);
    }

    public enum NetworkState {
        LOADING, LOADED, FAILED, EMPTY
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static String getProviders(Utils.webSite provider) {
        switch (provider) {
            case PIXABAY:
                return "Pixabay";
            case UNSPLASH:
                return "Unsplash";
            case PEXELS:
                return "Pexels";
        }
        return "";
    }

    public static boolean getNetworkStatus(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public interface OnFragmentInteractions {
        void onAddFragment(Fragment fragment);
        void updateToolbarTitle(String title);
    }
}
