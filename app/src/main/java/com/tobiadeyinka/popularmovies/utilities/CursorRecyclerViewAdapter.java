package com.tobiadeyinka.popularmovies.utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

import com.tobiadeyinka.popularmovies.database.ConfigValues;

public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    private Cursor cursor;
    private Context context;
    private int rowIdColumn;
    private boolean dataIsValid;
    private DataSetObserver dataSetObserver;

    public CursorRecyclerViewAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        dataIsValid = cursor != null;
        rowIdColumn = dataIsValid ? this.cursor.getColumnIndex("id") : -1;
        dataSetObserver = new NotifyingDataSetObserver();
        if (this.cursor != null) this.cursor.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public int getItemCount() {
        if (dataIsValid && cursor != null) return cursor.getCount();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (dataIsValid && cursor != null && cursor.moveToPosition(position))
            return cursor.getLong(rowIdColumn);
        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        if (!dataIsValid) throw new IllegalStateException("this should only be called when the cursor is valid");
        if (!cursor.moveToPosition(position)) throw new IllegalStateException("couldn't move cursor to position " + position);
        onBindViewHolder(viewHolder, cursor);
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) old.close();
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) return null;
        final Cursor oldCursor = cursor;

        if (oldCursor != null && dataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }

        cursor = newCursor;
        if (cursor != null) {
            if (dataSetObserver != null) cursor.registerDataSetObserver(dataSetObserver);
            rowIdColumn = newCursor.getColumnIndexOrThrow(ConfigValues.ID);
            dataIsValid = true;
            notifyDataSetChanged();
        } else {
            rowIdColumn = -1;
            dataIsValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            dataIsValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            dataIsValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }
}
