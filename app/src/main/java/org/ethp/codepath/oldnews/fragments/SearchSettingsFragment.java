package org.ethp.codepath.oldnews.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.ethp.codepath.oldnews.R;
import org.ethp.codepath.oldnews.databinding.FragmentSearchSettingsBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.ethp.codepath.oldnews.R.id.cbArts;
import static org.ethp.codepath.oldnews.R.id.cbFashionStyle;
import static org.ethp.codepath.oldnews.R.id.cbSports;
import static org.ethp.codepath.oldnews.R.id.etBeginDate;
import static org.ethp.codepath.oldnews.R.id.spnSortBy;

public class SearchSettingsFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    FragmentSearchSettingsBinding binding;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_settings, container, false);
        setup();
        return binding.getRoot();
    }

    private void setup() {
        binding.etBeginDate.setOnClickListener(new EditText.OnClickListener() {
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

        binding.btReset.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginDate = null;
                binding.etBeginDate.setText("");
                binding.spnSortBy.setSelection(0);
                binding.cbArts.setChecked(false);
                binding.cbFashionStyle.setChecked(false);
                binding.cbSports.setChecked(false);
            }
        });

        // Setup apply button to pass the dialog values to the listener
        binding.btApply.setOnClickListener(new Button.OnClickListener() {
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
                            binding.spnSortBy.getSelectedItemPosition(),
                            binding.cbArts.isChecked(),
                            binding.cbFashionStyle.isChecked(),
                            binding.cbSports.isChecked());
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
        binding.etBeginDate.setText(new SimpleDateFormat(getString(R.string.date_format)).format(beginDate));
    }



}