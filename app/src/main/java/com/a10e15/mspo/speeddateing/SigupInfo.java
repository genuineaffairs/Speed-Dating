package com.a10e15.mspo.speeddateing;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import static com.a10e15.mspo.speeddateing.SplashAct.user;

public class SigupInfo extends AppCompatActivity {

    public static final String ARG_EMAIL = "email_final";
    public static final String ARG_NAME = "name_final";
    public static final String ARG_SIGNHUP = "fial_sighnup";
    public String _email;
    public String _name;
    public static User cUser;
    public static final String ARG_UID = "uid_final";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private NoSwipePager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sigup_info);
        Bundle bd = this.getIntent().getExtras().getBundle(ARG_SIGNHUP);

            _email = bd.getString(ARG_EMAIL);
            _name = bd.getString(ARG_NAME);
            String uid = bd.getString(ARG_UID);
            cUser = User.writeNewUser(uid, _name, _email);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (NoSwipePager) findViewById(R.id.container);
        mViewPager.setPagingEnabled(false);
        mSectionsPagerAdapter.addFragments(WhoAmI.newInstance(null, null, null));
        mSectionsPagerAdapter.addFragments(ContactInfo.newInstance(null));
        mSectionsPagerAdapter.addFragments(FindWho.newInstance());
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }

    public void next_fragment(View view) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

    public void finishAccSetup(View view) {
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            mSectionsPagerAdapter.getItem(i);
        }
    }

    public void previous_fragment(View view) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }


    public void setCurrentItem(int item, boolean smoothScroll) {
        mViewPager.setCurrentItem(item, smoothScroll);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class WhoAmI extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_GENDER = "gender_str";
        private static final String ARG_RACE = "race_str";
        private static final String ARG_DOB = "dateofbirth_str";
        String gender;
        String race;
        String dob;
        public Bundle bd;

        public WhoAmI() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static WhoAmI newInstance(String gender, String race, String dob) {
            WhoAmI fragment = new WhoAmI();
            Bundle args = new Bundle();
            args.putString(ARG_GENDER, gender);
            args.putString(ARG_RACE, race);
            args.putString(ARG_DOB, dob);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle args = getArguments();
            gender = (String) args.get(ARG_GENDER);
            race = (String) args.get(ARG_RACE);
            dob = (String) args.get(ARG_DOB);


            final View rootView = inflater.inflate(R.layout.fragment_sigup_info, container, false);

            rootView.findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int cheaked1 = ((RadioGroup) rootView.findViewById(R.id.genderSelecter)).getCheckedRadioButtonId();
                    gender = ((RadioButton) rootView.findViewById(cheaked1)).getText().toString();
                    int cheaked2 = ((RadioGroup) rootView.findViewById(R.id.raceSelection)).getCheckedRadioButtonId();
                    race = ((RadioButton) rootView.findViewById(cheaked2)).getText().toString();
                    final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    dob = dateFormatter.format(getDateFromDatePicker(((DatePicker) rootView.findViewById(R.id.dobPicker))));
                    cUser.gender = gender;
                    cUser.race = race;
                    cUser.dob = dob;
                    cUser.updateUser();
                    ((SigupInfo) getActivity()).next_fragment(view);

                }
            });


            return rootView;
        }


    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ContactInfo extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_CONTACT_INFO = "contact_info";
        String contact_info;
        Bundle bd;

        public ContactInfo() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static ContactInfo newInstance(String contact_info) {
            ContactInfo fragment = new ContactInfo();
            Bundle args = new Bundle();
            args.putString(ARG_CONTACT_INFO, contact_info);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle args = getArguments();

            final View rootView = inflater.inflate(R.layout.fragment_sigup_info2, container, false);
            bd = new Bundle();
            ((AutoCompleteTextView) rootView.findViewById(R.id.contactInfo)).setText((String) args.get(ARG_CONTACT_INFO));

            rootView.findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contact_info = ((AutoCompleteTextView) rootView.findViewById(R.id.contactInfo)).getText().toString();
                    cUser.contactInfo = contact_info;
                    cUser.updateUser();
                    ((SigupInfo) getActivity()).next_fragment(view);
                }
            });


            return rootView;
        }


    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class LookingFor extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_FRAGMENT_ID = "fragment_id";
        Bundle bd;

        public LookingFor() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static LookingFor newInstance(int fragmentid) {
            LookingFor fragment = new LookingFor();
            Bundle args = new Bundle();
            args.putInt(ARG_FRAGMENT_ID, fragmentid);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle args = getArguments();

            View rootView = inflater.inflate(R.layout.fragment_sigup_info4, container, false);
            bd = new Bundle();


            return rootView;
        }


    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class FindWho extends Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        Bundle bd;

        public FindWho() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static FindWho newInstance() {
            FindWho fragment = new FindWho();
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.selection_sigup_info, container, false);
            bd = new Bundle();


            rootView.findViewById(R.id.lookSomeoneBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((SigupInfo) getActivity()).mSectionsPagerAdapter.addFragments(LookingFor.newInstance(0));
                    ((SigupInfo) getActivity()).mSectionsPagerAdapter.notifyDataSetChanged();
                    ((SigupInfo) getActivity()).next_fragment(view);
                }
            });
            rootView.findViewById(R.id.topicBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((SigupInfo) getActivity()).mSectionsPagerAdapter.addFragments(TopicChooser.newInstance(0));
                    ((SigupInfo) getActivity()).mSectionsPagerAdapter.notifyDataSetChanged();
                    ((SigupInfo) getActivity()).next_fragment(view);
                }
            });

            return rootView;
        }


        @Override
        public void onClick(View view) {
            if (view instanceof Button) {

            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TopicChooser extends Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_FRAGMENT_ID = "fragment_id";
        Bundle bd;

        public TopicChooser() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static TopicChooser newInstance(int fragmentid) {
            TopicChooser fragment = new TopicChooser();

            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle args = getArguments();

            View rootView = inflater.inflate(R.layout.fragment_sigup_info3, container, false);
            bd = new Bundle();


            return rootView;
        }


        @Override
        public void onClick(View view) {
            if (view instanceof Button) {

            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            String fragment_layout;
            switch (position) {
                case 0:
                    fragment_layout = "I am..";
                    break;
                case 1:
                    fragment_layout = "My contact info";
                    break;
                default:
                    fragment_layout = "What are you looking for?";
                    break;
            }
            return fragment_layout;
        }

        // Our custom method that populates this Adapter with Fragments
        public void addFragments(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    /**
     * @param datePicker
     * @return a java.util.Date
     */
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
