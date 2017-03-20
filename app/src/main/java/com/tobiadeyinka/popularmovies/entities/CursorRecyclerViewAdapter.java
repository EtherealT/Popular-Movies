package com.tobiadeyinka.popularmovies.entities;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Context context;
    private Cursor cursor;
    private boolean dataIsValid;
    private int rowIdColumn;
    private DataSetObserver dataSetObserver;

    public CursorRecyclerViewAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        dataIsValid = cursor != null;
        rowIdColumn = dataIsValid ? this.cursor.getColumnIndex("_id") : -1;
        dataSetObserver = new NotifyingDataSetObserver();
        if (dataIsValid) this.cursor.registerDataSetObserver(dataSetObserver);
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public int getItemCount() {
        return (dataIsValid && cursor != null) ? cursor.getCount() : 0;
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
        if (!dataIsValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
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
            if (dataSetObserver != null) {
                cursor.registerDataSetObserver(dataSetObserver);
            }
            rowIdColumn = newCursor.getColumnIndexOrThrow("_id");
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
