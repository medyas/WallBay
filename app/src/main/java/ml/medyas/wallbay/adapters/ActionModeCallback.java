package ml.medyas.wallbay.adapters;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ml.medyas.wallbay.R;

public class ActionModeCallback implements ActionMode.Callback {
    private onActionModeDestroyInterface mListener;
    private Context context;

    public ActionModeCallback(Context context, onActionModeDestroyInterface listener) {
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.default_action_mode, menu);
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
                Toast.makeText(context, "Menu favorite", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.download:
                Toast.makeText(context, "Menu Download", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mListener.onDestroyMode();
    }

    public interface onActionModeDestroyInterface {
        void onDestroyMode();
    }
}
