package com.gigigo.multiplegridrecyclerview.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import com.gigigo.baserecycleradapter.viewholder.BaseViewHolder;
import com.gigigo.multiplegridrecyclerview.entities.CellBlankElement;
import com.gigigo.multiplegridrecyclerview.R;

public class CellBlankViewHolder extends BaseViewHolder<CellBlankElement> {

  public CellBlankViewHolder(Context context, ViewGroup parent) {
    super(context, parent, R.layout.cell_blank_content_item);
  }

  @Override public void bindTo(CellBlankElement item, int position) {
  }
}
