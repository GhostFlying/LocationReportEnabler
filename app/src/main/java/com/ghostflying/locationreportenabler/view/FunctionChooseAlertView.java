package com.ghostflying.locationreportenabler.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghostflying.locationreportenabler.PropUtil;
import com.ghostflying.locationreportenabler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghostflying on 2016/12/25.
 */

public class FunctionChooseAlertView extends LinearLayout {
    private static final int TYPE_CHOICE = 0;
    private static final int TYPE_EDIT_TEXT = 1;

    private static final int CHOICE_ENABLE_REPORT = 100;
    private static final int CHOICE_CLEAR_GMS = 101;
    private static final int CHOICE_CLEAR_MAPS = 102;
    private static final int CHOICE_REBOOT = 103;
    private static final int CHOICE_HIDE = 104;

    private static final int EDIT_NUMERIC = 200;
    private static final int EDIT_COUNTRY = 201;

    private List<ChoiceItemViewHolder> mChoiceVHs;
    private List<EditTextViewHolder> mEditTextVHs;

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

    public String getOperatorNumber () {
        return mEditTextVHs.get(0).getText();
    }

    public String getOperatorCountry () {
        return mEditTextVHs.get(1).getText();
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

        // custom numeric
        addEditTextItemView(inflater, EDIT_NUMERIC);

        // custom country
        addEditTextItemView(inflater, EDIT_COUNTRY);

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

    private void addEditTextItemView(LayoutInflater inflater, int editType) {
        // item
        View itemView = inflater.inflate(R.layout.edit_text_item, null);
        EditTextViewHolder vh = new EditTextViewHolder((ViewGroup)itemView, editType);
        if (mEditTextVHs == null) {
            mEditTextVHs = new ArrayList<>(2);
        }
        mEditTextVHs.add(vh);
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

    private static class EditTextViewHolder extends ItemViewHolder {
        private TextInputLayout mTextInputLayout;
        private EditText mEditText;
        private Context mContext;
        private int mEditType;

        EditTextViewHolder(ViewGroup viewGroup, int editType) {
            mTextInputLayout = (TextInputLayout)viewGroup.findViewById(R.id.item_text_input_ly);
            mEditText = (EditText) viewGroup.findViewById(R.id.item_edit_text);
            mEditType = editType;

            mContext = mEditText.getContext();

            setup();
        }

        private void setup() {
            switch (mEditType) {
                case EDIT_NUMERIC:
                    mTextInputLayout.setHint(mContext.getString(R.string.hint_numeric));
                    mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case EDIT_COUNTRY:
                    mTextInputLayout.setHint(mContext.getString(R.string.hint_country));
                    break;
            }
            mEditText.setText(getInitValue());
        }

        private String getInitValue() {
            SharedPreferences preferences = PropUtil.getProtecredSharedPreferences(mContext);

            switch (mEditType) {
                case EDIT_COUNTRY:
                    return preferences.getString(
                            PropUtil.PREFERENCE_FAKE_COUNTRY,
                            PropUtil.PREFERENCE_FAKE_COUNTRY_DEFAULT
                    );
                case EDIT_NUMERIC:
                    return preferences.getString(
                            PropUtil.PREFERENCE_FAKE_NUMERIC,
                            PropUtil.PREFERENCE_FAKE_NUMERIC_DEFAULT
                    );
            }
            return "";
        }

        private String getText() {
            return mEditText.getText().toString();
        }

        @Override
        int getType() {
            return TYPE_EDIT_TEXT;
        }
    }
}
