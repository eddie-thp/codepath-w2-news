package org.ethp.codepath.oldnews.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import org.ethp.codepath.oldnews.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchSettingsFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.etBeginDate)
    EditText etBeginDate;
    @BindView(R.id.spnSortBy)
    Spinner spnSortBy;
    @BindView(R.id.cbArts)
    CheckBox cbArts;
    @BindView(R.id.cbFashionStyle)
    CheckBox cbFashionStyle;
    @BindView(R.id.cbSports)
    CheckBox cbSports;
    @BindView(R.id.btReset)
    Button btReset;
    @BindView(R.id.btApply)
    Button btApply;

    Date beginDate;

    private OnApplyClickedListener onApplyClickedListener;

    /**
     * Interface for receiving the settings from the Dialog when apply is clicked
     */
    public interface OnApplyClickedListener {
        void onApplyClicked(Date beginDate, int sortBySelection, boolean newsDeskArtsChecked, boolean newsDeskFashionChecked, boolean newsDeskSportsChecked);
    }

    /**
     * Sets the OnApplyClicked listener implementation
     * @param onApplyClickedListener
     */
    public void setOnApplyClickedListener(OnApplyClickedListener onApplyClickedListener) {
        this.onApplyClickedListener = onApplyClickedListener;
    }

    public SearchSettingsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_settings, container, false);
        setup(view);
        return view;
    }

    private void setup(View view) {
        ButterKnife.bind(this, view);

        etBeginDate.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();

                // Set date from begin date edit text
                Calendar cal = Calendar.getInstance();
                if (beginDate != null) {
                    cal.setTime(beginDate);
                }

                new DatePickerDialog(getActivity(), SearchSettingsFragment.this,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btReset.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginDate = null;
                etBeginDate.setText("");
                spnSortBy.setSelection(0);
                cbArts.setChecked(false);
                cbFashionStyle.setChecked(false);
                cbSports.setChecked(false);
            }
        });

        // Setup apply button to pass the dialog values to the listener
        btApply.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                OnApplyClickedListener listener = null;

                if (onApplyClickedListener != null) {
                    listener = onApplyClickedListener;
                } else if (activity instanceof OnApplyClickedListener) {
                    listener = (OnApplyClickedListener) activity;
                }

                if (listener != null) {
                    listener.onApplyClicked(beginDate,
                            spnSortBy.getSelectedItemPosition(),
                            cbArts.isChecked(),
                            cbFashionStyle.isChecked(),
                            cbSports.isChecked());
                } else {
                    // TODO should I log or throw an exception ?
                }

                dismiss();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.title_settings_dialog);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        beginDate = cal.getTime();
        etBeginDate.setText(new SimpleDateFormat(getString(R.string.date_format)).format(beginDate));
    }



}