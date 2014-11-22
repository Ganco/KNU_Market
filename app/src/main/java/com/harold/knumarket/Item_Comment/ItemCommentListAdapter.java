package com.harold.knumarket.Item_Comment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ����� Ŭ���� ����
 * 
 * @author Mike
 *
 */
public class ItemCommentListAdapter extends BaseAdapter {

	private Context mContext;

	private List<ItemComment> mItems = new ArrayList<ItemComment>();

	public ItemCommentListAdapter(Context context) {
		mContext = context;
	}

	public void addItem(ItemComment it) {
        Log.i("Item_comment addItem","cmment="+it.getData(0)+it.getData(1));
		mItems.add(it);
	}

	public void setListItems(List<ItemComment> lit) {
		mItems = lit;
	}

	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int position) {
		return mItems.get(position);
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	public boolean isSelectable(int position) {
		try {
			return mItems.get(position).isSelectable();
		} catch (IndexOutOfBoundsException ex) {
			return false;
		}
	}

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ItemCommentView itemView;
        if (convertView == null) {
            itemView = new ItemCommentView(mContext, mItems.get(position));
        } else {
            Log.i("Item_comment getView","position="+position);
            itemView = (ItemCommentView) convertView;

            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
        }
        return itemView;
    }
}
