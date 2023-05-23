package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.DynamicRV_Adapter;
import edu.ub.happyhound_app.model.FirebaseListener;
import edu.ub.happyhound_app.model.NotificationManager;
import edu.ub.happyhound_app.model.Reminder;
import edu.ub.happyhound_app.model.ReminderManager;
import edu.ub.happyhound_app.model.SearchDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaFragment extends Fragment implements FirebaseListener {

    private List<Reminder> remindersList;
    private DynamicRV_Adapter dynamicRVAdapter;
    private FloatingActionButton addNotification;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgendaFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgendaFragment newInstance(String param1, String param2) {
        AgendaFragment fragment = new AgendaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // eliminamos las notificaciones que ya han pasado
        NotificationManager notificationManager = new NotificationManager(requireActivity());
        notificationManager.reminderPassed();

        // buscamos las notificaciones pendientes
        SearchDatabase database = new SearchDatabase();
        database.getAllReminders(this);
        remindersList = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_reminderOtrosList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dynamicRVAdapter = new DynamicRV_Adapter(recyclerView, getActivity(), remindersList);
        recyclerView.setAdapter(dynamicRVAdapter);

        dynamicRVAdapter.setLoadMore(() -> {

            if (remindersList.size() <= 5) {
                remindersList.add(null);
                dynamicRVAdapter.notifyItemInserted(remindersList.size() - 1);
                new Handler().postDelayed(() -> {
                    remindersList.remove(remindersList.size() - 1);
                    dynamicRVAdapter.notifyItemRemoved(remindersList.size());
                }, 3000);
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addNotification = view.findViewById(R.id.floatingAddNotification);
        addNotification.setOnClickListener(view2 -> {
            Intent intent = new Intent(getActivity(), CrearRecordatorio.class);
            startActivity(intent);
        });
    }

    @Override
    public void onSuccess() {
        List<Reminder> reminders = ReminderManager.getReminderList();
        remindersList.addAll(reminders);
        dynamicRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure() {
    }
}