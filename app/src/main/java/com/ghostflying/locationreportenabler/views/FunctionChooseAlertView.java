package com.ghostflying.locationreportenabler.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghostflying.locationreportenabler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghostflying on 2016/12/25.
 */

public class FunctionChooseAlertView extends LinearLayout {
    private static final int TYPE_CHOICE = 0;

    private static final int CHOICE_ENABLE_REPORT = 100;
    private static final int CHOICE_CLEAR_GMS = 101;
    private static final int CHOICE_CLEAR_MAPS = 102;
    private static final int CHOICE_REBOOT = 103;
    private static final int CHOICE_HIDE = 104;

    private List<ChoiceItemViewHolder> mChoiceVHs;

    public FunctionChooseAlertView(Context context) {
        super(context);
        init();
    }

    public boolean[] getSelectedFunctions () {
        boolean[] functions = new boolean[mChoiceVHs.size()];
        for (int i = 0; i < mChoiceVHs.size(); i++) {
            functions[i] = mChoiceVHs.get(i).isChecked();
        }
        return functions;
    }

    private void init() {
        setOrientation(VERTICAL);
        setPadding(0, dip2px(10), 0, 0);
        addChildViews();
    }

    private void addChildViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        // order is important, and sort by choice type id may fix it but seems meaningless.

        // enable report
        addMultiChoiceItemView(inflater, CHOICE_ENABLE_REPORT, true);

        // TODO add custom code view

        // clear gms
        addMultiChoiceItemView(inflater, CHOICE_CLEAR_GMS, false);

        // clear maps
        addMultiChoiceItemView(inflater, CHOICE_CLEAR_MAPS, false);

        // reboot
        addMultiChoiceItemView(inflater, CHOICE_REBOOT, false);

        // hide
        addMultiChoiceItemView(inflater, CHOICE_HIDE, false);
    }

    private void addMultiChoiceItemView(LayoutInflater inflater, int choiceType, boolean mustChoose) {
        // item
        View itemView = inflater.inflate(R.layout.multi_choice_item, null);
        ChoiceItemViewHolder vh = new ChoiceItemViewHolder((ViewGroup) itemView, choiceType, mustChoose);
        if (mChoiceVHs == null) {
            mChoiceVHs = new ArrayList<>(5);
        }
        mChoiceVHs.add(vh);
        addView(itemView);
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private static abstract class ItemViewHolder {
        abstract int getType();
    }

    private static class ChoiceItemViewHolder extends ItemViewHolder {
        private CheckBox mCheckBox;
        private TextView mTv;
        private int mChoiceType;

        ChoiceItemViewHolder (ViewGroup view, int choiceType) {
            mCheckBox = (CheckBox) view.findViewById(R.id.item_cb);
            mTv = (TextView) view.findViewById(R.id.item_text);
            this.mChoiceType = choiceType;
            setText();
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCheckBox.isEnabled()) {
                        mCheckBox.setChecked(!mCheckBox.isChecked());
                    }
                }
            });
        }

        ChoiceItemViewHolder (ViewGroup viewGroup, int choiceType, boolean mustChosen) {
            this(viewGroup, choiceType);
            if (mustChosen) {
                mCheckBox.setChecked(true);
                mCheckBox.setEnabled(false);
            }
        }

        private void setText () {
            switch (mChoiceType) {
                case CHOICE_ENABLE_REPORT:
                    mTv.setText(R.string.choice_set_fake_operator);
                    break;
                case CHOICE_CLEAR_GMS:
                    mTv.setText(R.string.choice_clear_gms_data);
                    break;
                case CHOICE_CLEAR_MAPS:
                    mTv.setText(R.string.choice_clear_maps_data);
                    break;
                case CHOICE_REBOOT:
                    mTv.setText(R.string.choice_reboot);
                    break;
                case CHOICE_HIDE:
                    mTv.setText(R.string.choice_hide_from_launcher);
                    break;
            }
        }

        @Override
        int getType() {
            return TYPE_CHOICE;
        }

        int getChoiceType() {
            return mChoiceType;
        }

        boolean isChecked() {
            return mCheckBox.isChecked();
        }
    }
}
