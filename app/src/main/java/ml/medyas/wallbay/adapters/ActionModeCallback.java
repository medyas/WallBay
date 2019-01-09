package ml.medyas.wallbay.adapters;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import ml.medyas.wallbay.R;

public class ActionModeCallback implements ActionMode.Callback {
    private onActionModeInterface mListener;
    private boolean defaultMenu = true;

    public ActionModeCallback(onActionModeInterface listener, boolean defaultMenu) {
        this.mListener = listener;
        this.defaultMenu = defaultMenu;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        if (defaultMenu)
            actionMode.getMenuInflater().inflate(R.menu.default_action_mode, menu);
        else
            actionMode.getMenuInflater().inflate(R.menu.favorite_action_mode, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.add_to_fav:
                mListener.onMenuFavClicked();
                return true;

            case R.id.download:
                mListener.onMenuDownClicked();
                return true;

            case R.id.select_all:
                mListener.onSelectAll();
                return true;

            case R.id.remove:
                mListener.onMenuRemoveClicked();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mListener.onDestroyMode();
    }

    public interface onActionModeInterface {
        void onDestroyMode();

        void onMenuFavClicked();

        void onMenuDownClicked();

        void onMenuRemoveClicked();

        void onSelectAll();
    }
}
