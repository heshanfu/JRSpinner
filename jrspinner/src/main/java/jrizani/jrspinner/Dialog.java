package jrizani.jrspinner;
/*=============================*/
/*            AUTHOR           */
/*          JULIAN NR          */
/* juliannoorrizani@gmail.com  */
/*         19 Feb 2019         */
/*=============================*/

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Dialog extends DialogFragment {

    private String[] data;
    private String title;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private TextView tvTitle;
    private Adapter adapter;
    private ImageView reset;
    private View root;
    private CardView card;
    private JRSpinner view;
    private JRSpinner.OnItemClickListener listener;
    private int selected;

    public Dialog() {
    }

    public void setListener(JRSpinner.OnItemClickListener listener, JRSpinner view) {
        this.listener = listener;
        this.view = view;
    }

    public static Dialog newInstance(String title, String[] data, int selected) {
        Dialog instance = new Dialog();
        Bundle arguments = new Bundle();
        arguments.putStringArray("data", data);
        arguments.putString("title", title);
        arguments.putInt("selected", selected);
        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);

        if (getArguments() != null && getArguments().getStringArray("data") != null && getArguments().getString("title") != null) {
            data = getArguments().getStringArray("data");
            title = getArguments().getString("title");
            selected = getArguments().getInt("selected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jrspinner_layout_dialog, container, false);
        etSearch = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTitle = view.findViewById(R.id.title);
        reset = view.findViewById(R.id.reset);
        root = view.findViewById(R.id.root);
        card = view.findViewById(R.id.card);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        android.app.Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (data != null) {
            tvTitle.setText(title);
            adapter = new Adapter(new Adapter.Listener() {
                @Override
                public void onClick(Pair<Integer, String> item, int position) {
                    Dialog.this.view.setText(item.second);
                    Dialog.this.view.setSelected(item.first);
                    if (listener != null) {
                        listener.onItemClick(position);
                    }
                    dismiss();
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter.update(data, selected);
            recyclerView.setAdapter(adapter);
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    adapter.update(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        reset.setVisibility(View.VISIBLE);
                    } else {
                        reset.setVisibility(View.GONE);
                    }
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.reset();
                    etSearch.setText("");
                }
            });

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //do nothing
                }
            });
        } else {
            dismiss();
        }
    }
}
