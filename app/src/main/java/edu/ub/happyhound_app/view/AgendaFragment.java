package edu.ub.happyhound_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import edu.ub.happyhound_app.R;
import edu.ub.happyhound_app.model.DynamicRV_Adapter;
import edu.ub.happyhound_app.model.NotificationManager;
import edu.ub.happyhound_app.model.Reminder;
import edu.ub.happyhound_app.model.ReminderManager;
import edu.ub.happyhound_app.model.SearchDatabase;
import edu.ub.happyhound_app.model.StorageListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgendaFragment extends Fragment implements StorageListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Reminder> paseoRemindersList, comidaRemindersList, otherRemindersList;
    //    private DynamicRV_AdapterPaseo adapterPaseo;
//    private DynamicRV_AdapterComida adapterComida;
    private DynamicRV_Adapter adapterOtros, adapterPaseo, adapterComida;
    private FloatingActionButton addNotification;
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
        database.getReminders(this, "Paseo");
        database.getReminders(this, "Comida");
        database.getReminders(this, "Otros");
        paseoRemindersList = new ArrayList<>();
        comidaRemindersList = new ArrayList<>();
        otherRemindersList = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda, container, false);

        RecyclerView paseoRecyclerView = view.findViewById(R.id.rv_reminderPaseoList);
        RecyclerView comidaRecyclerView = view.findViewById(R.id.rv_reminderComidaList);
        RecyclerView otherRecyclerView = view.findViewById(R.id.rv_reminderOtrosList);

        adapterPaseo = setRecycleView(paseoRecyclerView, paseoRemindersList);
        adapterComida = setRecycleView(comidaRecyclerView, comidaRemindersList);
        adapterOtros = setRecycleView(otherRecyclerView, otherRemindersList);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addNotification = view.findViewById(R.id.floatingAddNotification);
        addNotification.setOnClickListener(view2 -> {
            Intent intent = new Intent(requireActivity(), SetReminder.class);
            startActivity(intent);
        });
    }

    @Override
    public void onSuccessPaseoList() {
        List<Reminder> paseoReminders = ReminderManager.getPaseoReminderList();
        paseoRemindersList.addAll(paseoReminders);
        adapterPaseo.notifyDataSetChanged();
    }

    @Override
    public void onSuccessComidaList() {
        List<Reminder> comidaReminders = ReminderManager.getComidaReminderList();
        comidaRemindersList.addAll(comidaReminders);
        adapterComida.notifyDataSetChanged();
    }

    @Override
    public void onSuccessOtherList() {
        List<Reminder> otherReminders = ReminderManager.getOtherReminderList();
        otherRemindersList.addAll(otherReminders);
        adapterOtros.notifyDataSetChanged();
    }

    private DynamicRV_Adapter setRecycleView(RecyclerView recyclerView, List<Reminder> remindersList) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DynamicRV_Adapter rvAdapter = new DynamicRV_Adapter(recyclerView, getActivity(), remindersList);
        recyclerView.setAdapter(rvAdapter);

        rvAdapter.setLoadMore(() -> {

            if (remindersList.size() <= 5) {
                remindersList.add(null);
                rvAdapter.notifyItemInserted(remindersList.size() - 1);
                new Handler().postDelayed(() -> {
                    remindersList.remove(remindersList.size() - 1);
                    rvAdapter.notifyItemRemoved(remindersList.size());
                }, 3000);
            }
        });

        return rvAdapter;
    }


}